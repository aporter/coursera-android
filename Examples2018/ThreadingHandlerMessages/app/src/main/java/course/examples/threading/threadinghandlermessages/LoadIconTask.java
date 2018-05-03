package course.examples.threading.threadinghandlermessages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class LoadIconTask extends Thread {

    private final UIHandler mHandler;
    private final Context mContext;

    LoadIconTask(Context context) {
        mContext = context;
        mHandler = new UIHandler();
    }

    public void run() {

        Message msg = mHandler.obtainMessage(HandlerMessagesActivity.SET_PROGRESS_BAR_VISIBILITY,
                ProgressBar.VISIBLE);
        mHandler.sendMessage(msg);

        int mResId = R.drawable.painter;
        final Bitmap tmp = BitmapFactory.decodeResource(mContext.getResources(), mResId);

        for (int i = 1; i < 11; i++) {
            sleep();
            msg = mHandler.obtainMessage(HandlerMessagesActivity.PROGRESS_UPDATE, i * 10);
            mHandler.sendMessage(msg);
        }

        msg = mHandler.obtainMessage(HandlerMessagesActivity.SET_BITMAP, tmp);
        mHandler.sendMessage(msg);

        msg = mHandler.obtainMessage(HandlerMessagesActivity.SET_PROGRESS_BAR_VISIBILITY, ProgressBar.INVISIBLE);
        mHandler.sendMessage(msg);
    }

    public LoadIconTask setmImageView(ImageView mImageView) {
        mHandler.setmImageView(mImageView);
        return this;
    }

    public LoadIconTask setmProgressBar(ProgressBar mProgressBar) {
        mHandler.setmProgressBar(mProgressBar);
        return this;
    }

    private void sleep() {
        try {
            int mDelay = 500;
            Thread.sleep(mDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class UIHandler extends Handler {

        private ImageView mImageView;
        private ProgressBar mProgressBar;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessagesActivity.SET_PROGRESS_BAR_VISIBILITY: {
                    mProgressBar.setVisibility((Integer) msg.obj);
                    break;
                }
                case HandlerMessagesActivity.PROGRESS_UPDATE: {
                    mProgressBar.setProgress((Integer) msg.obj);
                    break;
                }
                case HandlerMessagesActivity.SET_BITMAP: {
                    mImageView.setImageBitmap((Bitmap) msg.obj);
                    break;
                }
            }
        }

        void setmImageView(ImageView mImageView) {
            this.mImageView = mImageView;
        }

        void setmProgressBar(ProgressBar mProgressBar) {
            this.mProgressBar = mProgressBar;
        }
    }
}