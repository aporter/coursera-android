package course.examples.audiovideo.audiorecording;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class AudioRecordingActivity extends Activity {
	private static final String TAG = "AudioRecordTest";
	private static final String mFileName = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/audiorecordtest.3gp";
	private MediaRecorder mRecorder;
	private MediaPlayer mPlayer;
	private AudioManager mAudioManager;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		final ToggleButton mRecordButton = (ToggleButton) findViewById(R.id.record_button);
		final ToggleButton mPlayButton = (ToggleButton) findViewById(R.id.play_button);

		// Set up record Button
		mRecordButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				// Set enabled state
				mPlayButton.setEnabled(!isChecked);

				// Start/stop recording
				onRecordPressed(isChecked);

			}
		});

		// Set up play Button
		mPlayButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				// Set enabled state
				mRecordButton.setEnabled(!isChecked);

				// Start/stop playback
				onPlayPressed(isChecked);
			}
		});

		// Get AudioManager
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		// Request audio focus
		mAudioManager.requestAudioFocus(afChangeListener,
				AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

	}

	// Toggle recording
	private void onRecordPressed(boolean shouldStartRecording) {

		if (shouldStartRecording) {
			startRecording();
		} else {
			stopRecording();
		}

	}

	// Start recording with MediaRecorder
	private void startRecording() {

		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e(TAG, "Couldn't prepare and start MediaRecorder");
		}

		mRecorder.start();
	}

	// Stop recording. Release resources
	private void stopRecording() {

		if (null != mRecorder) {
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}

	}

	// Toggle playback
	private void onPlayPressed(boolean shouldStartPlaying) {

		if (shouldStartPlaying) {
			startPlaying();
		} else {
			stopPlaying();
		}

	}

	// Playback audio using MediaPlayer
	private void startPlaying() {

		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			Log.e(TAG, "Couldn't prepare and start MediaPlayer");
		}

	}

	// Stop playback. Release resources
	private void stopPlaying() {
		if (null != mPlayer) {
			if (mPlayer.isPlaying())
				mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}

	// Listen for Audio Focus changes
	OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {

		@Override
		public void onAudioFocusChange(int focusChange) {

			if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				mAudioManager.abandonAudioFocus(afChangeListener);

				// Stop playback, if necessary
				if (null != mPlayer && mPlayer.isPlaying())
					stopPlaying();
			}

		}

	};

	// Release recording and playback resources, if necessary
	@Override
	public void onPause() {
		super.onPause();

		if (null != mRecorder) {
			mRecorder.release();
			mRecorder = null;
		}

		if (null != mPlayer) {
			mPlayer.release();
			mPlayer = null;
		}

	}

}