package course.examples.fragments.actionbar


import android.content.Context
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

//Several Activity and Fragment lifecycle methods are instrumented to emit LogCat output
//so you can follow the class' lifecycle
class TitlesFragment : ListFragment() {

    companion object {

        private const val TAG = "TitlesFragment"
    }

    private var mListener: ListSelectionListener? = null
    private var mCurrIdx = ListView.INVALID_POSITION


    fun unCheckSelection() {
        if (listView.checkedItemCount > 0) {
            listView.setItemChecked(listView.checkedItemPosition, false)
        }
        mCurrIdx = ListView.INVALID_POSITION
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Set the ListSelectionListener for communicating with the QuoteViewerActivity
        mListener = context as? ListSelectionListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This Fragment will add items to the ActionBar
        setHasOptionsMenu(true)

        // Retain this Fragment across Activity Reconfigurations
        retainInstance = true
    }

    override fun onActivityCreated(savedState: Bundle?) {
        Log.i(TAG, "${javaClass.simpleName}: entered onActivityCreated()")
        super.onActivityCreated(savedState)

        // Set the list adapter for the ListView
        // Discussed in more detail in the user interface classes lesson
        listAdapter = ArrayAdapter(
            activity as Context,
            R.layout.title_item, resources.getStringArray(R.array.Titles)
        )

        // Set the list choice mode to allow only one selection at a time
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        // Inflate the options Menu using title_menu.xml
        inflater.inflate(R.menu.title_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.title_menu_item -> {

                // Show a Toast Message. Toast Messages are discussed in the lesson on user interface classes
                Toast.makeText(
                    activity?.applicationContext,
                    "This action provided by the TitlesFragment",
                    Toast.LENGTH_SHORT
                ).show()

                // return value true indicates that the menu click has been handled
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