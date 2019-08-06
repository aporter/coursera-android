package course.examples.broadcastreceiver.compoundorderedbroadcast

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View

class CompoundOrderedBroadcast : Activity() {

    companion object {

        private const val CUSTOM_INTENT = "course.examples.BroadcastReceiver.compoundorderedbroadcast.SHOW_TOAST"
    }

    private val mReceiver = Receiver1()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

    }

    fun onClick(v: View) {
        sendOrderedBroadcast(
            Intent(CUSTOM_INTENT).setPackage(packageName).setFlags(
                Intent.FLAG_DEBUG_LOG_RESOLUTION
            ),
            android.Manifest.permission.VIBRATE
        )
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(CUSTOM_INTENT)
        intentFilter.priority = 3
        registerReceiver(mReceiver, intentFilter)
    }

    override fun onStop() {
        unregisterReceiver(mReceiver)
        super.onStop()
    }
}