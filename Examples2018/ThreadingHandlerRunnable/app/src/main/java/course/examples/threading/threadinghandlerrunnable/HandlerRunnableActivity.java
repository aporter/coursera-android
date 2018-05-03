package course.examples.threading.threadinghandlerrunnable;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class HandlerRunnableActivity extends Activity {

    @SuppressWarnings("unused")
    private final String TAG = getClass().getSimpleName();
    private LoadIconTask mLoadIconTask;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private final static String PROG_BAR_PROGRESS_KEY = "PROG_BAR_PROGRESS_KEY";
    private final static String PROG_BAR_VISIBLE_KEY = "PROG_BAR_VISIBLE_KEY";
    private final static String BITMAP_KEY = "BITMAP_KEY";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mImageView = findViewById(R.id.imageView);
        mProgressBar = findViewById(R.id.progressBar);

        if (null != savedInstanceState) {
            mProgressBar.setVisibility(savedInstanceState.getInt(PROG_BAR_VISIBLE_KEY));
            mProgressBar.setProgress(savedInstanceState.getInt(PROG_BAR_PROGRESS_KEY));
            mImageView.setImageBitmap((Bitmap) savedInstanceState.getParcelable(BITMAP_KEY));
            mLoadIconTask = ((LoadIconTask) getLastNonConfigurationInstance());
            if (null != mLoadIconTask) {
                mLoadIconTask.setmProgressBar(mProgressBar)
                        .setmImageView(mImageView);
            }
        }
    }

    public void onClickLoadButton(View v) {
        v.setEnabled(false);
        mLoadIconTask = new LoadIconTask(getApplicationContext())
                .setmImageView(mImageView)
                .setmProgressBar(mProgressBar);
        mLoadIconTask.start();
    }


    public void onClickOtherButton(@SuppressWarnings("unused") View v) {
        Toast.makeText(HandlerRunnableActivity.this, "I'm Working",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PROG_BAR_VISIBLE_KEY, mProgressBar.getVisibility());
        outState.putInt(PROG_BAR_PROGRESS_KEY, mProgressBar.getProgress());

        if (null != mImageView.getDrawable()) {
            Bitmap bm = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            outState.putParcelable(BITMAP_KEY, bm);
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        return mLoadIconTask;
    }
}
