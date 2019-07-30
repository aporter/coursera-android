package course.examples.fragments.staticlayout

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log

//Several Activity lifecycle methods are instrumented to emit LogCat output
//so you can follow this class' lifecycle
class QuoteViewerActivity : FragmentActivity(), ListSelectionListener {

    companion object {

        private const val TAG = "QuoteViewerActivity"
        private const val LAST_INDEX = "last_index"
        lateinit var mTitleArray: Array<String>
        lateinit var mQuoteArray: Array<String>
        lateinit var mNoQuoteSelectedString: String
    }

    private lateinit var mQuotesFragment: QuotesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the string arrays with the titles and quotes
        mTitleArray = resources.getStringArray(R.array.Titles)
        mQuoteArray = resources.getStringArray(R.array.Quotes)
        mNoQuoteSelectedString = resources.getString(R.string.noQuoteSelected)

        setContentView(R.layout.quote_activity)

        // Get a reference to the QuotesFragment defined in the layout file
        mQuotesFragment = supportFragmentManager
            .findFragmentById(R.id.details) as QuotesFragment

        // Reset quote text on reconfiguration
        savedInstanceState?.let {
            mQuotesFragment.mCurrIdx = savedInstanceState.getInt(LAST_INDEX)
        }
    }

    // Called by TitlesFragment when the user selects an item
    override fun onListSelection(index: Int) {
        // Tell the QuoteFragment to show the quote string at position index
        mQuotesFragment.showQuoteAtIndex(index)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mQuotesFragment.let {
            outState.putInt(LAST_INDEX, mQuotesFragment.mCurrIdx)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        Log.i(TAG, "${javaClass.simpleName}:entered onDestroy()")
        super.onDestroy()
    }

    override fun onPause() {
        Log.i(TAG, "${javaClass.simpleName}:entered onPause()")
        super.onPause()
    }

    override fun onRestart() {
        Log.i(TAG, "${javaClass.simpleName}:entered onRestart()")
        super.onRestart()
    }

    override fun onResume() {
        Log.i(TAG, "${javaClass.simpleName}:entered onResume()")
        super.onResume()
    }

    override fun onStart() {
        Log.i(TAG, "${javaClass.simpleName}:entered onStart()")
        super.onStart()
    }

    override fun onStop() {
        Log.i(TAG, "${javaClass.simpleName}:entered onStop()")
        super.onStop()
    }
}