package course.examples.networking.json;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;


public class NetworkingJSONResponseActivity extends ListActivity implements RetainedFragment.OnFragmentInteractionListener {

    private RetainedFragment mRetainedFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            mRetainedFragment = (RetainedFragment) getFragmentManager()
                    .findFragmentByTag(RetainedFragment.TAG);
            onDownloadfinished();
        } else {

            mRetainedFragment = new RetainedFragment();
            getFragmentManager().beginTransaction()
                    .add(mRetainedFragment, RetainedFragment.TAG)
                    .commit();
            mRetainedFragment.onButtonPressed();
        }
    }


    public void onDownloadfinished() {
        if (null != mRetainedFragment.getData()) {
            setListAdapter(new ArrayAdapter<>(
                    NetworkingJSONResponseActivity.this,
                    R.layout.list_item, mRetainedFragment.getData()));
        }
    }
}