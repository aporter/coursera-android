package course.examples.notification.toastwithcustomview

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

class NotificationToastActivity : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
    }

    fun onClick(v: View) {

        val toast = Toast(applicationContext)

        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_LONG

        val vg =  findViewById<View>(R.id.toast_layout_root)

        toast.view = layoutInflater.inflate(
            R.layout.custom_toast,
            findViewById<ViewGroup>(R.id.toast_layout_root)
        )

        toast.show()
    }
}