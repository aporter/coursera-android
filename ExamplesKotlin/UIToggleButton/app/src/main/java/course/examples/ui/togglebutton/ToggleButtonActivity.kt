package course.examples.ui.togglebutton

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.ToggleButton

class ToggleButtonActivity : Activity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Set an setOnCheckedChangeListener on the ToggleButton
        setListener(
            findViewById<ToggleButton>(R.id.togglebutton),
            findViewById<FrameLayout>(R.id.top_frame)
        )

        // Set an OnCheckedChangeListener on the Switch
        setListener(
            findViewById<Switch>(R.id.switcher),
            findViewById<FrameLayout>(R.id.bottom_frame)
        )

    }

    private fun setListener(button: CompoundButton, background: View) {

        button.setOnCheckedChangeListener { _, isChecked ->
            // Toggle the Background color between a light and dark color
            if (isChecked) {
                background.setBackgroundColor(resources.getColor(R.color.primary_light, null))
            } else {
                background.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}