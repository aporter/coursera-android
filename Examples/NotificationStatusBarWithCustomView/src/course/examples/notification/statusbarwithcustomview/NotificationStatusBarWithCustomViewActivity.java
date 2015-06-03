package course.examples.notification.statusbarwithcustomview;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RemoteViews;

public class NotificationStatusBarWithCustomViewActivity extends Activity {

	// Notification ID to allow for future updates
	private static final int MY_NOTIFICATION_ID = 1;

	// Notification Count
	private int mNotificationCount;

	// Notification Text Elements
	private final CharSequence tickerText = "This is a Really, Really, Super Long Notification Message!";
	private final CharSequence contentText = "You've Been Notified!";

	// Notification Action Elements
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;

	// Notification Sound and Vibration on Arrival
	private Uri soundURI = Uri
			.parse("android.resource://course.examples.notification.statusbarwithcustomview/"
					+ R.raw.alarm_rooster);
	private long[] mVibratePattern = { 0, 200, 200, 300 };

	RemoteViews mContentView = new RemoteViews(
			"course.examples.notification.statusbarwithcustomview",
			R.layout.custom_notification);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mNotificationIntent = new Intent(getApplicationContext(),
				NotificationSubActivity.class);
		mContentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
				mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		final Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Define the Notification's expanded message and Intent:

				mContentView.setTextViewText(R.id.text, contentText + " ("
						+ ++mNotificationCount + ")");

				// Build the Notification

				Notification.Builder notificationBuilder = new Notification.Builder(
						getApplicationContext())
					.setTicker(tickerText)
					.setSmallIcon(android.R.drawable.stat_sys_warning)
					.setAutoCancel(true)
					.setContentIntent(mContentIntent)
					.setSound(soundURI)
					.setVibrate(mVibratePattern)
					.setContent(mContentView);

				// Pass the Notification to the NotificationManager:
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.notify(MY_NOTIFICATION_ID,
						notificationBuilder.build());
			}
		});

	}
}