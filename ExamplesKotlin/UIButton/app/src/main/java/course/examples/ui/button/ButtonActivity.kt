package course.examples.ui.button

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button

class ButtonActivity : Activity() {

    companion object {
        private var mCount: Int = 0
    }

    private lateinit var mButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Get a reference to the Press Me Button
        mButton = findViewById(R.id.button)

        // Reset Button Text if restarting
        savedInstanceState?.run {
            mButton.text = getString(R.string.got_pressed_string, mCount)
        }
    }

    // Set an OnClickListener on this Button
    // Called each time the user clicks the Button
    fun processClick(v: View) {

        // Maintain a count of user presses
        // Display count as text on the Button

        (v as Button).text = getString(R.string.got_pressed_string, ++mCount)
    }
}