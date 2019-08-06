package course.examples.broadcastreceiver.resultreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class Receiver2 : BroadcastReceiver() {

    companion object {
        private const val TAG = "Receiver2"
    }

    override fun onReceive(context: Context, intent: Intent) {

        Log.i(TAG, "INTENT RECEIVED by Receiver2")

        val tmp = if (resultData == null) "" else resultData
        resultData = "$tmp:Receiver 2:"
    }

}
