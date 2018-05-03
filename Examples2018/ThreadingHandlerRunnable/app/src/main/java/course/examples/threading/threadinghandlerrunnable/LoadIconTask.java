package course.examples.threading.threadinghandlerrunnable;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class LoadIconTask extends Thread {
    @SuppressWarnings("unused")
    private final String TAG = getClass().getSimpleName();
    private final Handler mHandler;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private final int mBitmapResID = R.drawable.painter;
    private final Context mAppContext;

    LoadIconTask(Context context) {
        mAppContext = context;
        mHandler = new Handler();
    }

    public LoadIconTask setmImageView(ImageView mImageView) {
        this.mImageView = mImageView;
        return this;
    }

    public LoadIconTask setmProgressBar(ProgressBar mProgressBar) {
        this.mProgressBar = mProgressBar;
        return this;
    }

    public void run() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
            }
        });

        // Simulating long-running operation

        for (int i = 1; i < 11; i++) {
            sleep();
            final int step = i;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setProgress(step * 10);
                }
            });
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mImageView.setImageBitmap(
                        BitmapFactory.decodeResource(mAppContext.getResources(), mBitmapResID));
            }
        });

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }

    private void sleep() {
        try {
            int mDelay = 500;
            Thread.sleep(mDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
