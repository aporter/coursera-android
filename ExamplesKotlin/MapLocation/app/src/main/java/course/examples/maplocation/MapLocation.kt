package course.examples.maplocation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

// Several Activity lifecycle methods are instrumented to emit LogCat output
// so you can follow this class' lifecycle

class MapLocation : Activity() {

    companion object {
        const val TAG = "MapLocation"
    }

    // UI elements
    private lateinit var addrText: EditText
    private lateinit var button: Button


    override fun onCreate(savedInstanceState: Bundle?) {

        /*
        Required call through to Activity.onCreate()
        Restore any saved instance state, if necessary
        */
        super.onCreate(savedInstanceState)

        // Set content view
        setContentView(R.layout.main)

        // Initialize UI elements
        addrText = findViewById(R.id.location)
        button = findViewById(R.id.mapButton)

        // Link UI elements to actions in code
        button.setOnClickListener { processClick() }

    }

    // Called when user clicks the Show Map button

    private fun processClick() {
        try {

            // Process text for network transmission
            var address = addrText.text.toString()
            address = address.replace(' ', '+')

            // Create Intent object for starting Google Maps application
            val geoIntent = Intent(
                    Intent.ACTION_VIEW, Uri
                    .parse("geo:0,0?q=$address"))

            if (packageManager.resolveActivity(geoIntent, 0) != null) {
                // Use the Intent to start Google Maps application using Activity.startActivity()
                startActivity(geoIntent)
            }

        } catch (e: Exception) {
            // Log any error messages to LogCat using Log.e()
            Log.e(TAG, e.toString())
        }
    }


    override fun onStart() {
        super.onStart()
        Log.i(TAG, "The activity is visible and about to be started.")
    }


    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "The activity is visible and about to be restarted.")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "The activity is visible and has focus (it is now \"resumed\")")
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
