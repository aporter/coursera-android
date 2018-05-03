package course.examples.contentproviders.contactslistwithadapter;

import android.Manifest;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.widget.Toast;

public class ContactsListActivity extends ListActivity {

	private final String[] permissions = {"android.permission.READ_CONTACTS"};
	private final int mRequestCode = 200;
    private Cursor mCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(permissions, mRequestCode);
        } else {
            loadContacts();
        }
	}

    private void loadContacts() {

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
        mCursor = contentResolver.query(Contacts.CONTENT_URI,
                columnsToExtract, whereClause, null, sortOrder);

        // pass mCursor to custom list adapter
        setListAdapter(new ContactInfoListAdapter(this, R.layout.list_item,
                mCursor, 0));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mRequestCode == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts();
            }
        } else {
            Toast.makeText(this, R.string.need_perms_string, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCursor.close();
    }
}