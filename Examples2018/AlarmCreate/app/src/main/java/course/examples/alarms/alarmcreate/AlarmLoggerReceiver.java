package course.examples.alarms.alarmcreate;

import java.text.DateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmLoggerReceiver extends BroadcastReceiver {

	private static final String TAG = "AlarmLoggerReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {

		// Log receipt of the Intent with timestamp
		Log.i(TAG,context.getString(R.string.logging_at_string) + DateFormat.getDateTimeInstance().format(new Date()));

	}
}
