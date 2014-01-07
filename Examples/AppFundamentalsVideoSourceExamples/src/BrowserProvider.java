/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.browser.provider;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R;
import android.app.SearchManager;
import android.app.backup.BackupManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.UriMatcher;
import android.content.res.Configuration;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Process;
import android.preference.PreferenceManager;
import android.provider.Browser;
import android.provider.Browser.BookmarkColumns;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.android.browser.BrowserSettings;
import com.android.browser.search.SearchEngine;


public class BrowserProvider extends ContentProvider {

    private SQLiteOpenHelper mOpenHelper;
    private BackupManager mBackupManager;
    static final String sDatabaseName = "browser.db";
    private static final String TAG = "BrowserProvider";
    private static final String ORDER_BY = "visits DESC, date DESC";

    private static final String PICASA_URL = "http://picasaweb.google.com/m/" +
            "viewer?source=androidclient";

    static final String[] TABLE_NAMES = new String[] {
        "bookmarks", "searches"
    };
    private static final String[] SUGGEST_PROJECTION = new String[] {
            "_id", "url", "title", "bookmark", "user_entered"
    };
    private static final String SUGGEST_SELECTION =
            "(url LIKE ? OR url LIKE ? OR url LIKE ? OR url LIKE ?"
                + " OR title LIKE ?) AND (bookmark = 1 OR user_entered = 1)";
    private String[] SUGGEST_ARGS = new String[5];

    // shared suggestion array index, make sure to match COLUMNS
    private static final int SUGGEST_COLUMN_INTENT_ACTION_ID = 1;
    private static final int SUGGEST_COLUMN_INTENT_DATA_ID = 2;
    private static final int SUGGEST_COLUMN_TEXT_1_ID = 3;
    private static final int SUGGEST_COLUMN_TEXT_2_ID = 4;
    private static final int SUGGEST_COLUMN_TEXT_2_URL_ID = 5;
    private static final int SUGGEST_COLUMN_ICON_1_ID = 6;
    private static final int SUGGEST_COLUMN_ICON_2_ID = 7;
    private static final int SUGGEST_COLUMN_QUERY_ID = 8;
    private static final int SUGGEST_COLUMN_INTENT_EXTRA_DATA = 9;

    // how many suggestions will be shown in dropdown
    // 0..SHORT: filled by browser db
    private static final int MAX_SUGGEST_SHORT_SMALL = 3;
    // SHORT..LONG: filled by search suggestions
    private static final int MAX_SUGGEST_LONG_SMALL = 6;

    // large screen size shows more
    private static final int MAX_SUGGEST_SHORT_LARGE = 6;
    private static final int MAX_SUGGEST_LONG_LARGE = 9;


    // shared suggestion columns
    private static final String[] COLUMNS = new String[] {
            "_id",
            SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_TEXT_2,
            SearchManager.SUGGEST_COLUMN_TEXT_2_URL,
            SearchManager.SUGGEST_COLUMN_ICON_1,
            SearchManager.SUGGEST_COLUMN_ICON_2,
            SearchManager.SUGGEST_COLUMN_QUERY,
            SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA};


    // make sure that these match the index of TABLE_NAMES
    static final int URI_MATCH_BOOKMARKS = 0;
    private static final int URI_MATCH_SEARCHES = 1;
    // (id % 10) should match the table name index
    private static final int URI_MATCH_BOOKMARKS_ID = 10;
    private static final int URI_MATCH_SEARCHES_ID = 11;
    //
    private static final int URI_MATCH_SUGGEST = 20;
    private static final int URI_MATCH_BOOKMARKS_SUGGEST = 21;

