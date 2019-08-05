package course.examples.broadcastreceiver.singlebroadcastdynamicregistration

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast

class Receiver : BroadcastReceiver() {

    companion object {

        private const val TAG = "Receiver"
    }

    override fun onReceive(context: Context, intent: Intent) {

        Log.i(TAG, "Broadcast Received")

        val vibrator = context
            .getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                500,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )

        Toast.makeText(context, "Broadcast Received by Receiver", Toast.LENGTH_LONG).show()

    }

}
