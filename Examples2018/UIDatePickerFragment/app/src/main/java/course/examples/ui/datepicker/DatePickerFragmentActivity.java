package course.examples.ui.datepicker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class DatePickerFragmentActivity extends Activity implements OnDateSetListener {

    private static final String CURRENT_DATE_KEY = "CURRENT_DATE_KEY";
    private TextView mDateDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Capture UI element
        mDateDisplay = findViewById(R.id.dateDisplay);

        setupUI(savedInstanceState);

    }

    private void setupUI(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            mDateDisplay.setText(R.string.no_date_selected_string);
        } else {
            mDateDisplay.setText(savedInstanceState.getCharSequence(CURRENT_DATE_KEY));
        }
    }

    // OnClickListener for the "Change the Date" Button
    public void buttonPressedCallback(@SuppressWarnings("unused") View v) {

        // Create and display a new DatePickerFragment
        new DatePickerFragment().show(getFragmentManager(), "DatePicker");
    }

    // Callback called by DatePickerFragment when user sets the time
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        mDateDisplay.setText(new StringBuilder()
                // Month is 0-based so add 1
                .append(monthOfYear + 1).append("-").append(dayOfMonth).append("-")
                .append(year).append(" "));
    }

    // Save instance state
    @Override
    public void onSaveInstanceState(Bundle bundle) {

        // CheckBox (checkedState, visibility)
        bundle.putCharSequence(CURRENT_DATE_KEY, mDateDisplay.getText());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(bundle);
    }

    public static class DatePickerFragment extends DialogFragment implements
            OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Set the current date in the DatePickerFragment
            final Calendar cal = Calendar.getInstance();

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH));
        }


        // Callback called by DatePickerDialog when user sets the time
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            ((OnDateSetListener) getActivity()).onDateSet(view, year,
                    monthOfYear, dayOfMonth);
        }
    }
}