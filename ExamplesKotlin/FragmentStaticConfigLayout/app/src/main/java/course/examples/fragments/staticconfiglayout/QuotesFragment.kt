package course.examples.fragments.staticconfiglayout

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView

//Several Activity and Fragment lifecycle methods are instrumented to emit LogCat output
//so you can follow the class' lifecycle
class QuotesFragment : Fragment() {

    companion object {

        private const val TAG = "QuotesFragment"
    }

    private lateinit var mQuoteView: TextView
    private var mQuoteArrayLen: Int = 0
    private var mCurrIdx = ListView.INVALID_POSITION

    // Show the Quote string at position newIndex
    fun showQuoteAtIndex(index: Int) {
        if (index in 0 until mQuoteArrayLen) {
            mQuoteView.text = QuoteViewerActivity.mQuoteArray[index]
            mCurrIdx = index
        } else {
            mQuoteView.text = QuoteViewerActivity.mNoQuoteSelectedString
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "${javaClass.simpleName}: onCreate()")
        super.onCreate(savedInstanceState)

        // Retain this Fragment across Activity reconfigurations
        retainInstance = true

    }

    // Called to create the content view for this Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "${javaClass.simpleName}: onCreateView()")

        // Inflate the layout defined in quote_fragment.xml
        // The last parameter is false because the returned view
        // does not need to be attached to the container ViewGroup
        return inflater.inflate(R.layout.quote_fragment, container, false)
    }

    // Set up some information about the mQuoteView TextView
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "${javaClass.simpleName}: onActivityCreated()")
        super.onActivityCreated(savedInstanceState)

        mQuoteView = activity!!.findViewById(R.id.quoteView)
        mQuoteArrayLen = QuoteViewerActivity.mQuoteArray.size

        showQuoteAtIndex(mCurrIdx)

    }

    override fun onAttach(context: Context) {
        Log.i(TAG, "${javaClass.simpleName}: onAttach()")
        super.onAttach(context)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.i(TAG, "${javaClass.simpleName}: onConfigurationChanged()")
        super.onConfigurationChanged(newConfig)
    }

    override fun onDestroy() {
        Log.i(TAG, "${javaClass.simpleName}: onDestroy()")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.i(TAG, "${javaClass.simpleName}: onDestroyView()")
        super.onDestroyView()
    }

    override fun onDetach() {
        Log.i(TAG, "${javaClass.simpleName}: onDetach()")
        super.onDetach()
    }

    override fun onPause() {
        Log.i(TAG, "${javaClass.simpleName}: onPause()")
        super.onPause()
    }

    override fun onResume() {
        Log.i(TAG, "${javaClass.simpleName}: onResume()")
        super.onResume()
    }

    override fun onStart() {
        Log.i(TAG, "${javaClass.simpleName}: onStart()")
        super.onStart()
    }

    override fun onStop() {
        Log.i(TAG, "${javaClass.simpleName}: onStop()")
        super.onStop()
    }
}
