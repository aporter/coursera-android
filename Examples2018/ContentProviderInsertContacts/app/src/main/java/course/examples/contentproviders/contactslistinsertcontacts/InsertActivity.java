package course.examples.contentproviders.contactslistinsertcontacts;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class InsertActivity extends Activity {

    private final String[] permissions = {"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.GET_ACCOUNTS"};
    private final int mRequestCode = 200;
    private Button mInsertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Exit if there are no google accounts
        if (AccountManager.get(this).getAccountsByType("com.google").length == 0)
            finish();

        setContentView(R.layout.main);
        mInsertButton = findViewById(R.id.insert);

        if (!checkPermission()) {
            requestPermissions(permissions, mRequestCode);
        } else {
            onPermissionsGranted();
        }
    }

    private void onPermissionsGranted() {
        mInsertButton.setEnabled(true);
    }

    private boolean checkPermission() {
        return (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED);
    }

    public void onClick(@SuppressWarnings("unused") View v) {
        // Start the DisplayActivity
        startActivity(new Intent(InsertActivity.this,
                DisplayActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mRequestCode == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                onPermissionsGranted();
            } else {
                Toast.makeText(this, R.string.need_perms_string, Toast.LENGTH_LONG).show();
            }
        }
    }
}
