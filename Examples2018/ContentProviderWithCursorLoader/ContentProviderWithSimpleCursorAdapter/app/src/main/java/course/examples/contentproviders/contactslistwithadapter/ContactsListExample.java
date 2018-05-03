package course.examples.contentproviders.contactslistwithadapter;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;

public class ContactsListExample extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Contact data
		String columnsToExtract[] = new String[] { Contacts._ID,
				Contacts.DISPLAY_NAME, Contacts.PHOTO_THUMBNAIL_URI };

		// Get the ContentResolver
		ContentResolver contentResolver = getContentResolver();

		// filter contacts with empty names
		String whereClause = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
				+ Contacts.DISPLAY_NAME + " != '' ) AND (" + Contacts.STARRED
				+ "== 1))";

		// sort by increasing ID
		String sortOrder = Contacts._ID + " ASC";

		// query contacts ContentProvider
		Cursor cursor = contentResolver.query(Contacts.CONTENT_URI,
				columnsToExtract, whereClause, null, sortOrder);

		// pass cursor to custom list adapter
		setListAdapter(new ContactInfoListAdapter(this, R.layout.list_item,
				cursor, 0));
	}
}