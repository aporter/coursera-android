package course.examples.AudioVideo.AudioManager;

import android.app.Activity;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AudioVideoAudioManagerActivity extends Activity {
	private int mVolume = 6, mVolumeMax = 10, mVolumeMin = 0;
	private SoundPool mSoundPool;
	private int mSoundId;
	private AudioManager mAudioManager;
	private boolean mCanPlayAudio;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Get reference to the AudioManager
		mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

		// Displays current volume level
		final TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText(String.valueOf(mVolume));

		// Increase the volume
		final Button upButton = (Button) findViewById(R.id.button2);
		upButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Play key click sound
				mAudioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);

				if (mVolume < mVolumeMax) {
					mVolume += 2;
					tv.setText(String.valueOf(mVolume));
				}
			}
		});

		// Decrease the volume
		final Button downButton = (Button) findViewById(R.id.button1);
		downButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// Play key click sound
				mAudioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);

				if (mVolume > mVolumeMin) {
					mVolume -= 2;
					tv.setText(String.valueOf(mVolume));
				}

			}
		});

		// Disable the Play Button
		final Button playButton = (Button) findViewById(R.id.button3);
		playButton.setEnabled(false);

		// Create a SoundPool
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

		// Load the sound
		mSoundId = mSoundPool.load(this, R.raw.slow_whoop_bubble_pop, 1);

		// Set an OnLoadCompleteListener on the SoundPool
		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				if (0 == status) {
					playButton.setEnabled(true);
				}
			}
		});

		// Play the sound using a SoundPool
		playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCanPlayAudio)
					mSoundPool.play(mSoundId, (float) mVolume / mVolumeMax,
							(float) mVolume / mVolumeMax, 1, 0, 1.0f);
			}

		});

		// Request audio focus
		int result = mAudioManager.requestAudioFocus(afChangeListener,
				AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

		mCanPlayAudio = AudioManager.AUDIOFOCUS_REQUEST_GRANTED == result;

	}

	// Get ready to play sound effects
	@Override
	protected void onResume() {
		super.onResume();
		mAudioManager.setSpeakerphoneOn(true);
		mAudioManager.loadSoundEffects();
	}

	// Release resources & clean up
	@Override
	protected void onPause() {
		if (null != mSoundPool) {
			mSoundPool.unload(mSoundId);
			mSoundPool.release();
			mSoundPool = null;
		}
		mAudioManager.setSpeakerphoneOn(false);
		mAudioManager.unloadSoundEffects();
		super.onPause();
	}

	// Listen for Audio focus changes
	OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
		public void onAudioFocusChange(int focusChange) {
			if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
				mAudioManager.abandonAudioFocus(afChangeListener);
				mCanPlayAudio = false;
			}
		}
	};

}