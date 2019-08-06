package course.examples.notification.toast

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class NotificationToastActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

    }

    fun onClick(v: View) {
        Toast.makeText(
            applicationContext,
            getString(R.string.youre_toast_string),
            Toast.LENGTH_LONG
        ).show()
    }
}