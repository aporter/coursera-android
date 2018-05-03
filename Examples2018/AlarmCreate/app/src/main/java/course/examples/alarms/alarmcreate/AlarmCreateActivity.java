package course.examples.alarms.alarmcreate;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

public class AlarmCreateActivity extends Activity {

    private AlarmManager mAlarmManager;
    private PendingIntent mNotificationReceiverPendingIntent,
            mLoggerReceiverPendingIntent;
    private static final long JITTER = 1000L;
    private static final long REPEAT_INTERVAL = 60 * 1000L;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        // Get the AlarmManager Service
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        Intent mNotificationReceiverIntent = new Intent(AlarmCreateActivity.this,
                AlarmNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                AlarmCreateActivity.this, 0, mNotificationReceiverIntent, 0);

        // Create an Intent to broadcast to the AlarmLoggerReceiver
        Intent mLoggerReceiverIntent = new Intent(AlarmCreateActivity.this,
                AlarmLoggerReceiver.class);

        // Create PendingIntent that holds the mLoggerReceiverPendingIntent
        mLoggerReceiverPendingIntent = PendingIntent.getBroadcast(
                AlarmCreateActivity.this, 0, mLoggerReceiverIntent, 0);

    }

    public void onClickButton(@SuppressWarnings("unused") View v) {

        // Set single alarm
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                mNotificationReceiverPendingIntent);

        // Set single alarm to fire shortly after previous alarm
        mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + JITTER,
                mLoggerReceiverPendingIntent);


        // Show Toast message
        Toast.makeText(getApplicationContext(), "Single Alarm Set",
                Toast.LENGTH_LONG).show();
    }


    public void onClickRepButton(@SuppressWarnings("unused") View v) {

        // Set repeating alarm
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime(),
                REPEAT_INTERVAL,
                mNotificationReceiverPendingIntent);

        // Set repeating alarm to fire shortly after previous alarm
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + JITTER,
                REPEAT_INTERVAL,
                mLoggerReceiverPendingIntent);

        // Show Toast message
        Toast.makeText(getApplicationContext(), "Repeating Alarm Set",
                Toast.LENGTH_LONG).show();
    }

    public void onClickCancelRepButton(@SuppressWarnings("unused") View v) {

        // Cancel all alarms using mNotificationReceiverPendingIntent
        mAlarmManager.cancel(mNotificationReceiverPendingIntent);

        // Cancel all alarms using mLoggerReceiverPendingIntent
        mAlarmManager.cancel(mLoggerReceiverPendingIntent);

        // Show Toast message
        Toast.makeText(getApplicationContext(),
                "Repeating Alarms Cancelled", Toast.LENGTH_LONG).show();
    }

}