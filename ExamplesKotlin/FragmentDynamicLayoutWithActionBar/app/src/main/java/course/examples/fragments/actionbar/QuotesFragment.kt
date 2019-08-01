package course.examples.fragments.actionbar


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

class QuotesFragment : Fragment() {

    companion object {

        private const val TAG = "QuotesFragment"
        private lateinit var mQuoteArray: Array<String>
    }

    private lateinit var mQuoteView: TextView
    private var mCurrIdx = ListView.INVALID_POSITION

    // Show the Quote string at position newIndex
    internal fun showQuoteAtIndex(index: Int) {
        if (index >= 0 && index < mQuoteArray.size) {
            mQuoteView.text = mQuoteArray[index]
            mCurrIdx = index
        }
    }

    // Called to create the content view for this Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout defined in quote_fragment.xml
        // The last parameter is false because the returned view does not need to be attached to the container ViewGroup
        return inflater.inflate(R.layout.quote_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Don't destroy Fragment on reconfiguration
        retainInstance = true

        // This Fragment adds options to the ActionBar
        setHasOptionsMenu(true)

    }

    // Set up some information about the mQuoteView TextView
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mQuoteView = activity!!.findViewById(R.id.quoteView)
        mQuoteArray = resources.getStringArray(R.array.Quotes)

        showQuoteAtIndex(mCurrIdx)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        // Inflate the options Menu using quote_menu.xml
        inflater.inflate(R.menu.quote_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            // Show Toast Messages. Toast Messages are discussed in the lesson on user interface classes
            // return value true indicates that the menu click has been handled

            R.id.detail_menu_item_main -> {
                Toast.makeText(
                    activity!!.applicationContext,
                    "This action provided by the QuoteFragment",
                    Toast.LENGTH_SHORT
                ).show()
                return true
            }

            R.id.detail_menu_item_secondary -> {
                Toast.makeText(
                    activity!!.applicationContext,
                    "This action is also provided by the QuoteFragment",
                    Toast.LENGTH_SHORT
                ).show()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        Log.i(TAG, "${javaClass.simpleName}: entered onStart()")
        super.onStart()
    }

    override fun onResume() {
        Log.i(TAG, "${javaClass.simpleName}: entered onResume()")
        super.onResume()
    }


    override fun onPause() {
        Log.i(TAG, "${javaClass.simpleName}: entered onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.i(TAG, "${javaClass.simpleName}: entered onStop()")
        super.onStop()
    }

    override fun onDetach() {
        Log.i(TAG, "${javaClass.simpleName}: entered onDetach()")
        super.onDetach()
    }


    override fun onDestroy() {
        Log.i(TAG, "${javaClass.simpleName}: entered onDestroy()")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.i(TAG, "${javaClass.simpleName}: entered onDestroyView()")
        super.onDestroyView()
    }
}