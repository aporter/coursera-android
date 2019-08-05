package course.examples.broadcastreceiver.singlebroadcastdynamicregistration

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View

class SingleBroadcastActivity : Activity() {

    companion object {

        private const val CUSTOM_INTENT =
            "course.examples.broadcastreceiver.singlebroadcastdynamicregistration.SHOW_TOAST"
    }

    private val intentFilter = IntentFilter(CUSTOM_INTENT)
    private val receiver = Receiver()

    private lateinit var mBroadcastMgr: LocalBroadcastManager

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBroadcastMgr = LocalBroadcastManager
            .getInstance(applicationContext)

        setContentView(R.layout.main)

    }

    // Called when Button is clicked
    fun onClick(v: View) {
        mBroadcastMgr.sendBroadcast(
            Intent(CUSTOM_INTENT).setFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION)
        )
    }

    override fun onStart() {
        super.onStart()
        mBroadcastMgr.registerReceiver(receiver, intentFilter)
    }

    override fun onStop() {
        mBroadcastMgr.unregisterReceiver(receiver)
        super.onStop()
    }
}