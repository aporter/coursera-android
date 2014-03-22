/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.google.android.wikinotes.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * The Wikinotes Content Provider is the guts of the wikinotes application. It
 * handles Content URLs to list all wiki notes, retrieve a note by name, or
 * return a list of matching notes based on text in the body or the title of
 * the note. It also handles all set up of the database, and reports back MIME
 * types for the individual notes or lists of notes to help Android route the
 * data to activities that can display that data (in this case either the
 * WikiNotes activity itself, or the WikiNotesList activity to list multiple
 * notes).
 */
public class WikiNotesProvider extends ContentProvider {

    private DatabaseHelper dbHelper;

    private static final String TAG = "WikiNotesProvider";
    private static final String DATABASE_NAME = "wikinotes.db";
    private static final int DATABASE_VERSION = 2;

    private static HashMap<String, String> NOTES_LIST_PROJECTION_MAP;

    private static final int NOTES = 1;
    private static final int NOTE_NAME = 2;
    private static final int NOTE_SEARCH = 3;

    private static final UriMatcher URI_MATCHER;

    /**
     * This inner DatabaseHelper class defines methods to create and upgrade
     * the database from previous versions.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
	public DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL("CREATE TABLE wikinotes (_id INTEGER PRIMARY KEY,"
		       + "title TEXT COLLATE LOCALIZED NOT NULL,"
		       + "body TEXT," + "created INTEGER,"
		       + "modified INTEGER" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion,
			      int newVersion) {
	    Log.w(TAG, "Upgrading database from version " + oldVersion +
		       " to " + newVersion +
		       ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS wikinotes");
	    onCreate(db);
	}
    }

    @Override
    public boolean onCreate() {
	// On the creation of the content provider, open the database,
	// the Database helper will create a new version of the database
	// if needed through the functionality in SQLiteOpenHelper
	dbHelper = new DatabaseHelper(getContext());
	return true;
    }

    /**
     * Figure out which query to run based on the URL requested and any other
     * parameters provided.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sort) {
	// Query the database using the arguments provided
	SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	String[] whereArgs = null;

	// What type of query are we going to use - the URL_MATCHER
	// defined at the bottom of this class is used to pattern-match
	// the URL and select the right query from the switch statement
	switch (URI_MATCHER.match(uri)) {
	case NOTES:
	    // this match lists all notes
	    qb.setTables("wikinotes");
	    qb.setProjectionMap(NOTES_LIST_PROJECTION_MAP);
	    break;

	case NOTE_SEARCH:
	    // this match searches for a text match in the body of notes
	    qb.setTables("wikinotes");
	    qb.setProjectionMap(NOTES_LIST_PROJECTION_MAP);
	    qb.appendWhere("body like ? or title like ?");
	    whereArgs = new String[2];
	    whereArgs[0] = whereArgs[1] = "%" + uri.getLastPathSegment() +
					  "%";
	    break;

	case NOTE_NAME:
	    // this match searches for an exact match for a specific note name
	    qb.setTables("wikinotes");
	    qb.appendWhere("title=?");
	    whereArgs = new String[] { uri.getLastPathSegment() };
	    break;

	default:
	    // anything else is considered and illegal request
	    throw new IllegalArgumentException("Unknown URL " + uri);
	}

	// If no sort order is specified use the default
	String orderBy;
	if (TextUtils.isEmpty(sort)) {
	    orderBy = WikiNote.Notes.DEFAULT_SORT_ORDER;
	} else {
	    orderBy = sort;
	}

	// Run the query and return the results as a Cursor
	SQLiteDatabase mDb = dbHelper.getReadableDatabase();
	Cursor c = qb.query(mDb, projection, null, whereArgs, null, null,
			    orderBy);
	c.setNotificationUri(getContext().getContentResolver(), uri);
	return c;
    }

    /**
     * For a given URL, return a MIME type for the data that will be returned
     * from that URL. This will help Android decide what to do with the data
     * it gets back.
     */
    @Override
    public String getType(Uri uri) {
	switch (URI_MATCHER.match(uri)) {
	case NOTES:
	case NOTE_SEARCH:
	    // for a notes list, or search results, return a mimetype
	    // indicating
	    // a directory of wikinotes
	    return "vnd.android.cursor.dir/vnd.google.wikinote";

	case NOTE_NAME:
	    // for a specific note name, return a mimetype indicating a single
	    // item representing a wikinote
	    return "vnd.android.cursor.item/vnd.google.wikinote";

	default:
	    // any other kind of URL is illegal
	    throw new IllegalArgumentException("Unknown URL " + uri);
	}
    }

    /**
     * For a URL and a list of initial values, insert a row using those values
     * into the database.
     */
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
	long rowID;
	ContentValues values;
	if (initialValues != null) {
	    values = new ContentValues(initialValues);
	} else {
	    values = new ContentValues();
	}

	// We can only insert a note, no other URLs make sense here
	if (URI_MATCHER.match(uri) != NOTE_NAME) {
	    throw new IllegalArgumentException("Unknown URL " + uri);
	}

