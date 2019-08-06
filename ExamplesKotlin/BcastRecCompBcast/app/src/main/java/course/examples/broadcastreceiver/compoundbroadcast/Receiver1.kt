package course.examples.broadcastreceiver.compoundbroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast

class Receiver1 : BroadcastReceiver() {

    companion object {
        private const val TAG = "Receiver1"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "INTENT RECEIVED")
    }

}
