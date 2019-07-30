package course.examples.fragments.staticlayout

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

// Note: This implementation does not handle reconfigurations

class TitlesFragment : ListFragment() {

    companion object {
        private const val TAG = "TitlesFragment"
    }

    private var mCurrIdx = ListView.INVALID_POSITION
    private lateinit var mListener: ListSelectionListener

    // Called when the user selects an item from the List
    override fun onListItemClick(l: ListView?, v: View?, pos: Int, id: Long) {

        if (pos != mCurrIdx) {
            // Indicates the selected item has been checked
            listView.setItemChecked(pos, true)
            mCurrIdx = pos

            // Inform the QuoteViewerActivity that the item in position pos has been selected
            mListener.onListSelection(pos)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG, "${javaClass.simpleName}: entered onAttach()")
        try {

            // Set the ListSelectionListener for communicating with the QuoteViewerActivity
            mListener = context as ListSelectionListener

        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnArticleSelectedListener")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "${javaClass.simpleName}: entered onCreate()")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "${javaClass.simpleName}: entered onCreateView()")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedState: Bundle?) {
        super.onActivityCreated(savedState)
        Log.i(TAG, "${javaClass.simpleName}: entered onActivityCreated()")

        // Set the list choice mode to allow only one selection at a time
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        // Set the list adapter for the ListView
        // Discussed in more detail in the user interface classes lesson
        listAdapter = ArrayAdapter(
            activity as Context,
            R.layout.title_item,
            QuoteViewerActivity.mTitleArray
        )
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