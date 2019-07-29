package course.examples.permissionexample.boomuser

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast

class BoomUserActivity : Activity() {


    companion object {

        // String used to represent the dangerous operation
        private const val ACTION_BOOM = "course.examples.permissionexample.boom.boom_action"

        // String used to represent the permission to invoke the dangerous operation
        private const val BOOM_PERM = "course.examples.permissionexample.BOOM_PERM"

        private const val BOOM_PERMISSION_REQUEST = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.boom_user_layout)

    }

    fun tryBoom(v: View) {
        // Step 1: Ensure permissions
        if (needsRuntimePermission()) {
            requestPermissions(arrayOf(BOOM_PERM), BOOM_PERMISSION_REQUEST)
        } else {
            startBoomApp()
        }
    }

    private fun needsRuntimePermission(): Boolean {
        // Check the SDK version and whether the permission is already granted.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
            BOOM_PERM
        ) != PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == BOOM_PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                startBoomApp()
            } else {
                Toast.makeText(this, getString(R.string.need_permission_string), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun startBoomApp() {

        // Step 2: Start Boom app
        // Create an implicit Intent using the Action String ACTION_BOOM
        // Launch an Activity that can receive the Intent using Activity.startActivity()
        startActivity(Intent(ACTION_BOOM))

    }
}
