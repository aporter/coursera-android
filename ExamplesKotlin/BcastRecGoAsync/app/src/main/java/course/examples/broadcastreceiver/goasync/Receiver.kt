package course.examples.broadcastreceiver.goasync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Receiver : BroadcastReceiver() {

    companion object {

        private const val TAG = "Receiver"
    }

    override fun onReceive(context: Context, intent: Intent) {

        Log.i(TAG, "Broadcast Received")

        val pendingResult = goAsync()

        GlobalScope.launch(context = Dispatchers.Main) {
            delay(7000)
            Toast.makeText(context, "Broadcast Received by Receiver", Toast.LENGTH_LONG).show()
            pendingResult.finish()
        }
    }

}