    private static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI("browser", TABLE_NAMES[URI_MATCH_BOOKMARKS],
                URI_MATCH_BOOKMARKS);
        URI_MATCHER.addURI("browser", TABLE_NAMES[URI_MATCH_BOOKMARKS] + "/#",
                URI_MATCH_BOOKMARKS_ID);
        URI_MATCHER.addURI("browser", TABLE_NAMES[URI_MATCH_SEARCHES],
                URI_MATCH_SEARCHES);
        URI_MATCHER.addURI("browser", TABLE_NAMES[URI_MATCH_SEARCHES] + "/#",
                URI_MATCH_SEARCHES_ID);
        URI_MATCHER.addURI("browser", SearchManager.SUGGEST_URI_PATH_QUERY,
                URI_MATCH_SUGGEST);
        URI_MATCHER.addURI("browser",
                TABLE_NAMES[URI_MATCH_BOOKMARKS] + "/" + SearchManager.SUGGEST_URI_PATH_QUERY,
                URI_MATCH_BOOKMARKS_SUGGEST);
    }

    // 1 -> 2 add cache table
    // 2 -> 3 update history table
    // 3 -> 4 add passwords table
    // 4 -> 5 add settings table
    // 5 -> 6 ?
    // 6 -> 7 ?
    // 7 -> 8 drop proxy table
    // 8 -> 9 drop settings table
    // 9 -> 10 add form_urls and form_data
    // 10 -> 11 add searches table
    // 11 -> 12 modify cache table
    // 12 -> 13 modify cache table
    // 13 -> 14 correspond with Google Bookmarks schema
    // 14 -> 15 move couple of tables to either browser private database or webview database
    // 15 -> 17 Set it up for the SearchManager
    // 17 -> 18 Added favicon in bookmarks table for Home shortcuts
    // 18 -> 19 Remove labels table
    // 19 -> 20 Added thumbnail
    // 20 -> 21 Added touch_icon
    // 21 -> 22 Remove "clientid"
    // 22 -> 23 Added user_entered
    // 23 -> 24 Url not allowed to be null anymore.
    private static final int DATABASE_VERSION = 24;

    // Regular expression which matches http://, followed by some stuff, followed by
    // optionally a trailing slash, all matched as separate groups.
    private static final Pattern STRIP_URL_PATTERN = Pattern.compile("^(http://)(.*?)(/$)?");

    private BrowserSettings mSettings;

    private int mMaxSuggestionShortSize;
    private int mMaxSuggestionLongSize;

    public BrowserProvider() {
    }

    // XXX: This is a major hack to remove our dependency on gsf constants and
    // its content provider. http://b/issue?id=2425179
    public static String getClientId(ContentResolver cr) {
        String ret = "android-google";
        Cursor legacyClientIdCursor = null;
        Cursor searchClientIdCursor = null;

        // search_client_id includes search prefix, legacy client_id does not include prefix
        try {
            searchClientIdCursor = cr.query(Uri.parse("content://com.google.settings/partner"),
               new String[] { "value" }, "name='search_client_id'", null, null);
            if (searchClientIdCursor != null && searchClientIdCursor.moveToNext()) {
                ret = searchClientIdCursor.getString(0);
            } else {
                legacyClientIdCursor = cr.query(Uri.parse("content://com.google.settings/partner"),
                    new String[] { "value" }, "name='client_id'", null, null);
                if (legacyClientIdCursor != null && legacyClientIdCursor.moveToNext()) {
                    ret = "ms-" + legacyClientIdCursor.getString(0);
                }
            }
        } catch (RuntimeException ex) {
            // fall through to return the default
        } finally {
            if (legacyClientIdCursor != null) {
                legacyClientIdCursor.close();
            }
            if (searchClientIdCursor != null) {
                searchClientIdCursor.close();
            }
        }
        return ret;
    }

    private static CharSequence replaceSystemPropertyInString(Context context, CharSequence srcString) {
        StringBuffer sb = new StringBuffer();
        int lastCharLoc = 0;

        final String client_id = getClientId(context.getContentResolver());

        for (int i = 0; i < srcString.length(); ++i) {
            char c = srcString.charAt(i);
            if (c == '{') {
                sb.append(srcString.subSequence(lastCharLoc, i));
                lastCharLoc = i;
          inner:
                for (int j = i; j < srcString.length(); ++j) {
                    char k = srcString.charAt(j);
                    if (k == '}') {
                        String propertyKeyValue = srcString.subSequence(i + 1, j).toString();
                        if (propertyKeyValue.equals("CLIENT_ID")) {
                            sb.append(client_id);
                        } else {
                            sb.append("unknown");
                        }
                        lastCharLoc = j + 1;
                        i = j;
                        break inner;
                    }
                }
            }
        }
        if (srcString.length() - lastCharLoc > 0) {
            // Put on the tail, if there is one
            sb.append(srcString.subSequence(lastCharLoc, srcString.length()));
        }
        return sb;
    }

    static class DatabaseHelper extends SQLiteOpenHelper {
        private Context mContext;

        public DatabaseHelper(Context context) {
            super(context, sDatabaseName, null, DATABASE_VERSION);
            mContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE bookmarks (" +
                    "_id INTEGER PRIMARY KEY," +
                    "title TEXT," +
                    "url TEXT NOT NULL," +
                    "visits INTEGER," +
                    "date LONG," +
                    "created LONG," +
                    "description TEXT," +
                    "bookmark INTEGER," +
                    "favicon BLOB DEFAULT NULL," +
                    "thumbnail BLOB DEFAULT NULL," +
                    "touch_icon BLOB DEFAULT NULL," +
                    "user_entered INTEGER" +
                    ");");

            final CharSequence[] bookmarks = mContext.getResources()
                    .getTextArray(R.array.bookmarks);
            int size = bookmarks.length;
            try {
                for (int i = 0; i < size; i = i + 2) {
                    CharSequence bookmarkDestination = replaceSystemPropertyInString(mContext, bookmarks[i + 1]);
                    db.execSQL("INSERT INTO bookmarks (title, url, visits, " +
                            "date, created, bookmark)" + " VALUES('" +
                            bookmarks[i] + "', '" + bookmarkDestination +
                            "', 0, 0, 0, 1);");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }

            db.execSQL("CREATE TABLE searches (" +
                    "_id INTEGER PRIMARY KEY," +
                    "search TEXT," +
                    "date LONG" +
                    ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion);
            if (oldVersion == 18) {
                db.execSQL("DROP TABLE IF EXISTS labels");
            }
            if (oldVersion <= 19) {
                db.execSQL("ALTER TABLE bookmarks ADD COLUMN thumbnail BLOB DEFAULT NULL;");
            }
            if (oldVersion < 21) {
                db.execSQL("ALTER TABLE bookmarks ADD COLUMN touch_icon BLOB DEFAULT NULL;");
            }
            if (oldVersion < 22) {
                db.execSQL("DELETE FROM bookmarks WHERE (bookmark = 0 AND url LIKE \"%.google.%client=ms-%\")");
                removeGears();
            }
            if (oldVersion < 23) {
                db.execSQL("ALTER TABLE bookmarks ADD COLUMN user_entered INTEGER;");
            }
            if (oldVersion < 24) {
                /* SQLite does not support ALTER COLUMN, hence the lengthy code. */
                db.execSQL("DELETE FROM bookmarks WHERE url IS NULL;");
                db.execSQL("ALTER TABLE bookmarks RENAME TO bookmarks_temp;");
                db.execSQL("CREATE TABLE bookmarks (" +
                        "_id INTEGER PRIMARY KEY," +
                        "title TEXT," +
                        "url TEXT NOT NULL," +
                        "visits INTEGER," +
                        "date LONG," +
                        "created LONG," +
                        "description TEXT," +
                        "bookmark INTEGER," +
                        "favicon BLOB DEFAULT NULL," +
                        "thumbnail BLOB DEFAULT NULL," +
                        "touch_icon BLOB DEFAULT NULL," +
                        "user_entered INTEGER" +
                        ");");
                db.execSQL("INSERT INTO bookmarks SELECT * FROM bookmarks_temp;");
                db.execSQL("DROP TABLE bookmarks_temp;");
            } else {
                db.execSQL("DROP TABLE IF EXISTS bookmarks");
                db.execSQL("DROP TABLE IF EXISTS searches");
                onCreate(db);
            }
        }

        private void removeGears() {
            new Thread() {
                @Override
                public void run() {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    String browserDataDirString = mContext.getApplicationInfo().dataDir;
                    final String appPluginsDirString = "app_plugins";
                    final String gearsPrefix = "gears";
                    File appPluginsDir = new File(browserDataDirString + File.separator
                            + appPluginsDirString);
                    if (!appPluginsDir.exists()) {
                        return;
                    }
                    // Delete the Gears plugin files
                    File[] gearsFiles = appPluginsDir.listFiles(new FilenameFilter() {
                        public boolean accept(File dir, String filename) {
                            return filename.startsWith(gearsPrefix);
                        }
                    });
                    for (int i = 0; i < gearsFiles.length; ++i) {
                        if (gearsFiles[i].isDirectory()) {
                            deleteDirectory(gearsFiles[i]);
                        } else {
                            gearsFiles[i].delete();
                        }
                    }
                    // Delete the Gears data files
                    File gearsDataDir = new File(browserDataDirString + File.separator
                            + gearsPrefix);
                    if (!gearsDataDir.exists()) {
                        return;
                    }
                    deleteDirectory(gearsDataDir);
                }

                private void deleteDirectory(File currentDir) {
                    File[] files = currentDir.listFiles();
                    for (int i = 0; i < files.length; ++i) {
                        if (files[i].isDirectory()) {
                            deleteDirectory(files[i]);
                        }
                        files[i].delete();
                    }
                    currentDir.delete();
                }
            }.start();
        }
    }

    @Override
    public boolean onCreate() {
        final Context context = getContext();
        boolean xlargeScreenSize = (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                == Configuration.SCREENLAYOUT_SIZE_XLARGE;
        boolean isPortrait = (context.getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT);


        if (xlargeScreenSize && isPortrait) {
            mMaxSuggestionLongSize = MAX_SUGGEST_LONG_LARGE;
            mMaxSuggestionShortSize = MAX_SUGGEST_SHORT_LARGE;
        } else {
            mMaxSuggestionLongSize = MAX_SUGGEST_LONG_SMALL;
            mMaxSuggestionShortSize = MAX_SUGGEST_SHORT_SMALL;
        }
        mOpenHelper = new DatabaseHelper(context);
        mBackupManager = new BackupManager(context);
        // we added "picasa web album" into default bookmarks for version 19.
        // To avoid erasing the bookmark table, we added it explicitly for
        // version 18 and 19 as in the other cases, we will erase the table.
        if (DATABASE_VERSION == 18 || DATABASE_VERSION == 19) {
            SharedPreferences p = PreferenceManager
                    .getDefaultSharedPreferences(context);
            boolean fix = p.getBoolean("fix_picasa", true);
            if (fix) {
                fixPicasaBookmark();
                Editor ed = p.edit();
                ed.putBoolean("fix_picasa", false);
                ed.apply();
            }
        }
        mSettings = BrowserSettings.getInstance();
        return true;
    }

    private void fixPicasaBookmark() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM bookmarks WHERE " +
                "bookmark = 1 AND url = ?", new String[] { PICASA_URL });
        try {
            if (!cursor.moveToFirst()) {
                // set "created" so that it will be on the top of the list
                db.execSQL("INSERT INTO bookmarks (title, url, visits, " +
                        "date, created, bookmark)" + " VALUES('" +
                        getContext().getString(R.string.picasa) + "', '"
                        + PICASA_URL + "', 0, 0, " + new Date().getTime()
                        + ", 1);");
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /*
     * Subclass AbstractCursor so we can combine multiple Cursors and add
     * "Search the web".
     * Here are the rules.
     * 1. We only have MAX_SUGGESTION_LONG_ENTRIES in the list plus
     *      "Search the web";
     * 2. If bookmark/history entries has a match, "Search the web" shows up at
     *      the second place. Otherwise, "Search the web" shows up at the first
     *      place.
     */
    private class MySuggestionCursor extends AbstractCursor {
        private Cursor  mHistoryCursor;
        private Cursor  mSuggestCursor;
        private int     mHistoryCount;
        private int     mSuggestionCount;
        private boolean mIncludeWebSearch;
        private String  mString;
        private int     mSuggestText1Id;
        private int     mSuggestText2Id;
        private int     mSuggestText2UrlId;
        private int     mSuggestQueryId;
        private int     mSuggestIntentExtraDataId;

        public MySuggestionCursor(Cursor hc, Cursor sc, String string) {
            mHistoryCursor = hc;
            mSuggestCursor = sc;
            mHistoryCount = hc != null ? hc.getCount() : 0;
            mSuggestionCount = sc != null ? sc.getCount() : 0;
            if (mSuggestionCount > (mMaxSuggestionLongSize - mHistoryCount)) {
                mSuggestionCount = mMaxSuggestionLongSize - mHistoryCount;
            }
            mString = string;
            mIncludeWebSearch = string.length() > 0;

            // Some web suggest providers only give suggestions and have no description string for
            // items. The order of the result columns may be different as well. So retrieve the
            // column indices for the fields we need now and check before using below.
            if (mSuggestCursor == null) {
                mSuggestText1Id = -1;
                mSuggestText2Id = -1;
                mSuggestText2UrlId = -1;
                mSuggestQueryId = -1;
                mSuggestIntentExtraDataId = -1;
            } else {
                mSuggestText1Id = mSuggestCursor.getColumnIndex(
                                SearchManager.SUGGEST_COLUMN_TEXT_1);
                mSuggestText2Id = mSuggestCursor.getColumnIndex(
                                SearchManager.SUGGEST_COLUMN_TEXT_2);
                mSuggestText2UrlId = mSuggestCursor.getColumnIndex(
                        SearchManager.SUGGEST_COLUMN_TEXT_2_URL);
                mSuggestQueryId = mSuggestCursor.getColumnIndex(
                                SearchManager.SUGGEST_COLUMN_QUERY);
                mSuggestIntentExtraDataId = mSuggestCursor.getColumnIndex(
                                SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA);
            }
        }

        @Override
        public boolean onMove(int oldPosition, int newPosition) {
            if (mHistoryCursor == null) {
                return false;
            }
            if (mIncludeWebSearch) {
                if (mHistoryCount == 0 && newPosition == 0) {
                    return true;
                } else if (mHistoryCount > 0) {
                    if (newPosition == 0) {
                        mHistoryCursor.moveToPosition(0);
                        return true;
                    } else if (newPosition == 1) {
                        return true;
                    }
                }
                newPosition--;
            }
            if (mHistoryCount > newPosition) {
                mHistoryCursor.moveToPosition(newPosition);
            } else {
                mSuggestCursor.moveToPosition(newPosition - mHistoryCount);
            }
            return true;
        }

        @Override
        public int getCount() {
            if (mIncludeWebSearch) {
                return mHistoryCount + mSuggestionCount + 1;
            } else {
                return mHistoryCount + mSuggestionCount;
            }
        }

        @Override
        public String[] getColumnNames() {
            return COLUMNS;
        }

        @Override
        public String getString(int columnIndex) {
            if ((mPos != -1 && mHistoryCursor != null)) {
                int type = -1; // 0: web search; 1: history; 2: suggestion
                if (mIncludeWebSearch) {
                    if (mHistoryCount == 0 && mPos == 0) {
                        type = 0;
                    } else if (mHistoryCount > 0) {
                        if (mPos == 0) {
                            type = 1;
                        } else if (mPos == 1) {
                            type = 0;
                        }
                    }
                    if (type == -1) type = (mPos - 1) < mHistoryCount ? 1 : 2;
                } else {
                    type = mPos < mHistoryCount ? 1 : 2;
                }

                switch(columnIndex) {
                    case SUGGEST_COLUMN_INTENT_ACTION_ID:
                        if (type == 1) {
                            return Intent.ACTION_VIEW;
                        } else {
                            return Intent.ACTION_SEARCH;
                        }

                    case SUGGEST_COLUMN_INTENT_DATA_ID:
                        if (type == 1) {
                            return mHistoryCursor.getString(1);
                        } else {
                            return null;
                        }

                    case SUGGEST_COLUMN_TEXT_1_ID:
                        if (type == 0) {
                            return mString;
                        } else if (type == 1) {
                            return getHistoryTitle();
                        } else {
                            if (mSuggestText1Id == -1) return null;
                            return mSuggestCursor.getString(mSuggestText1Id);
                        }

                    case SUGGEST_COLUMN_TEXT_2_ID:
                        if (type == 0) {
                            return getContext().getString(R.string.search_the_web);
                        } else if (type == 1) {
                            return null;  // Use TEXT_2_URL instead
                        } else {
                            if (mSuggestText2Id == -1) return null;
                            return mSuggestCursor.getString(mSuggestText2Id);
                        }

                    case SUGGEST_COLUMN_TEXT_2_URL_ID:
                        if (type == 0) {
                            return null;
                        } else if (type == 1) {
                            return getHistoryUrl();
                        } else {
                            if (mSuggestText2UrlId == -1) return null;
                            return mSuggestCursor.getString(mSuggestText2UrlId);
                        }

                    case SUGGEST_COLUMN_ICON_1_ID:
                        if (type == 1) {
                            if (mHistoryCursor.getInt(3) == 1) {
                                return Integer.valueOf(
                                        R.drawable.ic_search_category_bookmark)
                                        .toString();
                            } else {
                                return Integer.valueOf(
                                        R.drawable.ic_search_category_history)
                                        .toString();
                            }
                        } else {
                            return Integer.valueOf(
                                    R.drawable.ic_search_category_suggest)
                                    .toString();
                        }

                    case SUGGEST_COLUMN_ICON_2_ID:
                        return "0";

                    case SUGGEST_COLUMN_QUERY_ID:
                        if (type == 0) {
                            return mString;
                        } else if (type == 1) {
                            // Return the url in the intent query column. This is ignored
                            // within the browser because our searchable is set to
                            // android:searchMode="queryRewriteFromData", but it is used by
                            // global search for query rewriting.
                            return mHistoryCursor.getString(1);
                        } else {
                            if (mSuggestQueryId == -1) return null;
                            return mSuggestCursor.getString(mSuggestQueryId);
                        }

                    case SUGGEST_COLUMN_INTENT_EXTRA_DATA:
                        if (type == 0) {
                            return null;
                        } else if (type == 1) {
                            return null;
                        } else {
                            if (mSuggestIntentExtraDataId == -1) return null;
                            return mSuggestCursor.getString(mSuggestIntentExtraDataId);
                        }
                }
            }
            return null;
        }

        @Override
        public double getDouble(int column) {
            throw new UnsupportedOperationException();
        }

        @Override
        public float getFloat(int column) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getInt(int column) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long getLong(int column) {
            if ((mPos != -1) && column == 0) {
                return mPos;        // use row# as the _Id
            }
            throw new UnsupportedOperationException();
        }

        @Override
        public short getShort(int column) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isNull(int column) {
            throw new UnsupportedOperationException();
        }

        // TODO Temporary change, finalize after jq's changes go in
        @Override
        public void deactivate() {
            if (mHistoryCursor != null) {
                mHistoryCursor.deactivate();
            }
            if (mSuggestCursor != null) {
                mSuggestCursor.deactivate();
            }
            super.deactivate();
        }

        @Override
        public boolean requery() {
            return (mHistoryCursor != null ? mHistoryCursor.requery() : false) |
                    (mSuggestCursor != null ? mSuggestCursor.requery() : false);
        }

        // TODO Temporary change, finalize after jq's changes go in
        @Override
        public void close() {
            super.close();
            if (mHistoryCursor != null) {
                mHistoryCursor.close();
                mHistoryCursor = null;
            }
            if (mSuggestCursor != null) {
                mSuggestCursor.close();
                mSuggestCursor = null;
            }
        }

        /**
         * Provides the title (text line 1) for a browser suggestion, which should be the
         * webpage title. If the webpage title is empty, returns the stripped url instead.
         *
         * @return the title string to use
         */
        private String getHistoryTitle() {
            String title = mHistoryCursor.getString(2 /* webpage title */);
            if (TextUtils.isEmpty(title) || TextUtils.getTrimmedLength(title) == 0) {
                title = stripUrl(mHistoryCursor.getString(1 /* url */));
            }
            return title;
        }

        /**
         * Provides the subtitle (text line 2) for a browser suggestion, which should be the
         * webpage url. If the webpage title is empty, then the url should go in the title
         * instead, and the subtitle should be empty, so this would return null.
         *
         * @return the subtitle string to use, or null if none
         */
        private String getHistoryUrl() {
            String title = mHistoryCursor.getString(2 /* webpage title */);
            if (TextUtils.isEmpty(title) || TextUtils.getTrimmedLength(title) == 0) {
                return null;
            } else {
                return stripUrl(mHistoryCursor.getString(1 /* url */));
            }
        }

    }

    @Override
    public Cursor query(Uri url, String[] projectionIn, String selection,
            String[] selectionArgs, String sortOrder)
            throws IllegalStateException {
        int match = URI_MATCHER.match(url);
        if (match == -1) {
            throw new IllegalArgumentException("Unknown URL");
        }

        if (match == URI_MATCH_SUGGEST || match == URI_MATCH_BOOKMARKS_SUGGEST) {
            // Handle suggestions
            return doSuggestQuery(selection, selectionArgs, match == URI_MATCH_BOOKMARKS_SUGGEST);
        }

        String[] projection = null;
        if (projectionIn != null && projectionIn.length > 0) {
            projection = new String[projectionIn.length + 1];
            System.arraycopy(projectionIn, 0, projection, 0, projectionIn.length);
            projection[projectionIn.length] = "_id AS _id";
        }

        String whereClause = null;
        if (match == URI_MATCH_BOOKMARKS_ID || match == URI_MATCH_SEARCHES_ID) {
            whereClause = "_id = " + url.getPathSegments().get(1);
        }

        Cursor c = mOpenHelper.getReadableDatabase().query(TABLE_NAMES[match % 10], projection,
                DatabaseUtils.concatenateWhere(whereClause, selection), selectionArgs,
                null, null, sortOrder, null);
        c.setNotificationUri(getContext().getContentResolver(), url);
        return c;
    }

    private Cursor doSuggestQuery(String selection, String[] selectionArgs, boolean bookmarksOnly) {
        String suggestSelection;
        String [] myArgs;
        if (selectionArgs[0] == null || selectionArgs[0].equals("")) {
            return new MySuggestionCursor(null, null, "");
        } else {
            String like = selectionArgs[0] + "%";
            if (selectionArgs[0].startsWith("http")
                    || selectionArgs[0].startsWith("file")) {
                myArgs = new String[1];
                myArgs[0] = like;
                suggestSelection = selection;
            } else {
                SUGGEST_ARGS[0] = "http://" + like;
                SUGGEST_ARGS[1] = "http://www." + like;
                SUGGEST_ARGS[2] = "https://" + like;
                SUGGEST_ARGS[3] = "https://www." + like;
                // To match against titles.
                SUGGEST_ARGS[4] = like;
                myArgs = SUGGEST_ARGS;
                suggestSelection = SUGGEST_SELECTION;
            }
        }

        Cursor c = mOpenHelper.getReadableDatabase().query(TABLE_NAMES[URI_MATCH_BOOKMARKS],
                SUGGEST_PROJECTION, suggestSelection, myArgs, null, null,
                ORDER_BY, Integer.toString(mMaxSuggestionLongSize));

        if (bookmarksOnly || Patterns.WEB_URL.matcher(selectionArgs[0]).matches()) {
            return new MySuggestionCursor(c, null, "");
        } else {
            // get search suggestions if there is still space in the list
            if (myArgs != null && myArgs.length > 1
                    && c.getCount() < (MAX_SUGGEST_SHORT_SMALL - 1)) {
                SearchEngine searchEngine = mSettings.getSearchEngine();
                if (searchEngine != null && searchEngine.supportsSuggestions()) {
                    Cursor sc = searchEngine.getSuggestions(getContext(), selectionArgs[0]);
                    return new MySuggestionCursor(c, sc, selectionArgs[0]);
                }
            }
            return new MySuggestionCursor(c, null, selectionArgs[0]);
        }
    }

    @Override
    public String getType(Uri url) {
        int match = URI_MATCHER.match(url);
        switch (match) {
            case URI_MATCH_BOOKMARKS:
                return "vnd.android.cursor.dir/bookmark";

            case URI_MATCH_BOOKMARKS_ID:
                return "vnd.android.cursor.item/bookmark";

            case URI_MATCH_SEARCHES:
                return "vnd.android.cursor.dir/searches";

            case URI_MATCH_SEARCHES_ID:
                return "vnd.android.cursor.item/searches";

            case URI_MATCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URL");
        }
    }

    @Override
    public Uri insert(Uri url, ContentValues initialValues) {
        boolean isBookmarkTable = false;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = URI_MATCHER.match(url);
        Uri uri = null;
        switch (match) {
            case URI_MATCH_BOOKMARKS: {
                // Insert into the bookmarks table
                long rowID = db.insert(TABLE_NAMES[URI_MATCH_BOOKMARKS], "url",
                        initialValues);
                if (rowID > 0) {
                    uri = ContentUris.withAppendedId(Browser.BOOKMARKS_URI,
                            rowID);
                }
                isBookmarkTable = true;
                break;
            }

            case URI_MATCH_SEARCHES: {
                // Insert into the searches table
                long rowID = db.insert(TABLE_NAMES[URI_MATCH_SEARCHES], "url",
                        initialValues);
                if (rowID > 0) {
                    uri = ContentUris.withAppendedId(Browser.SEARCHES_URI,
                            rowID);
                }
                break;
            }

            default:
                throw new IllegalArgumentException("Unknown URL");
        }

        if (uri == null) {
            throw new IllegalArgumentException("Unknown URL");
        }
        getContext().getContentResolver().notifyChange(uri, null);

        // Back up the new bookmark set if we just inserted one.
        // A row created when bookmarks are added from scratch will have
        // bookmark=1 in the initial value set.
        if (isBookmarkTable
                && initialValues.containsKey(BookmarkColumns.BOOKMARK)
                && initialValues.getAsInteger(BookmarkColumns.BOOKMARK) != 0) {
            mBackupManager.dataChanged();
        }
        return uri;
    }

    @Override
    public int delete(Uri url, String where, String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = URI_MATCHER.match(url);
        if (match == -1 || match == URI_MATCH_SUGGEST) {
            throw new IllegalArgumentException("Unknown URL");
        }

        // need to know whether it's the bookmarks table for a couple of reasons
        boolean isBookmarkTable = (match == URI_MATCH_BOOKMARKS_ID);
        String id = null;

        if (isBookmarkTable || match == URI_MATCH_SEARCHES_ID) {
            StringBuilder sb = new StringBuilder();
            if (where != null && where.length() > 0) {
                sb.append("( ");
                sb.append(where);
                sb.append(" ) AND ");
            }
            id = url.getPathSegments().get(1);
            sb.append("_id = ");
            sb.append(id);
            where = sb.toString();
        }

        ContentResolver cr = getContext().getContentResolver();

        // we'lll need to back up the bookmark set if we are about to delete one
        if (isBookmarkTable) {
            Cursor cursor = cr.query(Browser.BOOKMARKS_URI,
                    new String[] { BookmarkColumns.BOOKMARK },
                    "_id = " + id, null, null);
            if (cursor.moveToNext()) {
                if (cursor.getInt(0) != 0) {
                    // yep, this record is a bookmark
                    mBackupManager.dataChanged();
                }
            }
            cursor.close();
        }

        int count = db.delete(TABLE_NAMES[match % 10], where, whereArgs);
        cr.notifyChange(url, null);
        return count;
    }

    @Override
    public int update(Uri url, ContentValues values, String where,
            String[] whereArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int match = URI_MATCHER.match(url);
        if (match == -1 || match == URI_MATCH_SUGGEST) {
            throw new IllegalArgumentException("Unknown URL");
        }

        if (match == URI_MATCH_BOOKMARKS_ID || match == URI_MATCH_SEARCHES_ID) {
            StringBuilder sb = new StringBuilder();
            if (where != null && where.length() > 0) {
                sb.append("( ");
                sb.append(where);
                sb.append(" ) AND ");
            }
            String id = url.getPathSegments().get(1);
            sb.append("_id = ");
            sb.append(id);
            where = sb.toString();
        }

        ContentResolver cr = getContext().getContentResolver();

        // Not all bookmark-table updates should be backed up.  Look to see
        // whether we changed the title, url, or "is a bookmark" state, and
        // request a backup if so.
        if (match == URI_MATCH_BOOKMARKS_ID || match == URI_MATCH_BOOKMARKS) {
            boolean changingBookmarks = false;
            // Alterations to the bookmark field inherently change the bookmark
            // set, so we don't need to query the record; we know a priori that
            // we will need to back up this change.
            if (values.containsKey(BookmarkColumns.BOOKMARK)) {
                changingBookmarks = true;
            } else if ((values.containsKey(BookmarkColumns.TITLE)
                     || values.containsKey(BookmarkColumns.URL))
                     && values.containsKey(BookmarkColumns._ID)) {
                // If a title or URL has been changed, check to see if it is to
                // a bookmark.  The ID should have been included in the update,
                // so use it.
                Cursor cursor = cr.query(Browser.BOOKMARKS_URI,
                        new String[] { BookmarkColumns.BOOKMARK },
                        BookmarkColumns._ID + " = "
                        + values.getAsString(BookmarkColumns._ID), null, null);
                if (cursor.moveToNext()) {
                    changingBookmarks = (cursor.getInt(0) != 0);
                }
                cursor.close();
            }

            // if this *is* a bookmark row we're altering, we need to back it up.
            if (changingBookmarks) {
                mBackupManager.dataChanged();
            }
        }

        int ret = db.update(TABLE_NAMES[match % 10], values, where, whereArgs);
        cr.notifyChange(url, null);
        return ret;
    }

    /**
     * Strips the provided url of preceding "http://" and any trailing "/". Does not
     * strip "https://". If the provided string cannot be stripped, the original string
     * is returned.
     *
     * TODO: Put this in TextUtils to be used by other packages doing something similar.
     *
     * @param url a url to strip, like "http://www.google.com/"
     * @return a stripped url like "www.google.com", or the original string if it could
     *         not be stripped
     */
    private static String stripUrl(String url) {
        if (url == null) return null;
        Matcher m = STRIP_URL_PATTERN.matcher(url);
        if (m.matches() && m.groupCount() == 3) {
            return m.group(2);
        } else {
            return url;
        }
    }

    public static Cursor getBookmarksSuggestions(ContentResolver cr, String constraint) {
        Uri uri = Uri.parse("content://browser/" + SearchManager.SUGGEST_URI_PATH_QUERY);
        return cr.query(uri, SUGGEST_PROJECTION, SUGGEST_SELECTION,
            new String[] { constraint }, ORDER_BY);
    }

}
