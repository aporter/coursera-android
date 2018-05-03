package course.examples.ui.datepicker;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragmentActivity extends Activity implements
        OnTimeSetListener {

    private static final String CURRENT_TIME_KEY = "CURRENT_TIME_KEY";
    @SuppressWarnings("unused")
    private String TAG = "TimePickerFragmentActivity";
    private TextView mTimeDisplay;

    // Prepends a "0" to 1-digit minutes
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Capture UI element
        mTimeDisplay = findViewById(R.id.timeDisplay);

        setupUI(savedInstanceState);
    }

    private void setupUI(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            mTimeDisplay.setText(R.string.no_time_selected_string);
        } else {
            mTimeDisplay.setText(savedInstanceState.getCharSequence(CURRENT_TIME_KEY));
        }
    }

    // OnClickListener for the "Change the Time" Button
    public void buttonPressedCallback(@SuppressWarnings("unused") View view) {

        // Create and display DatePickerFragment
        new TimePickerFragment().show(getFragmentManager(), "TimePicker");
    }

    // Callback called by TimePickerFragment when user sets the time
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTimeDisplay.setText(new StringBuilder().
                append(pad(hourOfDay)).
                append(":").
                append(pad(minute)));
    }

    // Save instance state
    @Override
    public void onSaveInstanceState(Bundle bundle) {

        // TextView Time String
        bundle.putCharSequence(CURRENT_TIME_KEY, mTimeDisplay.getText());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(bundle);
    }

    public static class TimePickerFragment extends DialogFragment implements
            OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hourOfDay, minute,
                    false);
        }

        // Callback called by TimePickerDialog when user sets the time
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            ((OnTimeSetListener) getActivity()).onTimeSet(view, hourOfDay,
                    minute);
        }
    }
}
