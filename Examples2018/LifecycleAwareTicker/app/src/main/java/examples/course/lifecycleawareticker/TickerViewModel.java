package examples.course.lifecycleawareticker;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

class TickerViewModel extends ViewModel implements DefaultLifecycleObserver {

    private static final int ONE_SECOND = 1000;
    private final MutableLiveData<Integer> mCounter = new MutableLiveData<>();
    private Timer mTimer;

    TickerViewModel() {
        // Set initial value
        mCounter.setValue(0);
    }

    LiveData<Integer> getCounter() {
        return mCounter;
    }

    void bindToActivityLifecycle(TickerDisplayActivity tickerDisplayActivity) {
        tickerDisplayActivity.getLifecycle().addObserver(this);
    }


    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        mTimer = new Timer();

        // Update the elapsed time every second.
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // setValue() cannot be called from a background thread so post to main thread.
                //noinspection ConstantConditions
                mCounter.postValue(mCounter.getValue() + 1);
            }
        }, ONE_SECOND, ONE_SECOND);
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        mTimer.cancel();
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        // Not implemented
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        // Not implemented
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        // Not implemented
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        // Not implemented
    }
}
