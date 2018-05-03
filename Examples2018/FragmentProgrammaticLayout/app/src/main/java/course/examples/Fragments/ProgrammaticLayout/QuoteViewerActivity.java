package course.examples.Fragments.ProgrammaticLayout;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

//Several Activity lifecycle methods are instrumented to emit LogCat output
//so you can follow this class' lifecycle
public class QuoteViewerActivity extends Activity implements ListSelectionListener {

    private static final String TAG = "QuoteViewerActivity";
    public static String[] mTitleArray;
    public static String[] mQuoteArray;
    public static String mNoQuoteSelectedString;
    // Reference to the QuotesFragment
    private QuotesFragment mQuoteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");

        // Get the string arrays with the titles and quotes
        mTitleArray = getResources().getStringArray(R.array.Titles);
        mQuoteArray = getResources().getStringArray(R.array.Quotes);
        mNoQuoteSelectedString = getResources().getString(R.string.noQuoteSelected);

        setContentView(R.layout.quote_activity);

        // Get a reference to the FragmentManager
        FragmentManager fragmentManager = getFragmentManager();


        if (null == fragmentManager.findFragmentById(R.id.title_frame)) {

            // Begin a new FragmentTransaction
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            // Add the TitleFragment
            fragmentTransaction.add(R.id.title_frame, new TitlesFragment());

            // Add the QuoteFragment
            mQuoteFragment = new QuotesFragment();
            fragmentTransaction.add(R.id.quote_frame, mQuoteFragment);

            // Commit the FragmentTransaction
            fragmentTransaction.commit();
        } else {
            mQuoteFragment = (QuotesFragment) fragmentManager.findFragmentById(R.id.quote_frame);
        }
    }

    // Called when the user selects an item in the TitlesFragment
    @Override
    public void onListSelection(int index) {
        // Tell the QuoteFragment to show the quote string at position index
        mQuoteFragment.showQuoteAtIndex(index);
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }

}