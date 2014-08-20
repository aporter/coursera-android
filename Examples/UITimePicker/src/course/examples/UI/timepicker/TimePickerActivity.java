package course.examples.UI.timepicker;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

//This application uses some deprecated methods. 
//See UITimePickerFragment for a  more modern version of this application

public class TimePickerActivity extends Activity {

	private TextView mTimeDisplay;
	private Button mPickTime;
	private int mHour;
	private int mMinute;

	static final int TIME_DIALOG_ID = 0;

	// The callback received when the user "sets" the time in the dialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Capture UI elements
		mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
		mPickTime = (Button) findViewById(R.id.pickTime);

		// Set an OnClickListener on the Change the Time Button
		mPickTime.setOnClickListener(new View.OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});

		// Get the current time
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		// Display the current date
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

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);
		}
		return null;
	}

}