package course.examples.datamanagement.sharedpreferences

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView

import java.util.Random

class SharedPreferenceReadWriteActivity : Activity() {

    companion object {
        private const val HIGH_SCORE_KEY = "HIGH_SCORE_KEY"
        private const val GAME_SCORE_KEY = "GAME_SCORE_KEY"
    }

    private lateinit var mGameScore: TextView
    private lateinit var mHighScore: TextView
    private lateinit var mPrefs: SharedPreferences

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPrefs = getPreferences(Context.MODE_PRIVATE)

        setContentView(R.layout.main)

        // High Score
        mHighScore = findViewById(R.id.high_score_text)
        mHighScore.text = mPrefs.getInt(HIGH_SCORE_KEY, 0).toString()

        //Game Score
        mGameScore = findViewById(R.id.game_score_text)
        if (null == savedInstanceState) {
            mGameScore.text = "0"
        } else {
            mGameScore.text = savedInstanceState.getString(GAME_SCORE_KEY)
        }
    }

    fun onClickPlayButton(v: View) {

        val r = Random()
        val highScore = r.nextInt(1000)
        mGameScore.text = highScore.toString()

        // Get Stored High Score
        if (highScore > mPrefs.getInt(HIGH_SCORE_KEY, 0)) {

            // Get and edit high score
            val editor = mPrefs.edit()
            editor.putInt(HIGH_SCORE_KEY, highScore)
            editor.apply()

            mHighScore.text = highScore.toString()

        }
    }

    fun onClickResetButton(v: View) {

        // Set high score to 0
        val editor = mPrefs.edit()
        editor.putInt(HIGH_SCORE_KEY, 0)
        editor.apply()

        mHighScore.text = "0"
        mGameScore.text = "0"

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(GAME_SCORE_KEY, mGameScore.text.toString())
    }
}