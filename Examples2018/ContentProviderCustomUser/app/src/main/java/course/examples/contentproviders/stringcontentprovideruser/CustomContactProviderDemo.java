package course.examples.contentproviders.stringcontentprovideruser;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.Random;

import course.examples.contentproviders.stringcontentprovider.DataContract;

public class CustomContactProviderDemo extends ListActivity {


    @SuppressWarnings("unused")
    private static final String TAG = "CustomContactProviderDemo";
    private final String[] permissions = {"course.examples.contentproviders.stringcontentprovider.PERMISSION"};
    private final int mRequestCode = 200;
    private Cursor mCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, mRequestCode);
        } else {
            useContentProvider();
        }
    }

    private void useContentProvider() {
        Random random = new Random();

        ContentResolver contentResolver = getContentResolver();

        ContentValues values = new ContentValues();

        // Insert first record
        values.put(DataContract.DATA, "Record_" + random.nextInt(Integer.MAX_VALUE));
        Uri firstRecordUri = contentResolver.insert(DataContract.CONTENT_URI, values);

        values.clear();

        // Insert second record
        values.put(DataContract.DATA, "Record_" + random.nextInt(Integer.MAX_VALUE));
        contentResolver.insert(DataContract.CONTENT_URI, values);

        values.clear();

        // Insert third record
        values.put(DataContract.DATA, "Record_" + random.nextInt(Integer.MAX_VALUE));
        contentResolver.insert(DataContract.CONTENT_URI, values);

        // Delete first record
        if (null != firstRecordUri) {
            contentResolver.delete(firstRecordUri, null, null);
        }

        // Create and set mCursor and list adapter
        mCursor = contentResolver.query(DataContract.CONTENT_URI, null, null, null,
                null);

        setListAdapter(new SimpleCursorAdapter(this, R.layout.list_layout, mCursor,
                DataContract.ALL_COLUMNS, new int[]{R.id.idString,
                R.id.data}, 0));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (mRequestCode == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                useContentProvider();
            } else {
                Toast.makeText(this, R.string.need_perms_string, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mCursor) mCursor.close();
    }
}