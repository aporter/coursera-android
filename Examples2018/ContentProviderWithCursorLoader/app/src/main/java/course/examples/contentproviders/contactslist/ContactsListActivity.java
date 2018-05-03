package course.examples.contentproviders.contactslist;

import android.Manifest;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.widget.Toast;

public class ContactsListActivity extends ListActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private ContactInfoListAdapter mAdapter;

    private final String[] permissions = {"android.permission.READ_CONTACTS"};
    private final int mRequestCode = 200;

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
        // Create and set empty adapter
        mAdapter = new ContactInfoListAdapter(this, R.layout.list_item, null, 0);
        setListAdapter(mAdapter);

        // Initialize the loader
        getLoaderManager().initLoader(0, null, this);
    }

    // Contacts data items to extract
    private static final String[] CONTACTS_ROWS = new String[]{Contacts._ID,
            Contacts.DISPLAY_NAME, Contacts.PHOTO_THUMBNAIL_URI};

    // Called when a new Loader should be created
    // Returns a new CursorLoader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // String used to filter contacts with empty or missing names or are unstarred
        String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + Contacts.DISPLAY_NAME + " != '' ) AND (" + Contacts.STARRED
                + "== 1))";

        // String used for defining the sort order
        String sortOrder = Contacts._ID + " ASC";

        return new CursorLoader(this, Contacts.CONTENT_URI, CONTACTS_ROWS,
                select, null, sortOrder);
    }

    // Called when the Loader has finished loading its data
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // Swap the new cursor into the List adapter
        mAdapter.swapCursor(data);

    }

    // Called when the last Cursor provided to onLoadFinished()
    // is about to be closed

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // set List adapter's cursor to null
        mAdapter.swapCursor(null);
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
}