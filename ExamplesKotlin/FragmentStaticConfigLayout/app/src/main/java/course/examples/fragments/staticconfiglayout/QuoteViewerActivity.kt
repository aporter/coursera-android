package course.examples.fragments.staticconfiglayout

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log

//Several Activity lifecycle methods are instrumented to emit LogCat output
//so you can follow this class' lifecycle
class QuoteViewerActivity : FragmentActivity(), ListSelectionListener {


    companion object {

        private const val TAG = "QuoteViewerActivity"
        lateinit var mTitleArray: Array<String>
        lateinit var mQuoteArray: Array<String>
        lateinit var mNoQuoteSelectedString: String
    }

    private lateinit var mQuoteFragment: QuotesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the string arrays with the titles and quotes
        mTitleArray = resources.getStringArray(R.array.Titles)
        mQuoteArray = resources.getStringArray(R.array.Quotes)
        mNoQuoteSelectedString = resources.getString(R.string.noQuoteSelected)

        setContentView(R.layout.main)

        // Get a reference to the QuotesFragment
        mQuoteFragment = supportFragmentManager
            .findFragmentById(R.id.details) as QuotesFragment
    }

    // Called when the user selects an item in the TitlesFragment
    override fun onListSelection(index: Int) {

        // Tell the QuoteFragment to show the quote string at position index
        mQuoteFragment.showQuoteAtIndex(index)

    }

    override fun onDestroy() {
        Log.i(TAG, "${javaClass.simpleName} entered onDestroy()")
        super.onDestroy()
    }

    override fun onPause() {
        Log.i(TAG, "${javaClass.simpleName} entered onPause()")
        super.onPause()
    }

    override fun onRestart() {
        Log.i(TAG, "${javaClass.simpleName} entered onRestart()")
        super.onRestart()
    }

    override fun onResume() {
        Log.i(TAG, "${javaClass.simpleName} entered onResume()")
        super.onResume()
    }

    override fun onStart() {
        Log.i(TAG, "${javaClass.simpleName} entered onStart()")
        super.onStart()
    }

    override fun onStop() {
        Log.i(TAG, "${javaClass.simpleName} entered onStop()")
        super.onStop()
    }
}