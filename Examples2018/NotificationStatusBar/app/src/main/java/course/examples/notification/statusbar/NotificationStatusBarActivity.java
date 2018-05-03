package course.examples.notification.statusbar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class NotificationStatusBarActivity extends Activity {

    // Notification ID to allow for future updates
    private static final int MY_NOTIFICATION_ID = 1;

    // Notification Count
    private int mNotificationCount;

    // Notification Text Elements
    private final CharSequence tickerText = "This is a Really, Really, Super Long Notification Message!";
    private final CharSequence contentTitle = "Notification";
    private final CharSequence contentText = "You've Been Notified!";

    private final long[] mVibratePattern = {100, 200, 300, 400, 500, 400, 300, 200, 400};

    private String mChannelID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mChannelID = getPackageName() + ".channel_01";

        // The user-visible name of the channel.
        CharSequence name = getString(R.string.channel_name);

        // The user-visible description of the channel
        String description = getString(R.string.channel_description);
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

        Uri mSoundURI = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm_rooster);
        mChannel.setSound(mSoundURI, (new AudioAttributes.Builder())
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build());

        mNotificationManager.createNotificationChannel(mChannel);
    }


    public void onClick(@SuppressWarnings("unused") View v) {


        // Define action Intent
        Intent mNotificationIntent = new Intent(getApplicationContext(),
                NotificationSubActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent mContentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        // Define the Notification's expanded message and Intent:

        Notification.Builder notificationBuilder = new Notification.Builder(
                getApplicationContext(), mChannelID)
                .setTicker(tickerText)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setContentText(
                        contentText + " (" + ++mNotificationCount + ")")
                .setContentIntent(mContentIntent);

        // Pass the Notification to the NotificationManager:
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());
    }
}