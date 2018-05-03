package course.examples.threading.threadingasynctask;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

public class AsyncTaskFragment extends Fragment {
    static final String TAG = "AsyncTaskFragment";
    private static final int PAINTER = R.drawable.painter;
    private OnFragmentInteractionListener mListener;
    private Bitmap mImageBitmap;

    public AsyncTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }


    void onButtonPressed() {
        new LoadIconTask(this).execute(PAINTER);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    // Upcalls to hosting Activity
    private void setProgressBarVisibility(int isVisible) {
        if (null != mListener) {
            mListener.setProgressBarVisibility(isVisible);
        }
    }

    private void setImageBitmap(Bitmap result) {
        mImageBitmap = result;
        if (null != mListener) {
            mListener.setImageBitmap(result);
        }
    }

    private void setProgress(Integer value) {
        if (null != mListener) {
            mListener.setProgress(value);
        }
    }

    Bitmap getImageBitmap() {
        return mImageBitmap;
    }

    // Interface to Hosting Activity
    public interface OnFragmentInteractionListener {
        void setProgressBarVisibility(int isVisible);

        void setImageBitmap(Bitmap result);

        void setProgress(Integer value);
    }

    static class LoadIconTask extends AsyncTask<Integer, Integer, Bitmap> {
        // GC can reclaim weakly referenced variables.
        private final WeakReference<AsyncTaskFragment> mAsyncTaskFragment;

        LoadIconTask(AsyncTaskFragment fragment) {
            mAsyncTaskFragment = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            mAsyncTaskFragment.get().setProgressBarVisibility(ProgressBar.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Integer... resId) {
            // simulating long-running operation
            for (int i = 1; i < 11; i++) {
                sleep();
                publishProgress(i * 10);
            }
            return BitmapFactory.decodeResource(mAsyncTaskFragment.get().getResources(), resId[0]);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mAsyncTaskFragment.get().setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            mAsyncTaskFragment.get().setProgressBarVisibility(ProgressBar.INVISIBLE);
            mAsyncTaskFragment.get().setImageBitmap(result);
        }

        private void sleep() {
            try {
                int mDelay = 500;
                Thread.sleep(mDelay);
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
        }
    }
}
