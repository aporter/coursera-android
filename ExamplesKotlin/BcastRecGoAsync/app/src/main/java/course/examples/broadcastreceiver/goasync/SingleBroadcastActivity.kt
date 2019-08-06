package course.examples.broadcastreceiver.goasync

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View

class SingleBroadcastActivity : Activity() {

    companion object {

        private const val CUSTOM_INTENT = "course.examples.broadcastreceiver.goasync.SHOW_TOAST"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

    }

    // Called when Button is clicked
    fun onClick(v: View) {
        sendBroadcast(Intent(CUSTOM_INTENT).setPackage("course.examples.broadcastreceiver.goasync"))
    }
}