package course.examples.AudioVideo.VideoPlay;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class AudioVideoVideoPlayActivity extends Activity {
	VideoView mVideoView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Get a reference to the VideoView
		
		mVideoView = (VideoView) findViewById(R.id.videoViewer);

		// Add a Media controller to allow forward/reverse/pause/resume 
		
		final MediaController mMediaController = new MediaController(
				AudioVideoVideoPlayActivity.this, true);
		
		mMediaController.setEnabled(false);

		mVideoView.setMediaController(mMediaController);
		
		mVideoView
				.setVideoURI(Uri
						.parse("android.resource://course.examples.AudioVideo.VideoPlay/raw/moon"));
		
		// Add an OnPreparedListener to enable the MediaController once the video is ready
		mVideoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				mMediaController.setEnabled(true);
			}
		});
	}
	
	// Clean up and release resources
	@Override
	protected void onPause() {

		if (mVideoView != null && mVideoView.isPlaying()) {
			mVideoView.stopPlayback();
			mVideoView = null;
		}
		super.onPause();
	}
}