package examples.course.lifecycleawareticker;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class TickerDisplayActivity extends AppCompatActivity {

    private TextView mCounterView;
    private TickerViewModel mTickerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker_display);

        mCounterView = findViewById(R.id.counter);

        // Get reference to TickerViewModel
        mTickerViewModel
                = ViewModelProviders.of(this).get(TickerViewModel.class);

        // Display initial Ticker value
        mCounterView.setText(String.valueOf(mTickerViewModel.getCounter().getValue()));

        // Observe changes to Ticker
        beginObservingTicker();

        // Tie TickerViewModel to Activity lifecycle
        mTickerViewModel.bindToActivityLifecycle(this);

    }

    private void beginObservingTicker() {

        // Create Observer
        final Observer<Integer> tickerObserver = new Observer<Integer>() {
            @Override
            public void onChanged(final Integer integer) {
                mCounterView.setText(String.valueOf(integer));
            }
        };

        // Register observer
        mTickerViewModel.getCounter().observe(this, tickerObserver);
    }
}
