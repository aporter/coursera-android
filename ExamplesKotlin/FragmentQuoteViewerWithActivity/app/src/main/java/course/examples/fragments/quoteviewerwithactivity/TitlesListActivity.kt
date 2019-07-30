package course.examples.fragments.quoteviewerwithactivity

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

class TitlesListActivity : ListActivity() {


    companion object {
        const val INDEX = "index"
    }

    //private var mTitleArray: Array<String>? = null
    private lateinit var mQuoteArray: Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the string array with the quotes
        mQuoteArray = resources.getStringArray(R.array.Quotes)

        // Set the list adapter for the ListView
        // Discussed in more detail in the user interface classes lesson
        listAdapter = ArrayAdapter(
            this@TitlesListActivity,
            R.layout.list_text_item_layout, resources.getStringArray(R.array.Titles)
        )

    }

    public override fun onListItemClick(l: ListView, v: View, pos: Int, id: Long) {

        // Create implicitly Intent to start the QuoteListActivity class
        val showItemIntent = Intent(
            this@TitlesListActivity,
            QuoteListActivity::class.java
        )

        // Add an Extra representing the currently selected position
        // The name of the Extra is stored in INDEX
        showItemIntent.putExtra(INDEX, mQuoteArray[pos])

        // Start the QuoteListActivity using Activity.startActivity()
        startActivity(showItemIntent)
    }
}
