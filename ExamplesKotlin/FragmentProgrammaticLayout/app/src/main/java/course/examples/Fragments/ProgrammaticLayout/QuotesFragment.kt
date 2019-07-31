package course.examples.Fragments.ProgrammaticLayout

import android.content.Context
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
    private var mCurrIdx = ListView.INVALID_POSITION
    private var mQuoteArrayLen: Int = 0

    // Show the Quote string at position newIndex
    internal fun showQuoteAtIndex(index: Int) {
        if (index in 0 until mQuoteArrayLen) {
            mQuoteView.text = QuoteViewerActivity.mQuoteArray[index]
            mCurrIdx = index
        } else {
            mQuoteView.text = QuoteViewerActivity.mNoQuoteSelectedString
        }
    }

    override fun onAttach(context: Context) {
        Log.i(TAG, "${javaClass.simpleName}::entered onAttach()")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "${javaClass.simpleName}::entered onCreate()")
        super.onCreate(savedInstanceState)

        retainInstance = true
    }

    // Called to create the content view for this Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i(TAG, "${javaClass.simpleName}::entered onCreateView()")

        // Inflate the layout defined in quote_fragment.xml
        // The last parameter is false because the returned view does not need to be attached to the container ViewGroup
        return inflater.inflate(R.layout.quote_fragment, container, false)
    }

    // Set up some information about the mQuoteView TextView
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(TAG, "${javaClass.simpleName}::entered onActivityCreated()")

        mQuoteView = activity!!.findViewById(R.id.quoteView)
        mQuoteArrayLen = QuoteViewerActivity.mQuoteArray.size

        showQuoteAtIndex(mCurrIdx)
    }

    override fun onStart() {
        Log.i(TAG, "${javaClass.simpleName}::entered onStart()")
        super.onStart()
    }

    override fun onResume() {
        Log.i(TAG, "${javaClass.simpleName}::entered onResume()")
        super.onResume()
    }


    override fun onPause() {
        Log.i(TAG, "${javaClass.simpleName}::entered onPause()")
        super.onPause()
    }

    override fun onStop() {
        Log.i(TAG, "${javaClass.simpleName}::entered onStop()")
        super.onStop()
    }

    override fun onDetach() {
        Log.i(TAG, "${javaClass.simpleName}::entered onDetach()")
        super.onDetach()
    }


    override fun onDestroy() {
        Log.i(TAG, "${javaClass.simpleName}::entered onDestroy()")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.i(TAG, "${javaClass.simpleName}::entered onDestroyView()")
        super.onDestroyView()
    }
}
