package course.examples.contentproviders.stringcontentprovideruser;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import course.examples.contentproviders.stringcontentprovider.DataContract;

public class CustomContactProviderDemo extends ListActivity {

	
	@SuppressWarnings("unused")
	private static final String TAG = "CustomContactProviderDemo";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ContentResolver contentResolver = getContentResolver();

		ContentValues values = new ContentValues();

		// Insert first record
		values.put(DataContract.DATA, "Record1");
		Uri firstRecordUri = contentResolver.insert(DataContract.CONTENT_URI, values);

		values.clear();

		// Insert second record
		values.put(DataContract.DATA, "Record2");
		contentResolver.insert(DataContract.CONTENT_URI, values);

		values.clear();

		// Insert third record
		values.put(DataContract.DATA, "Record3");
		contentResolver.insert(DataContract.CONTENT_URI, values);

		// Delete first record
		contentResolver.delete(firstRecordUri, null, null);

		// Create and set cursor and list adapter
		Cursor c = contentResolver.query(DataContract.CONTENT_URI, null, null, null,
				null);

		setListAdapter(new SimpleCursorAdapter(this, R.layout.list_layout, c,
				DataContract.ALL_COLUMNS, new int[] { R.id.idString,
						R.id.data }, 0));

	}
}