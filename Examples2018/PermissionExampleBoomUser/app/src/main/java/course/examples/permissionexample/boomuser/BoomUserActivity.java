package course.examples.permissionexample.boomuser;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class BoomUserActivity extends Activity {

    // String used to represent the dangerous operation
    private static final String ACTION_BOOM = "course.examples.permissionexample.boom.boom_action";
    private static final String BOOM_PERM = "course.examples.permissionexample.BOOM_PERM";
    private static final int BOOM_PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boom_user_layout);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {

            // Called when the user clicks on the Detonate Button
            @Override
            public void onClick(View v) {
                tryBoom();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void tryBoom() {
        // Step 1: Ensure permissions
        if (needsRuntimePermission(BOOM_PERM)) {
            requestPermissions(new String[]{BOOM_PERM}, BOOM_PERMISSION_REQUEST);
        } else {
            startBoomApp();
        }
    }

    private boolean needsRuntimePermission(String permission) {
        // Check the SDK version and whether the permission is already granted.
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == BOOM_PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                startBoomApp();
            } else {
                Toast.makeText(this, getString(R.string.need_permission_string), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startBoomApp() {

        // Step 2: Start Boom app
        // Create an implicit Intent using the Action String ACTION_BOOM
        // Launch an Activity that can receive the Intent using Activity.startActivity()
        startActivity(new Intent(ACTION_BOOM));

    }

}
