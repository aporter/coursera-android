package course.examples.broadcastreceiver.singlebroadcaststaticregistration

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View

class SimpleBroadcastActivity : Activity() {


    companion object {

        private const val CUSTOM_INTENT =
            "course.examples.broadcastreceiver.singlebroadcaststaticregistration.SHOW_TOAST"
        private const val TAG = "SimpleBroadcastActivity"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
    }

    @SuppressWarnings("UnusedParameters")
    fun onClick(view: View) {
        Log.i(TAG, "Broadcast sent")

        val intent = Intent(CUSTOM_INTENT)
        intent.setPackage("course.examples.broadcastreceiver.singlebroadcaststaticregistration")
        sendBroadcast(intent, Manifest.permission.VIBRATE)
    }
}