package course.examples.Services.MusicService;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

public class MusicService extends Service {

	@SuppressWarnings("unused")
	private final String TAG = "MusicService";

	private static final int NOTIFICATION_ID = 1;
	private MediaPlayer mPlayer;
	private int mStartID;

	@Override
	public void onCreate() {
		super.onCreate();

		// Set up the Media Player
		mPlayer = MediaPlayer.create(this, R.raw.badnews);

		if (null != mPlayer) {

			mPlayer.setLooping(false);

			// Stop Service when music has finished playing
			mPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {

					// stop Service if it was started with this ID
					// Otherwise let other start commands proceed
					stopSelf(mStartID);

				}
			});
		}

		// Create a notification area notification so the user 
		// can get back to the MusicServiceClient
		
		final Intent notificationIntent = new Intent(getApplicationContext(),
				MusicServiceClient.class);
		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		final Notification notification = new Notification.Builder(
				getApplicationContext())
				.setSmallIcon(android.R.drawable.ic_media_play)
				.setOngoing(true).setContentTitle("Music Playing")
				.setContentText("Click to Access Music Player")
				.setContentIntent(pendingIntent).build();

		// Put this Service in a foreground state, so it won't 
		// readily be killed by the system  
		startForeground(NOTIFICATION_ID, notification);

	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {

		if (null != mPlayer) {

			// ID for this start command
			mStartID = startid;

			if (mPlayer.isPlaying()) {

				// Rewind to beginning of song
				mPlayer.seekTo(0);

			} else {

				// Start playing song
				mPlayer.start();

			}

		}

		// Don't automatically restart this Service if it is killed
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {

		if (null != mPlayer) {

			mPlayer.stop();
			mPlayer.release();

		}
	}

	// Can't bind to this Service
	@Override
	public IBinder onBind(Intent intent) {

		return null;

	}

}
