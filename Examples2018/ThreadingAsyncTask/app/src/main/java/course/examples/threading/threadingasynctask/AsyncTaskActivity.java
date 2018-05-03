package course.examples.threading.threadingasynctask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AsyncTaskActivity extends Activity implements AsyncTaskFragment.OnFragmentInteractionListener {

    private final static String PROG_BAR_PROGRESS_KEY = "PROG_BAR_PROGRESS_KEY";
    private final static String PROG_BAR_VISIBLE_KEY = "PROG_BAR_VISIBLE_KEY";

    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private Button mLoadButton;
    private AsyncTaskFragment mAsyncTaskFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        mImageView = findViewById(R.id.imageView);
        mProgressBar = findViewById(R.id.progressBar);
        mLoadButton = findViewById(R.id.loadButton);

        if (null != savedInstanceState) {
            mProgressBar.setVisibility(savedInstanceState.getInt(PROG_BAR_VISIBLE_KEY));
            mProgressBar.setProgress(savedInstanceState.getInt(PROG_BAR_PROGRESS_KEY));
            mAsyncTaskFragment = (AsyncTaskFragment) getFragmentManager()
                    .findFragmentByTag(AsyncTaskFragment.TAG);
            mImageView.setImageBitmap(mAsyncTaskFragment.getImageBitmap());
        } else {
            // Create headless Fragment that runs LoadIconAsyncTask and stores the loaded bitmap
            mAsyncTaskFragment = new AsyncTaskFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(mAsyncTaskFragment, AsyncTaskFragment.TAG)
                    .commit();
        }
    }

    public void onClickLoadButton(@SuppressWarnings("unused") View v) {
        mLoadButton.setEnabled(false);
        mAsyncTaskFragment.onButtonPressed();
    }


    public void onClickOtherButton(@SuppressWarnings("unused") View v) {
        Toast.makeText(AsyncTaskActivity.this, "I'm Working",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PROG_BAR_VISIBLE_KEY, mProgressBar.getVisibility());
        outState.putInt(PROG_BAR_PROGRESS_KEY, mProgressBar.getProgress());
    }


    // Callbacks from AsyncTaskFragment
    @Override
    public void setProgressBarVisibility(int isVisible) {
        mProgressBar.setVisibility(isVisible);
    }

    @Override
    public void setProgress(Integer value) {
        mProgressBar.setProgress(value);
    }

    @Override
    public void setImageBitmap(Bitmap result) {
        mImageView.setImageBitmap(result);
    }
}