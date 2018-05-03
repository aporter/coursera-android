package course.examples.fragments.staticlayout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

//Several Activity lifecycle methods are instrumented to emit LogCat output
//so you can follow this class' lifecycle
public class QuoteViewerActivity extends Activity implements
        ListSelectionListener {

    private static final String TAG = "QuoteViewerActivity";
    public static String[] mTitleArray;
    public static String[] mQuoteArray;
    public static String mNoQuoteSelectedString;

    private QuotesFragment mQuotesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the string arrays with the titles and quotes
        mTitleArray = getResources().getStringArray(R.array.Titles);
        mQuoteArray = getResources().getStringArray(R.array.Quotes);
        mNoQuoteSelectedString = getResources().getString(R.string.noQuoteSelected);

        setContentView(R.layout.quote_activity);

        // Get a reference to the QuotesFragment
        mQuotesFragment = (QuotesFragment) getFragmentManager()
                .findFragmentById(R.id.details);
    }

    // Called by TitlesFragment when the user selects an item
    @Override
    public void onListSelection(int index) {
            // Tell the QuoteFragment to show the quote string at position index
        mQuotesFragment.showQuoteAtIndex(index);
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