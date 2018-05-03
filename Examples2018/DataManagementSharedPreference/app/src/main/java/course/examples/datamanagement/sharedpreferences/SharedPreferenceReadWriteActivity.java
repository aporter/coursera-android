package course.examples.datamanagement.sharedpreferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class SharedPreferenceReadWriteActivity extends Activity {
    private static final String HIGH_SCORE_KEY = "HIGH_SCORE_KEY";
    private static final String GAME_SCORE_KEY = "GAME_SCORE_KEY";
    private TextView mGameScore, mHighScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        setContentView(R.layout.main);

        // High Score
        mHighScore = findViewById(R.id.high_score_text);
        mHighScore.setText(String.valueOf(prefs.getInt(HIGH_SCORE_KEY, 0)));

        //Game Score
        mGameScore = findViewById(R.id.game_score_text);
        if (null == savedInstanceState) {
            mGameScore.setText(String.valueOf("0"));
        } else {
            mGameScore.setText(savedInstanceState.getString(GAME_SCORE_KEY));
        }

        // Play Button
        final Button playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Random r = new Random();
                int val = r.nextInt(1000);
                mGameScore.setText(String.valueOf(val));

                // Get Stored High Score
                if (val > prefs.getInt(HIGH_SCORE_KEY, 0)) {

                    // Get and edit high score
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(HIGH_SCORE_KEY, val);
                    editor.apply();

                    mHighScore.setText(String.valueOf(val));

                }
            }
        });

        // Reset Button
        final Button resetButton = findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // Set high score to 0
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(HIGH_SCORE_KEY, 0);
                editor.apply();

                mHighScore.setText(String.valueOf("0"));
                mGameScore.setText(String.valueOf("0"));

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GAME_SCORE_KEY,mGameScore.getText().toString());
    }

}