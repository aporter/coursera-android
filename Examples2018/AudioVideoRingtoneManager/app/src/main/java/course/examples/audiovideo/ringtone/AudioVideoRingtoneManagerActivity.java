package course.examples.audiovideo.ringtone;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class AudioVideoRingtoneManagerActivity extends Activity {
    private Ringtone mCurrentRingtone;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mHandler = new Handler();

    }

    public void onClickRingtoneButton(@SuppressWarnings("unused") View v) {
        playRingtone(RingtoneManager.TYPE_RINGTONE);
    }

    public void onClickAlarmButton(@SuppressWarnings("unused") View v) {
        playRingtone(RingtoneManager.TYPE_ALARM);
    }

    public void onClickNotifButton(@SuppressWarnings("unused") View v) {
        playRingtone(RingtoneManager.TYPE_NOTIFICATION);
    }

    // Shut off current Ringtone and play new one
    private void playRingtone(int newRingtoneType) {

        Ringtone newRingtone = RingtoneManager.getRingtone(
                getApplicationContext(), RingtoneManager
                        .getDefaultUri(newRingtoneType));

        if (null != mCurrentRingtone && mCurrentRingtone.isPlaying())
            mCurrentRingtone.stop();

        mCurrentRingtone = newRingtone;

        if (null != newRingtone) {
            mCurrentRingtone.play();
            postStopRingtoneMessage();
        }
    }

    // Stop ringtones playing when Activity pauses
    @Override
    protected void onPause() {
        if (null != mCurrentRingtone) {
            mCurrentRingtone.stop();
        }
        super.onPause();
    }

    // Stop any ringtones playing in 5 seconds
    private void postStopRingtoneMessage() {
        final Runnable r = new Runnable() {
            public void run() {
                if (null != mCurrentRingtone) {
                    mCurrentRingtone.stop();
                }
            }
        };
        mHandler.postDelayed(r, 5000);
    }
}