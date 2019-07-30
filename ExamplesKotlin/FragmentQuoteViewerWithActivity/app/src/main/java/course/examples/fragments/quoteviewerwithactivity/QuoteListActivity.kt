package course.examples.fragments.quoteviewerwithactivity

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter

class QuoteListActivity : ListActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the Intent that started this Activity
        val intent = intent

        // Retrieve the Extra stored under the name TitlesListActivity.INDEX
        val quote = intent.getStringExtra(TitlesListActivity.INDEX)

        quote?.let {

            // Set the list adapter for the ListView
            // Discussed in more detail in the user interface classes lesson
            listAdapter = ArrayAdapter(
                this@QuoteListActivity,
                R.layout.list_text_item_layout,
                arrayOf(quote)
            )
        }
    }
}