package course.examples.ContentProviders.StringContentProvider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.SparseArray;

// Note: Currently, this data does not persist across device reboot
public class StringsContentProvider extends ContentProvider {

	// Data storage 
	private static final SparseArray<DataRecord> db = new SparseArray<DataRecord>();

	@SuppressWarnings("unused")
	private static final String TAG = "StringsContentProvider";

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
				cursor.addRow(new Object[] { dataRecord.getID(),
						dataRecord.getData() });

			}
		} else if (isItemUri(uri)){
	
			// Add single row to cursor
			Integer requestId = Integer.parseInt(uri.getLastPathSegment());

			if (null != db.get(requestId)) {

				DataRecord dr = db.get(requestId);
				cursor.addRow(new Object[] { dr.getID(), dr.getData() });

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

	// Initialize ContentProvider 
	// Nothing to do in this case
	@Override
	public boolean onCreate() {
		return true;
	}

	// Does last segment of the Uri match a string of digits?
	private boolean isItemUri(Uri uri) {
		return uri.getLastPathSegment().matches("\\d+");
	}

	// Is the last segment of the Uri the name of the data table?
	private boolean isTableUri(Uri uri) {
		return uri.getLastPathSegment().equals(DataContract.DATA_TABLE);
	}

}
