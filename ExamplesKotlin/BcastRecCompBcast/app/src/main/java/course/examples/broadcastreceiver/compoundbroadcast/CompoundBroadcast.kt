package course.examples.broadcastreceiver.compoundbroadcast

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View

class CompoundBroadcast : Activity() {


    companion object {

        private const val CUSTOM_INTENT = "course.examples.broadcastreceiver.compoundbroadcast.SHOW_TOAST"
        private val mIntentFilter = IntentFilter(CUSTOM_INTENT)
    }

    private val mReceiver1 = Receiver1()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
    }

    @Suppress("Unused Parameter")
    fun onClick(v: View) {
        val intent =
            Intent(CUSTOM_INTENT).setPackage(packageName)
                .setFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION)
        sendBroadcast(intent, Manifest.permission.VIBRATE)
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(mReceiver1, mIntentFilter)
    }

    override fun onStop() {
        unregisterReceiver(mReceiver1)
        super.onStop()
    }
}