package examples.course.lifecycleawareticker

import android.arch.lifecycle.DefaultLifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import android.os.Handler
import android.util.Log

class TickerViewModel : ViewModel(), DefaultLifecycleObserver {

    companion object {
        private const val ONE_SECOND = 1000
        private const val TAG = "TickerViewModel"
    }

    private val mCounter = MutableLiveData<Int>()
    private lateinit var mHandler: Handler

    internal val counter: LiveData<Int>
        get() = mCounter

    private val updater: Runnable = object : Runnable {
        override fun run() {

            mCounter.value = mCounter.value!! + 1
            mHandler.postDelayed(this, ONE_SECOND.toLong())
        }
    }

    init {
        // Set initial value
        mCounter.value = 0
        mHandler = Handler()
    }

    internal fun bindToActivityLifecycle(tickerDisplayActivity: TickerDisplayActivity) {
        tickerDisplayActivity.lifecycle.addObserver(this)
    }

    override fun onResume(owner: LifecycleOwner) {

        // Update the elapsed time every second.
        mHandler.postDelayed(updater, 1000L)

        Log.i(TAG,"Entered onResume")
    }

    override fun onPause(owner: LifecycleOwner) {
        // Cancel work on Handler
        mHandler.removeCallbacks(updater)

        Log.i(TAG,"Entered onPause")
    }

    override fun onCreate(owner: LifecycleOwner) {
        Log.i(TAG,"Entered onCreate")
    }

    override fun onStart(owner: LifecycleOwner) {
        Log.i(TAG,"Entered onStart")
    }

    override fun onStop(owner: LifecycleOwner) {
        Log.i(TAG,"Entered onStop")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        Log.i(TAG,"Entered onDestroy")
    }
}
