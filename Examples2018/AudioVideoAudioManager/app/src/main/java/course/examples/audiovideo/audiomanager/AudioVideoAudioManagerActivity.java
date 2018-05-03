package course.examples.audiovideo.audiomanager;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AudioVideoAudioManagerActivity extends Activity {
    private static final String TAG = "AudioVideoAudioManager";
    private int mVolume = 6;
    private final int mVolumeMax = 10;
    private SoundPool mSoundPool;
    private int mSoundId;
    private AudioManager mAudioManager;
    private boolean mCanPlayAudio;
    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Get reference to the AudioManager
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Display current volume level in TextView
        mTextView= findViewById(R.id.textView1);
        mTextView.setText(String.valueOf(mVolume));

        final Button playButton = findViewById(R.id.button3);

        // Disable the Play Button so user can't click it before sounds are ready
        playButton.setEnabled(false);

        // Create a SoundPool
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        mSoundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();

        // Load bubble popping sound into the SoundPool
        mSoundId = mSoundPool.load(this, R.raw.slow_whoop_bubble_pop, 1);

        // Set an OnLoadCompleteListener on the SoundPool
        mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {

                // If sound loading was successful enable the play Button
                if (0 == status) {
                    playButton.setEnabled(true);
                } else {
                    Log.i(TAG, "Unable to load sound");
                    finish();
                }
            }
        });

        // Request audio focus
        int result = mAudioManager.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        // Set to true if app has audio focus
        mCanPlayAudio = AudioManager.AUDIOFOCUS_REQUEST_GRANTED == result;

    }


    public void onUpButtonClick(@SuppressWarnings("unused") View v) {
        // Play key click sound
        mAudioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);

        if (mVolume < mVolumeMax) {
            mVolume += 2;
            mTextView.setText(String.valueOf(mVolume));
        }
    }

    public void onDownButtonClick(@SuppressWarnings("unused") View v) {
        // Play key click sound
        mAudioManager.playSoundEffect(AudioManager.FX_KEY_CLICK);

        int mVolumeMin = 0;
        if (mVolume > mVolumeMin) {
            mVolume -= 2;
            mTextView.setText(String.valueOf(mVolume));
        }
    }

    // Play the sound using the SoundPool
    public void onPlayButtonClick(@SuppressWarnings("unused") View v) {
        if (mCanPlayAudio)
            mSoundPool.play(mSoundId, (float) mVolume / mVolumeMax,
                    (float) mVolume / mVolumeMax, 1, 0, 1.0f);
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
    private final OnAudioFocusChangeListener afChangeListener = new OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mAudioManager.abandonAudioFocus(afChangeListener);
                mCanPlayAudio = false;
            }
        }
    };
}