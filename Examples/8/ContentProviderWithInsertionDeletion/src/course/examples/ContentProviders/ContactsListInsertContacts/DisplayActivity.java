package course.examples.ContentProviders.ContactsListInsertContacts;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentProviderOperation;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import course.examples.ContentProviders.ContactsListWithInsDel.R;

public class DisplayActivity extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	public final static String[] mNames = new String[] { "Android Painter",
			"Steve Ballmer", "Steve Jobs", "Larry Page" };

	private static final String columnsToExtract[] = new String[] {
			Contacts._ID, Contacts.DISPLAY_NAME, Contacts.STARRED };
	private static final String columnsToDisplay[] = new String[] { Contacts.DISPLAY_NAME };
	private static final int[] resourceIds = new int[] { R.id.name };
	private static final String TAG = "ContactsListDisplayActivity";

	private Account[] mAccountList;
	private String mType;
	private String mName;
	private SimpleCursorAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get Account information
		mAccountList = AccountManager.get(this).getAccountsByType("com.google");
		mType = mAccountList[0].type;
		mName = mAccountList[0].name;

		// Insert new contacts
		insertAllNewContacts();

		// Create and set empty list adapter
		mAdapter = new SimpleCursorAdapter(this, R.layout.list_layout, null,
				columnsToDisplay, resourceIds, 0);
		setListAdapter(mAdapter);

		// Initialize a CursorLoader
		getLoaderManager().initLoader(0, null, this);

	}

	// Insert all new contacts into Contacts ContentProvider
	private void insertAllNewContacts() {

		// Set up a batch operation on Contacts ContentProvider
		ArrayList<ContentProviderOperation> batchOperation = new ArrayList<ContentProviderOperation>();

		for (String name : mNames) {
			addRecordToBatchInsertOperation(name, batchOperation);
		}

		try {

			// Apply all batched operations
			getContentResolver().applyBatch(ContactsContract.AUTHORITY,
					batchOperation);

		} catch (RemoteException e) {
			Log.i(TAG, "RemoteException");
		} catch (OperationApplicationException e) {
			Log.i(TAG, "RemoteException");
		}

	}

	// Insert named contact into Contacts ContentProvider
	private void addRecordToBatchInsertOperation(String name,
			List<ContentProviderOperation> ops) {

		int position = ops.size();

		// First part of operation
		ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
				.withValue(RawContacts.ACCOUNT_TYPE, mType)
				.withValue(RawContacts.ACCOUNT_NAME, mName)
				.withValue(Contacts.STARRED, 1).build());

		// Second part of operation
		ops.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, position)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, name).build());

	}

	// Remove all newly-added contacts when activity is destroyed
	@Override
	protected void onDestroy() {

		deleteAllNewContacts();

		super.onDestroy();

	}

	private void deleteAllNewContacts() {

		for (String name : mNames) {

			deleteContact(name);

		}
	}

	private void deleteContact(String name) {

		getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI,
				ContactsContract.Contacts.DISPLAY_NAME + "=?",
				new String[] { name });

	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
				+ Contacts.DISPLAY_NAME + " != '' ) AND (" + Contacts.STARRED
				+ "== 1))";

		return new CursorLoader(this, Contacts.CONTENT_URI, columnsToExtract,
				select, null, Contacts._ID + " ASC");
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		mAdapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> loader) {

		mAdapter.swapCursor(null);

	}

}
