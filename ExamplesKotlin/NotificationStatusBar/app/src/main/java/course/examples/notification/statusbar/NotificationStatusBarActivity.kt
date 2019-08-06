package course.examples.notification.statusbar

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

class NotificationStatusBarActivity : Activity() {


    companion object {

        // Notification ID to allow for future updates
        private const val MY_NOTIFICATION_ID = 1

        private const val KEY_COUNT = "key_count"
        private lateinit var mNotificationManager: NotificationManager
        private lateinit var mChannelID: String

        // Notification Text Elements
        private const val tickerText = "This is a Really, Really, Super Long Notification Message!"
        private const val contentTitle = "Notification"
        private const val contentText = "You've Been Notified!"

        private val mVibratePattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

    }


    // Notification Count
    private var mNotificationCount: Int = 0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        savedInstanceState?.run {
            mNotificationCount = savedInstanceState.getInt(KEY_COUNT)
        }

        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
    }

    private fun createNotificationChannel() {

        mChannelID = "$packageName.channel_01"

        // The user-visible name of the channel.
        val name = getString(R.string.channel_name)

        // The user-visible description of the channel
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

        val mSoundURI = Uri.parse("android.resource://" + packageName + "/" + R.raw.alarm_rooster)
        mChannel.setSound(
            mSoundURI, AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
        )

        mNotificationManager.createNotificationChannel(mChannel)
    }


    fun onClick(v: View) {


        // Define action Intent
        val mNotificationIntent = Intent(
            applicationContext,
            NotificationSubActivity::class.java
        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val mContentIntent = PendingIntent.getActivity(
            applicationContext, 0,
            mNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )


        // Define the Notification's expanded message and Intent:

        val notificationBuilder = Notification.Builder(
            applicationContext, mChannelID
        )
            .setTicker(tickerText)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setAutoCancel(true)
            .setContentTitle(contentTitle)
            .setContentText("$contentText ( ${++mNotificationCount} )")
            .setContentIntent(mContentIntent)

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