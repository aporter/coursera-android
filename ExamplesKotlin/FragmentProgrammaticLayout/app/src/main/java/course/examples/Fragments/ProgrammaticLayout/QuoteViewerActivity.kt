package course.examples.Fragments.ProgrammaticLayout

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


    // Reference to the QuotesFragment
    private lateinit var mQuoteFragment: QuotesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "${javaClass.simpleName}::entered onCreate()")

        // Get the string arrays with the titles and quotes
        mTitleArray = resources.getStringArray(R.array.Titles)
        mQuoteArray = resources.getStringArray(R.array.Quotes)
        mNoQuoteSelectedString = resources.getString(R.string.noQuoteSelected)

        setContentView(R.layout.quote_activity)

        // Get a reference to the FragmentManager
        val fragmentManager = supportFragmentManager

        if (null == fragmentManager.findFragmentById(R.id.title_frame)) {

            // Begin a new FragmentTransaction
            val fragmentTransaction = fragmentManager
                .beginTransaction()

            // Add the TitleFragment
            fragmentTransaction.add(R.id.title_frame, TitlesFragment())

            // Add the QuoteFragment
            mQuoteFragment = QuotesFragment()
            fragmentTransaction.add(R.id.quote_frame, mQuoteFragment)

            // Commit the FragmentTransaction
            fragmentTransaction.commit()
        } else {
            mQuoteFragment = fragmentManager.findFragmentById(R.id.quote_frame) as QuotesFragment
        }
    }

    // Called when the user selects an item in the TitlesFragment
    override fun onListSelection(index: Int) {
        // Tell the QuoteFragment to show the quote string at position index
        mQuoteFragment.showQuoteAtIndex(index)
    }

    override fun onDestroy() {
        Log.i(TAG, "${javaClass.simpleName}::entered onDestroy()")
        super.onDestroy()
    }

    override fun onPause() {
        Log.i(TAG, "${javaClass.simpleName}::entered onPause()")
        super.onPause()
    }

    override fun onRestart() {
        Log.i(TAG, "${javaClass.simpleName}::entered onRestart()")
        super.onRestart()
    }

    override fun onResume() {
        Log.i(TAG, "${javaClass.simpleName}::entered onResume()")
        super.onResume()
    }

    override fun onStart() {
        Log.i(TAG, "${javaClass.simpleName}::entered onStart()")
        super.onStart()
    }

    override fun onStop() {
        Log.i(TAG, "${javaClass.simpleName}::entered onStop()")
        super.onStop()
    }
}