package course.examples.ui.datepicker

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
import java.util.*

class TimePickerFragmentActivity : FragmentActivity(), OnTimeSetListener {

    companion object {

        private const val KEY_CURRENT_TIME = "CURRENT_TIME_KEY"

        @Suppress("unused")
        private const val TAG = "TimePickerFragmentActivity"

        // Prepends a "0" to 1-digit minute values
        private fun pad(c: Int): String {
            return if (c >= 10)
                c.toString()
            else
                "0$c"
        }
    }

    private lateinit var mTimeDisplay: TextView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Capture UI element
        mTimeDisplay = findViewById(R.id.timeDisplay)

        setupUI(savedInstanceState)
    }

    private fun setupUI(savedInstanceState: Bundle?) {
        if (null == savedInstanceState) {
            mTimeDisplay.setText(R.string.no_time_selected_string)
        } else {
            mTimeDisplay.text = savedInstanceState.getCharSequence(KEY_CURRENT_TIME)
        }
    }

    // OnClickListener for the "Change the Time" Button
    fun buttonPressedCallback(view: View) {

        // Create and display DatePickerFragment
        TimePickerFragment().show(supportFragmentManager, "TimePicker")
    }

    // Callback called by TimePickerFragment when user sets the time
    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        mTimeDisplay.text = StringBuilder().append(pad(hourOfDay)).append(":").append(pad(minute))
    }

    // Save instance state
    public override fun onSaveInstanceState(bundle: Bundle) {

        // TextView Time String
        bundle.putCharSequence(KEY_CURRENT_TIME, mTimeDisplay.text)

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(bundle)
    }


    // Time Picking Fragment
    class TimePickerFragment : DialogFragment(), OnTimeSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val c = Calendar.getInstance()
            val hourOfDay = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // Create a new instance of TimePickerDialog and return it
            return TimePickerDialog(activity, this, hourOfDay, minute,false)
        }

        // Callback called by TimePickerDialog when user sets the time
        override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
            (activity as OnTimeSetListener).onTimeSet(view, hourOfDay, minute)
        }
    }
}
