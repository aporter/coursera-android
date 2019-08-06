package course.examples.broadcastreceiver.resultreceiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast

class CompoundOrderedBroadcastWithResultReceiver : Activity() {


    companion object {

        private const val CUSTOM_INTENT = "course.examples.broadcastreceiver.resultreceiver.SHOW_TOAST"
    }

    private val mReceiver1 = Receiver1()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

    }

    fun onClick(v: View) {
        sendOrderedBroadcast(Intent(CUSTOM_INTENT).setPackage(packageName),
            null,
            object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    Toast.makeText(
                        context,
                        "Final Result is $resultData",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            null,
            0,
            null,
            null
        )
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(CUSTOM_INTENT)
        intentFilter.priority = 3
        registerReceiver(mReceiver1, intentFilter)
    }

    override fun onStop() {
        unregisterReceiver(mReceiver1)
        super.onStop()
    }
}