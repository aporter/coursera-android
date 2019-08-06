package course.examples.broadcastreceiver.resultreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class Receiver1 : BroadcastReceiver() {

    companion object {
        private const val TAG = "Receiver1"
    }

    override fun onReceive(context: Context, intent: Intent) {

        Log.i(TAG, "INTENT RECEIVED by Receiver1")

        val tmp = if (resultData == null) "" else resultData
        resultData = "$tmp:Receiver 1:"
    }

}
