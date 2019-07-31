package course.examples.fragments.dynamiclayout

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.util.Log
import android.widget.FrameLayout
import android.widget.LinearLayout

//Several Activity lifecycle methods are instrumented to emit LogCat output
//so you can follow this class' lifecycle
class QuoteViewerActivity : FragmentActivity(), ListSelectionListener {

    companion object {

        private const val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT
        private const val TAG = "QuoteViewerActivity"
        lateinit var mTitleArray: Array<String>
        lateinit var mQuoteArray: Array<String>
    }

    private var mQuoteFragment: QuotesFragment? = null
    private var mTitleFragment: TitlesFragment? = null
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mTitleFrameLayout: FrameLayout
    private lateinit var mQuotesFrameLayout: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        Log.i(TAG, "${javaClass.simpleName}: entered onCreate()")

        super.onCreate(savedInstanceState)

        // Get the string arrays with the titles and quotes
        mTitleArray = resources.getStringArray(R.array.Titles)
        mQuoteArray = resources.getStringArray(R.array.Quotes)

        setContentView(R.layout.main)

        // Get references to the TitleFragment and to the QuotesFragment containers
        mTitleFrameLayout = findViewById(R.id.title_fragment_container)
        mQuotesFrameLayout = findViewById(R.id.quote_fragment_container)


        // Get a reference to the FragmentManager
        mFragmentManager = supportFragmentManager

        mQuoteFragment =
            mFragmentManager.findFragmentById(R.id.quote_fragment_container) as QuotesFragment?
        mTitleFragment =
            mFragmentManager.findFragmentById(R.id.title_fragment_container) as TitlesFragment?


        if (null == mFragmentManager.findFragmentById(R.id.title_fragment_container)) {

            mTitleFragment = TitlesFragment()

            mTitleFragment?.let {mTitleFragment ->

                // Start a new FragmentTransaction
                val fragmentTransaction = mFragmentManager
                    .beginTransaction()


                // Add the TitleFragment to the layout
                fragmentTransaction.add(
                    R.id.title_fragment_container,
                    mTitleFragment
                )

                // Commit the FragmentTransaction
                fragmentTransaction.commit()
            }
        }

        // Set layout parameters
        setLayout()


        // Add a OnBackStackChangedListener to reset the layout when the back stack changes
        mFragmentManager
            .addOnBackStackChangedListener { layoutAfterBackStackedChanged() }
    }

    private fun layoutAfterBackStackedChanged() {

        // If backed up to single pane display uncheck any checked titles
        if (null == mFragmentManager.findFragmentById(R.id.quote_fragment_container)) {
            mTitleFragment?.unCheckSelection()
        }

        // Set layout parameters
        setLayout()
    }

    private fun setLayout() {

        // Determine whether the QuoteFragment has been created
        if (null == mFragmentManager.findFragmentById(R.id.quote_fragment_container)) {

            // Make the TitleFragment occupy the entire layout
            mTitleFrameLayout.layoutParams = LinearLayout.LayoutParams(
                MATCH_PARENT, MATCH_PARENT
            )
            mQuotesFrameLayout.layoutParams = LinearLayout.LayoutParams(
                0,
                MATCH_PARENT
            )
        } else {

            // Make the TitleLayout take 1/3 of the layout's width
            mTitleFrameLayout.layoutParams = LinearLayout.LayoutParams(
                0,
                MATCH_PARENT, 1f
            )

            // Make the QuoteLayout take 2/3's of the layout's width
            mQuotesFrameLayout.layoutParams = LinearLayout.LayoutParams(
                0,
                MATCH_PARENT, 2f
            )
        }
    }

    // Called when the user selects an item in the TitlesFragment
    override fun onListSelection(index: Int) {

        // If the QuoteFragment has not been created, create and add it now
        if (null == mFragmentManager.findFragmentById(R.id.quote_fragment_container)) {

            mQuoteFragment = QuotesFragment()

            mQuoteFragment?.let {mQuoteFragment ->
                // Start a new FragmentTransaction
                val fragmentTransaction = mFragmentManager
                    .beginTransaction()


                // Add the QuoteFragment to the layout
                fragmentTransaction.add(
                    R.id.quote_fragment_container,
                    mQuoteFragment
                )

                // Add this FragmentTransaction to the backstack
                fragmentTransaction.addToBackStack(null)

                // Commit the FragmentTransaction
                fragmentTransaction.commit()

                // Force Android to execute the committed FragmentTransaction
                mFragmentManager.executePendingTransactions()
            }
        }

        // Tell the QuoteFragment to show the quote string at position index
        mQuoteFragment?.showQuoteAtIndex(index)

    }

    override fun onDestroy() {
        Log.i(TAG, "${javaClass.simpleName}: entered onDestroy()")
        super.onDestroy()
    }

    override fun onPause() {
        Log.i(TAG, "${javaClass.simpleName}: entered onPause()")
        super.onPause()
    }

    override fun onRestart() {
        Log.i(TAG, "${javaClass.simpleName}: entered onRestart()")
        super.onRestart()
    }

    override fun onResume() {
        Log.i(TAG, "${javaClass.simpleName}: entered onResume()")
        super.onResume()
    }

    override fun onStart() {
        Log.i(TAG, "${javaClass.simpleName}: entered onStart()")
        super.onStart()
    }

    override fun onStop() {
        Log.i(TAG, "${javaClass.simpleName}: entered onStop()")
        super.onStop()
    }
}