package course.examples.ui.listlayout

import android.app.ListActivity
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast

class ListViewActivity : ListActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a new Adapter containing a list of colors
        // Set the adapter on this ListActivity's built-in ListView
        listAdapter = ArrayAdapter(
            this, R.layout.list_item,
            resources.getStringArray(R.array.colors)
        )

        val lv = listView

        // Enable filtering when the user types in the virtual keyboard
        lv.isTextFilterEnabled = true

        // Set an setOnItemClickListener on the ListView
        lv.onItemClickListener = OnItemClickListener { _, view, _, _ ->
            // Display a Toast message indicting the selected item
            Toast.makeText(
                applicationContext,
                (view as TextView).text, Toast.LENGTH_SHORT
            ).show()
        }
    }
}