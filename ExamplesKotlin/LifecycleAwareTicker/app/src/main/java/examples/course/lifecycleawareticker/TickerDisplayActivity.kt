package examples.course.lifecycleawareticker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class TickerDisplayActivity : AppCompatActivity() {

    private lateinit var mCounterView: TextView
    private lateinit var mTickerViewModel: TickerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticker_display)

        mCounterView = findViewById(R.id.counter)

        // Get reference to TickerViewModel
        mTickerViewModel = ViewModelProviders.of(this).get(TickerViewModel::class.java)

        // Display initial Ticker value
        mCounterView.text = mTickerViewModel.counter.value.toString()

        // Observe changes to Ticker
        beginObservingTicker()

        // Tie TickerViewModel to Activity lifecycle
        mTickerViewModel.bindToActivityLifecycle(this)

    }

    private fun beginObservingTicker() {

        // Create Observer
        val tickerObserver = Observer<Int> { mCounterView.text = it.toString() }

        // Register observer
        mTickerViewModel.counter.observe(this, tickerObserver)
    }
}
