package course.examples.contentproviders.stringcontentprovider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.SparseArray;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Iterator;

public class StringsContentProvider extends ContentProvider {

    // Internal Data storage
    private static final SparseArray<DataRecord> db = new SparseArray<>();

    @SuppressWarnings("unused")
    private static final String TAG = "StringsContentProvider";
    private static final String DB_FILE_NAME = "db.txt";

    // Delete some or all data items
    @Override
    public synchronized int delete(Uri uri, String selection,
                                   String[] selectionArgs) {

        int numRecordsRemoved = 0;

        // If last segment is the table name, delete all data items
        if (isTableUri(uri)) {
            numRecordsRemoved = db.size();
            db.clear();

            // If last segment is the digit, delete data item with that ID
        } else if (isItemUri(uri)) {

            Integer requestId = Integer.parseInt(uri.getLastPathSegment());
            if (null != db.get(requestId)) {
                db.remove(requestId);
                numRecordsRemoved++;
            }
        }

        // Write out database modifications/updates to file
        persistDb();

        //return number of items deleted
        return numRecordsRemoved;
    }

    // Return MIME type for given uri
    @Override
    public synchronized String getType(Uri uri) {

        String contentType = DataContract.CONTENT_ITEM_TYPE;
        if (isTableUri(uri)) {
            contentType = DataContract.CONTENT_DIR_TYPE;
        }
        return contentType;
    }

    // Insert specified value into ContentProvider
    @Override
    public synchronized Uri insert(Uri uri, ContentValues value) {

        if (value.containsKey(DataContract.DATA)) {
            DataRecord dataRecord = new DataRecord(value.getAsString(DataContract.DATA));
            db.put(dataRecord.getID(), dataRecord);

            // Write database modifications/updates to file
            persistDb();

            // return Uri associated with newly-added data item
            return Uri.withAppendedPath(DataContract.CONTENT_URI,
                    String.valueOf(dataRecord.getID()));

        }

        return null;
    }


    // return all or some rows from ContentProvider based on specified Uri
    // all other parameters are ignored

    @Override
    public synchronized Cursor query(Uri uri, String[] projection,
                                     String selection, String[] selectionArgs, String sortOrder) {

        // Create simple cursor
        MatrixCursor cursor = new MatrixCursor(DataContract.ALL_COLUMNS);

        if (isTableUri(uri)) {

            // Add all rows to cursor
            for (int idx = 0; idx < db.size(); idx++) {

                DataRecord dataRecord = db.get(db.keyAt(idx));
                cursor.addRow(new Object[]{dataRecord.getID(),
                        dataRecord.getData()});

            }
        } else if (isItemUri(uri)) {

            // Add single row to cursor
            Integer requestId = Integer.parseInt(uri.getLastPathSegment());

            if (null != db.get(requestId)) {

                DataRecord dr = db.get(requestId);
                cursor.addRow(new Object[]{dr.getID(), dr.getData()});

            }
        }
        return cursor;
    }

    // Ignore request
    @Override
    public synchronized int update(Uri uri, ContentValues values,
                                   String selection, String[] selectionArgs) {
        return 0;
    }

    // Does last segment of the Uri match a string of digits?
    private boolean isItemUri(Uri uri) {
        return uri.getLastPathSegment().matches("\\d+");
    }

    // Is the last segment of the Uri the name of the data table?
    private boolean isTableUri(Uri uri) {
        return uri.getLastPathSegment().equals(DataContract.DATA_TABLE);
    }

    // Initialize ContentProvider. Read data from file
    @Override
    public synchronized boolean onCreate() {
        readDb();
        return true;
    }

    // Write data to SharedPreferences in JSON format
    // Should consider better format as JSON keys should be String type
    private void persistDb() {

        StringBuilder sb = new StringBuilder("{");

        for (int idx = 0; idx < db.size(); idx++) {
            DataRecord tmp = db.get(db.keyAt(idx));
            if (null != tmp) {
                sb.append(tmp.toString()).append(",");
            }
        }

        sb.replace(sb.length() - 1, sb.length(), "}");

        writeDb(sb.toString());
    }

    @SuppressLint("ApplySharedPref")
    private void writeDb(String data) {

        @SuppressWarnings("ConstantConditions")
        SharedPreferences sharedPref = getContext().getSharedPreferences(DB_FILE_NAME, Context.MODE_PRIVATE);

        if (null == sharedPref) return;

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("DBString", data);
        editor.commit();

    }

    private void readDb() {
        @SuppressWarnings("ConstantConditions")
        SharedPreferences sharedPref = getContext().getSharedPreferences(DB_FILE_NAME, Context.MODE_PRIVATE);

        if (null == sharedPref) return;

        String dbString = sharedPref.getString("DBString", null);
        if (null != dbString) {
            db.clear();
            JSONTokener parser = new JSONTokener(dbString);
            while (parser.more()) {
                try {
                    JSONObject tmp = (JSONObject) parser.nextValue();
                    Iterator<String> iterator = tmp.keys();
                    while (iterator.hasNext()) {
                        String value = iterator.next();
                        int id = tmp.getInt(value);

                        db.put(id, new DataRecord(id, value));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
