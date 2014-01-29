package course.examples.ContentProviders.ContactsList;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;

public class ContactsListExample extends ListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI, 
				new String[] {ContactsContract.Contacts.DISPLAY_NAME},
					null, null, null);

		List<String> contacts = new ArrayList<String>();
		if (c.moveToFirst()) {
			do {
				contacts.add(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
			} while (c.moveToNext());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, contacts);
		setListAdapter(adapter);
	}
}