package course.examples.notification.statusbarwithcustomview;

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
import android.widget.RemoteViews;

public class NotificationStatusBarWithCustomViewActivity extends Activity {

    private NotificationManager mNotificationManager;

    // Notification ID to allow for future updates
    private static final int MY_NOTIFICATION_ID = 1;

    // Notification Count
    private int mNotificationCount;

    // Notification Text Elements
    private final CharSequence mTickerText = "This is a Really, Really, Super Long Notification Message!";
    private final CharSequence mContentText = "You've Been Notified!";

    // Notification Sound and Vibration on Arrival
    private final Uri soundURI = Uri
            .parse("android.resource://course.examples.notification.statusbarwithcustomview/"
                    + R.raw.alarm_rooster);
    private final long[] mVibratePattern = {100, 200, 300, 400, 500, 400, 300, 200, 400};

    // Notification Channel ID
    private String mChannelID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        createNotificationChannel();

    }

    private void createNotificationChannel() {

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mChannelID = getPackageName() + ".channel_01";
        // The user-visible name of the channel.
        CharSequence name = getString(R.string.channel_name);

        // The user-visible description of the channel.
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

        //Uri soundURI = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alarm_rooster);
        mChannel.setSound(soundURI, (new AudioAttributes.Builder()).setUsage(AudioAttributes.USAGE_NOTIFICATION).build());

        mNotificationManager.createNotificationChannel(mChannel);
    }


    public void onClick(@SuppressWarnings("unused") View v) {

        // Define action Intent
        Intent notificationIntent = new Intent(getApplicationContext(),
                NotificationSubActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews contentView = new RemoteViews(
                getPackageName(),
                R.layout.custom_notification);

        contentView.setTextViewText(R.id.notification_text, mContentText + " (" + ++mNotificationCount + ")");

        // Define the Notification's expanded message and Intent:
        Notification.Builder notificationBuilder = new Notification.Builder(
                getApplicationContext(), mChannelID)
                .setTicker(mTickerText)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setCustomContentView(contentView);

        // Pass the Notification to the NotificationManager:
        mNotificationManager.notify(MY_NOTIFICATION_ID,
                notificationBuilder.build());
    }
}