package course.examples.notification.statusbarwithcustomview

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews

class NotificationStatusBarWithCustomViewActivity : Activity() {


    companion object {

        // Notification ID to allow for future updates
        private val MY_NOTIFICATION_ID = 1

        private const val KEY_COUNT = "key_count"

        // Notification Text Elements
        private val mTickerText = "This is a Really, Really, Super Long Notification Message!"
        private val mContentText = "You've Been Notified!"

        // Notification Sound and Vibration on Arrival
        private val soundURI = Uri
            .parse("android.resource://course.examples.notification.statusbarwithcustomview/" + R.raw.alarm_rooster)
        private val mVibratePattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

        // Notification Channel ID
        private lateinit var mChannelID: String

        private lateinit var mNotificationManager: NotificationManager

//        private lateinit var soundURI: Uri
    }

    // Notification Count
    private var mNotificationCount: Int = 0


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        savedInstanceState?.run {
            mNotificationCount = savedInstanceState.getInt(KEY_COUNT)
        }
        createNotificationChannel()

    }

    private fun createNotificationChannel() {

        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        mChannelID = "$packageName.channel_01"
        // The user-visible name of the channel.
        val name = getString(R.string.channel_name)

        // The user-visible description of the channel.
        val description = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(mChannelID, name, importance)

        // Configure the notification channel.
        mChannel.description = description
        mChannel.enableLights(true)

        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        mChannel.lightColor = Color.RED
        mChannel.enableVibration(true)
        mChannel.vibrationPattern = mVibratePattern

//        Uri soundURI = Uri.parse("android.resource://" + packageName + "/" + R.raw.alarm_rooster);
        mChannel.setSound(
            soundURI,
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
        )

        mNotificationManager.createNotificationChannel(mChannel)
    }


    fun onClick(v: View) {

        // Define action Intent
        val notificationIntent = Intent(
            applicationContext,
            NotificationSubActivity::class.java
        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val contentIntent = PendingIntent.getActivity(
            applicationContext, 0,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val contentView = RemoteViews(
            packageName,
            R.layout.custom_notification
        )

        contentView.setTextViewText(
            R.id.notification_text,
            "$mContentText ( ${++mNotificationCount} )"
        )

        // Define the Notification's expanded message and Intent:
        val notificationBuilder = Notification.Builder(
            applicationContext, mChannelID
        )
            .setTicker(mTickerText)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .setCustomContentView(contentView)

        // Pass the Notification to the NotificationManager:
        mNotificationManager.notify(
            MY_NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_COUNT, mNotificationCount)
    }
}