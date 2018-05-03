package examples.course.ticker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TickerDisplayActivity extends Activity {

    private static final String COUNTER_KEY = "COUNTER_KEY";
    private static final long delay = 1000;
    private TextView mCounterView;
    private static int mCounter = 0;
    private static Runnable update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker_display);

        mCounterView = findViewById(R.id.counter);

        if (null != savedInstanceState) {
            mCounter = savedInstanceState.getInt(COUNTER_KEY);
        }
        // Runnable that updates the counter and then posts
        // itself to the Handler to be be run again after 1 second
        update = new Runnable() {
            @Override
            public void run() {
                mCounterView.setText(String.valueOf(++mCounter));
                mCounterView.postDelayed(this, delay);
            }
        };
    }

    // Save instance state
    @Override
    public void onSaveInstanceState(Bundle bundle) {

        // Save mCounter value
        bundle.putInt(COUNTER_KEY, mCounter);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(bundle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setTimerEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTimerEnabled(true);
    }

    private void setTimerEnabled(boolean isInForeground) {
        if (isInForeground) {

            // Update the counter and post the update Runnable
            mCounterView.setText(String.valueOf(mCounter));
            mCounterView.postDelayed(update, delay);
        } else {

            // Remove already posted Runnables to stop the ticker's updating
            mCounterView.removeCallbacks(update);
        }
    }
}
