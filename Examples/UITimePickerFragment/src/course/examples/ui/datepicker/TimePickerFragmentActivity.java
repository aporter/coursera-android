package course.examples.ui.datepicker;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragmentActivity extends Activity implements
		OnTimeSetListener {

	private TextView mTimeDisplay;
	private Button mPickTime;
	private int mHour;
	private int mMinute;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Capture UI elements
		mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
		mPickTime = (Button) findViewById(R.id.pickTime);

		// Set an OnClickListener for the Change the Time Button
		mPickTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// Create a new DatePickerFragment
				DialogFragment newFragment = new TimePickerFragment();

				// Display DatePickerFragment
				newFragment.show(getFragmentManager(), "TimePicker");
			}
		});
	}

	// Callback called when user sets the time
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		mHour = hourOfDay;
		mMinute = minute;
		updateDisplay();
	}

	// Update the time String in the TextView
	private void updateDisplay() {
		mTimeDisplay.setText(new StringBuilder().append(pad(mHour)).append(":")
				.append(pad(mMinute)));
	}

	// Prepends a "0" to 1-digit minutes
	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
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

		// Callback to TimePickerFragmentActivity.onTimeSet() to update the UI
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			((OnTimeSetListener) getActivity()).onTimeSet(view, hourOfDay,
					minute);

		}
	}
}
