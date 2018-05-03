package course.examples.alarms.alarmcreate;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.util.Log;

import java.text.DateFormat;
import java.util.Date;

public class AlarmNotificationReceiver extends BroadcastReceiver {
    // Notification ID to allow for future updates
    private static final int MY_NOTIFICATION_ID = 1;
    private static final String TAG = "AlarmNotificationReceiver";

    // Notification Text Elements
    private final CharSequence tickerText = "Are You Playing Angry Birds Again!";
    private final CharSequence contentTitle = "A Kind Reminder";
    private final CharSequence contentText = "Get back to studying!!";

    // Notification Sound and Vibration on Arrival
    private final Uri mSoundURI = Uri
            .parse("android.resource://course.examples.Alarms.AlarmCreate/"
                    + R.raw.alarm_rooster);
    private final long[] mVibratePattern = {0, 200, 200, 300};
    private Context mContext;
    private String mChannelID;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;

        createNotificationChannel();

        // The Intent to be used when the user clicks on the Notification View
        Intent mNotificationIntent = new Intent(context, AlarmCreateActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // The PendingIntent that wraps the underlying Intent
        PendingIntent mContentIntent = PendingIntent.getActivity(context, 0,
                mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the Notification
        Notification.Builder notificationBuilder = new Notification.Builder(
                mContext,mChannelID).setTicker(tickerText)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setAutoCancel(true).setContentTitle(contentTitle)
                .setContentText(contentText).setContentIntent(mContentIntent);

        // Get the NotificationManager
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // Pass the Notification to the NotificationManager:
        mNotificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());

        // Log occurrence of notify() call
        Log.i(TAG, mContext.getString(R.string.sending_not_string)
                + DateFormat.getDateTimeInstance().format(new Date()));
    }

    private void createNotificationChannel() {
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        mChannelID = mContext.getPackageName() + ".channel_01";

        // The user-visible name of the channel.
        CharSequence name = mContext.getString(R.string.channel_name);

        // The user-visible description of the channel
        String description = mContext.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(mChannelID, name, importance);

        // Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.enableLights(true);

        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(mVibratePattern);

        mChannel.setSound(mSoundURI, (new AudioAttributes.Builder())
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build());

        mNotificationManager.createNotificationChannel(mChannel);
    }
}
