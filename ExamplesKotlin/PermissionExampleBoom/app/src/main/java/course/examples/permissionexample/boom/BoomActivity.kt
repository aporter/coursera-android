package course.examples.permissionexample.boom

import android.app.Activity
import android.os.Bundle

class BoomActivity : Activity() {

    companion object {

        // String used to represent the dangerous operation
        private const val ACTION_BOOM = "course.examples.permissionexample.boom.boom_action"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check to see which Intent was use to start app
        val actionString = intent.action
        if (null != actionString && actionString == ACTION_BOOM) {
            setContentView(R.layout.boom_layout)
        }
    }
}
