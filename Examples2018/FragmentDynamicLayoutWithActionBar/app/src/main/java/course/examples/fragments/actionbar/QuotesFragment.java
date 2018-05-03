package course.examples.fragments.actionbar;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QuotesFragment extends Fragment {

    private static final String TAG = "QuotesFragment";
    private TextView mQuoteView;
    private int mCurrIdx = ListView.INVALID_POSITION;
    private String[] mQuoteArray;

    // Show the Quote string at position newIndex
    void showQuoteAtIndex(int index) {
        if (index >= 0 && index < mQuoteArray.length) {
            mQuoteView.setText(mQuoteArray[index]);
            mCurrIdx = index;
        }
    }

    // Called to create the content view for this Fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout defined in quote_fragment.xml
        // The last parameter is false because the returned view does not need to be attached to the container ViewGroup
        return inflater.inflate(R.layout.quote_fragment, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Don't destroy Fragment on reconfiguration
        setRetainInstance(true);

        // This Fragment adds options to the ActionBar
        setHasOptionsMenu(true);

    }

    // Set up some information about the mQuoteView TextView
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mQuoteView = getActivity().findViewById(R.id.quoteView);
        mQuoteArray = getResources().getStringArray(R.array.Quotes);

        showQuoteAtIndex(mCurrIdx);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate the options Menu using quote_menu.xml
        inflater.inflate(R.menu.quote_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Show Toast Messages. Toast Messages are discussed in the lesson on user interface classes
            // return value true indicates that the menu click has been handled

            case R.id.detail_menu_item_main:
                Toast.makeText(getActivity().getApplicationContext(),
                        "This action provided by the QuoteFragment",
                        Toast.LENGTH_SHORT).show();
                return true;

            case R.id.detail_menu_item_secondary:
                Toast.makeText(getActivity().getApplicationContext(),
                        "This action is also provided by the QuoteFragment",
                        Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }


    @Override
    public void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDetach()");
        super.onDetach();
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroyView()");
        super.onDestroyView();
    }

}