	// Update the modified time of the record
	Long now = Long.valueOf(System.currentTimeMillis());

	// Make sure that the fields are all set
	if (values.containsKey(WikiNote.Notes.CREATED_DATE) == false) {
	    values.put(WikiNote.Notes.CREATED_DATE, now);
	}

	if (values.containsKey(WikiNote.Notes.MODIFIED_DATE) == false) {
	    values.put(WikiNote.Notes.MODIFIED_DATE, now);
	}

	if (values.containsKey(WikiNote.Notes.TITLE) == false) {
	    values.put(WikiNote.Notes.TITLE, uri.getLastPathSegment());
	}

	if (values.containsKey(WikiNote.Notes.BODY) == false) {
	    values.put(WikiNote.Notes.BODY, "");
	}

	SQLiteDatabase db = dbHelper.getWritableDatabase();
	rowID = db.insert("wikinotes", "body", values);
	if (rowID > 0) {
	    Uri newUri = Uri.withAppendedPath(WikiNote.Notes.ALL_NOTES_URI,
					      uri.getLastPathSegment());
	    getContext().getContentResolver().notifyChange(newUri, null);
	    return newUri;
	}

	throw new SQLException("Failed to insert row into " + uri);
    }

    /**
     * Delete rows matching the where clause and args for the supplied URL.
     * The URL can be either the all notes URL (in which case all notes
     * matching the where clause will be deleted), or the note name, in which
     * case it will delete the note with the exactly matching title. Any of
     * the other URLs will be rejected as invalid. For deleting notes by title
     * we use ReST style arguments from the URI and don't support where clause
     * and args.
     */
    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
	/* This is kind of dangerous: it deletes all records with a single
	   Intent. It might make sense to make this ContentProvider
	   non-public, since this is kind of over-powerful and the provider
	   isn't generally intended to be public.  But for now it's still
	   public. */ 
	if (WikiNote.Notes.ALL_NOTES_URI.equals(uri)) {
	    return dbHelper.getWritableDatabase().delete("wikinotes", null,
							 null);
	}

	int count;
	SQLiteDatabase db = dbHelper.getWritableDatabase();
	switch (URI_MATCHER.match(uri)) {
	case NOTES:
	    count = db.delete("wikinotes", where, whereArgs);
	    break;

	case NOTE_NAME:
	    if (!TextUtils.isEmpty(where) || (whereArgs != null)) {
		throw new UnsupportedOperationException(
							"Cannot update note using where clause");
	    }
	    String noteId = uri.getPathSegments().get(1);
	    count = db.delete("wikinotes", "_id=?", new String[] { noteId });
	    break;

	default:
	    throw new IllegalArgumentException("Unknown URL " + uri);
	}

	getContext().getContentResolver().notifyChange(uri, null);
	return count;
    }

    /**
     * The update method, which will allow either a mass update using a where
     * clause, or an update targeted to a specific note name (the latter will
     * be more common). Other matched URLs will be rejected as invalid. For
     * updating notes by title we use ReST style arguments from the URI and do
     * not support using the where clause or args.
     */
    @Override
    public int update(Uri uri, ContentValues values, String where,
		      String[] whereArgs) {
	int count;
	SQLiteDatabase db = dbHelper.getWritableDatabase();
	switch (URI_MATCHER.match(uri)) {
	case NOTES:
	    count = db.update("wikinotes", values, where, whereArgs);
	    break;

	case NOTE_NAME:
	    values.put(WikiNote.Notes.MODIFIED_DATE, System
		.currentTimeMillis());
	    count = db.update("wikinotes", values, where, whereArgs);
	    break;

	default:
	    throw new IllegalArgumentException("Unknown URL " + uri);
	}

	getContext().getContentResolver().notifyChange(uri, null);
	return count;
    }

    /*
     * This static block sets up the pattern matches for the various URIs that
     * this content provider can handle. It also sets up the default projection
     * block (which defines what columns are returned from the database for the
     * results of a query).
     */
    static {
	URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
	URI_MATCHER.addURI(WikiNote.WIKINOTES_AUTHORITY, "wikinotes", NOTES);
	URI_MATCHER.addURI(WikiNote.WIKINOTES_AUTHORITY, "wikinotes/*",
			   NOTE_NAME);
	URI_MATCHER.addURI(WikiNote.WIKINOTES_AUTHORITY, "wiki/search/*",
			   NOTE_SEARCH);

	NOTES_LIST_PROJECTION_MAP = new HashMap<String, String>();
	NOTES_LIST_PROJECTION_MAP.put(WikiNote.Notes._ID, "_id");
	NOTES_LIST_PROJECTION_MAP.put(WikiNote.Notes.TITLE, "title");
	NOTES_LIST_PROJECTION_MAP.put(WikiNote.Notes.BODY, "body");
	NOTES_LIST_PROJECTION_MAP.put(WikiNote.Notes.CREATED_DATE, "created");
	NOTES_LIST_PROJECTION_MAP.put(WikiNote.Notes.MODIFIED_DATE,
				      "modified");
    }
}
