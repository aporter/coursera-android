package course.examples.fragments.staticconfiglayout

import android.content.Context
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

//Several Activity and Fragment lifecycle methods are instrumented to emit LogCat output
//so you can follow the class' lifecycle

class TitlesFragment : ListFragment() {

    companion object {

        private const val TAG = "TitlesFragment"
    }

    private var mListener: ListSelectionListener? = null
    private var mCurrIdx = ListView.INVALID_POSITION


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, javaClass.simpleName + ":onAttach()")

        // Set the ListSelectionListener for communicating with the QuoteViewerActivity
        mListener = context as? ListSelectionListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, javaClass.simpleName + ":onCreate()")

        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, javaClass.simpleName + ":onCreateView()")

        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onActivityCreated(savedState: Bundle?) {
        super.onActivityCreated(savedState)
        Log.i(TAG, javaClass.simpleName + ":onActivityCreated()")

        // Set the list choice mode to allow only one selection at a time
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Set the list adapter for the ListView
        // Discussed in more detail in the user interface classes lesson
        listAdapter = ArrayAdapter(
            activity as Context,
            R.layout.list_item, QuoteViewerActivity.mTitleArray
        )
    }

    // Called when the user selects an item from the List
    override fun onListItemClick(l: ListView, v: View, pos: Int, id: Long) {
        if (mCurrIdx != pos) {
            mCurrIdx = pos
            // Indicates the selected item has been checked
            l.setItemChecked(mCurrIdx, true)
            // Inform the QuoteViewerActivity that the item in position pos has been selected
            mListener?.onListSelection(mCurrIdx)
        }
    }

    override fun onDestroy() {
        Log.i(TAG, javaClass.simpleName + ":onDestroy()")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.i(TAG, javaClass.simpleName + ":onDestroyView()")
        super.onDestroyView()
    }

    override fun onDetach() {
        Log.i(TAG, javaClass.simpleName + ":onDetach()")
        super.onDetach()
    }

    override fun onPause() {
        Log.i(TAG, javaClass.simpleName + ":onPause()")
        super.onPause()
    }

    override fun onResume() {
        Log.i(TAG, javaClass.simpleName + ":onResume()")
        super.onResume()
    }

    override fun onStart() {
        Log.i(TAG, javaClass.simpleName + ":onStart()")
        super.onStart()
    }

    override fun onStop() {
        Log.i(TAG, javaClass.simpleName + ":onStop()")
        super.onStop()
    }
}