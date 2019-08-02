package course.examples.ui.datepicker

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import java.util.*

class DatePickerFragmentActivity : FragmentActivity(), OnDateSetListener {

    companion object {

        private const val CURRENT_DATE_KEY = "CURRENT_DATE_KEY"
    }

    private lateinit var mDateDisplay: TextView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Capture UI element
        mDateDisplay = findViewById(R.id.dateDisplay)

        setupUI(savedInstanceState)

    }

    private fun setupUI(savedInstanceState: Bundle?) {
        if (null == savedInstanceState) {
            mDateDisplay.setText(R.string.no_date_selected_string)
        } else {
            mDateDisplay.text = savedInstanceState.getCharSequence(CURRENT_DATE_KEY)
        }
    }

    // OnClickListener for the "Change the Date" Button
    fun buttonPressedCallback(v: View) {

        // Create and display a new DatePickerFragment
        DatePickerFragment().show(supportFragmentManager, "DatePicker")
    }

    // Callback called by DatePickerFragment when user sets the time
    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        // Month is 0-based so add 1
        mDateDisplay.text = StringBuilder()
            .append(monthOfYear + 1).append("-").append(dayOfMonth).append("-")
            .append(year).append(" ")
    }

    // Save instance state
    public override fun onSaveInstanceState(bundle: Bundle) {

        // CheckBox (checkedState, visibility)
        bundle.putCharSequence(CURRENT_DATE_KEY, mDateDisplay.text)

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(bundle)
    }


    // Date picking Fragment
    class DatePickerFragment : android.support.v4.app.DialogFragment(), OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            // Set the current date in the DatePickerFragment
            val cal = Calendar.getInstance()

            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(
                activity as Context, this,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
        }

        // Callback called by DatePickerDialog when user sets the time
        override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {

            (activity as OnDateSetListener).onDateSet(view, year, monthOfYear, dayOfMonth)
        }
    }
}