/*
 * Must have Google Maps application installed
 */

package course.examples.maplocationfromcontacts

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.Toast

//Several Activity lifecycle methods are instrumented to emit LogCat output
//so you can follow this class' lifecycle

class MapLocationFromContactsActivity : Activity() {


    companion object {

        // These variables are shorthand aliases for data items in Contacts-related database tables
        private const val CONTENT_ITEM_TYPE = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_TYPE
        private const val FORMATTED_ADDRESS = ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS

        // These variables are used for interaction with Contacts app
        private const val PICK_CONTACT_REQUEST = 0
        private const val PERMISSIONS_PICK_CONTACT_REQUEST = 1
        private const val READ_CONTACTS_PERM = Manifest.permission.READ_CONTACTS

        private const val TAG = "MapLocationFromContacts"
        private var hasPermission: Boolean = false
    }

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        button = findViewById(R.id.mapButton)

        // Called when user clicks the Show Map button
        button.setOnClickListener { getContact() }

    }

    private fun needsRuntimePermission(): Boolean {
        // Check the SDK version and whether the permission is already granted.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(READ_CONTACTS_PERM) != PackageManager.PERMISSION_GRANTED
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun getContact() {
        // Step 1: Ensure permissions
        if (!hasPermission && needsRuntimePermission()) {
            requestPermissions(arrayOf(READ_CONTACTS_PERM), PERMISSIONS_PICK_CONTACT_REQUEST)
        } else {
            startContactsApp()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == PERMISSIONS_PICK_CONTACT_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                hasPermission = true
                startContactsApp()
            } else {
                Toast.makeText(this, "This app requires access to your contact list", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Step 2: Start Contacts
    private fun startContactsApp() {

        // Create Intent object for picking data from Contacts database
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = CONTENT_ITEM_TYPE

        if (packageManager.resolveActivity(intent, 0) != null) {
            // Use intent to start Contacts application
            // Variable PICK_CONTACT_REQUEST identifies this operation
            startActivityForResult(intent, PICK_CONTACT_REQUEST)

        }
    }

    // Callback invoked after picking contact
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        // Ensure that this call is the result of a successful PICK_CONTACT_REQUEST request
        if (resultCode == RESULT_OK && requestCode == PICK_CONTACT_REQUEST) {

            // These details are covered in the lesson on ContentProviders

            val contactUri = data.data ?: return
            val projection = arrayOf(FORMATTED_ADDRESS)
            val cr = contentResolver
            val cursor = cr.query(contactUri, projection, null, null, null)

            if (null != cursor && cursor.moveToFirst()) {
                val numberIndex = cursor.getColumnIndex(FORMATTED_ADDRESS)
                var formattedAddress: String? = cursor.getString(numberIndex)

                if (null != formattedAddress) {

                    // Process text for network transmission
                    formattedAddress = formattedAddress.replace(' ', '+')

                    // Create Intent object for starting Google Maps application
                    val geoIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("geo:0,0?q=$formattedAddress"))

                    // Use the Intent to start Google Maps application using Activity.startActivity()
                    startActivity(geoIntent)
                }
            }

            cursor?.close()
        }
    }

    override fun onRestart() {
        Log.i(TAG, "The activity is about to be restarted.")
        super.onRestart()
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "The activity is about to become visible.")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "The activity has become visible (it is now \"resumed\")")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG,
                "Another activity is taking focus (this activity is about to be \"paused\")")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "The activity is no longer visible (it is now \"stopped\")")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "The activity is about to be destroyed.")
    }
}
