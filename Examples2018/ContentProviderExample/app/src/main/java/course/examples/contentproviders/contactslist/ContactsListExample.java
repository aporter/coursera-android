package course.examples.contentproviders.contactslist;

import android.Manifest;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactsListExample extends ListActivity {

    private final String[] permissions = {"android.permission.READ_CONTACTS"};
    private final int mRequestCode = 200;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, mRequestCode);
        } else {
            displayContact();
        }
    }

    private void displayContact() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                null, null, null);

        List<String> contacts = new ArrayList<>();
        if (null != cursor && cursor.moveToFirst()) {
            do {
                contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, contacts);
        setListAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mRequestCode == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayContact();
            } else {
                Toast.makeText(this, R.string.need_perms_string, Toast.LENGTH_LONG).show();
            }

        }
    }


}