package examples.course.ticker

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class TickerDisplayActivity : Activity() {

    companion object {

        private const val COUNTER_KEY = "COUNTER_KEY"
        private const val DELAY: Long = 1000
    }

    private lateinit var mCounterView: TextView
    private lateinit var mUpdater: Runnable
    private var mCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticker_display)

        mCounterView = findViewById(R.id.counter)

        savedInstanceState?.let { mCounter = it.getInt(COUNTER_KEY) }

        // Runnable that updates the counter and then posts
        // itself to the Handler to be be run again after 1 second
        mUpdater = Runnable {
            mCounterView.text = (++mCounter).toString()
            mCounterView.postDelayed(mUpdater, DELAY)
        }
    }

    // Save instance state
    public override fun onSaveInstanceState(bundle: Bundle) {

        // Save mCounter value
        bundle.putInt(COUNTER_KEY, mCounter)

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(bundle)
    }

    override fun onPause() {
        super.onPause()
        setTimerEnabled(false)
    }

    override fun onResume() {
        super.onResume()
        setTimerEnabled(true)
    }

    private fun setTimerEnabled(isInForeground: Boolean) {
        if (isInForeground) {

            // Update the counter and post the mUpdater Runnable
            mCounterView.text = mCounter.toString()
            mCounterView.postDelayed(mUpdater, DELAY)
        } else {

            // Remove already posted Runnables to stop the ticker's updating
            mCounterView.removeCallbacks(mUpdater)
        }
    }
}
