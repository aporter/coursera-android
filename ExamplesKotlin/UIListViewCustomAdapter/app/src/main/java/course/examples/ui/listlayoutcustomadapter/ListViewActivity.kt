package course.examples.ui.listlayoutcustomadapter

import android.app.ListActivity
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import android.widget.Toast

class ListViewActivity : ListActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a new Adapter containing a list of colors
        // Set the adapter on this ListActivity's built-in ListView
        listAdapter = ListViewAdapter(
            this,
            R.layout.list_item,
            resources.getStringArray(R.array.colors)
        )

        listView.setBackgroundColor(resources.getColor(R.color.divider, null))
        // Enable filtering when the user types in the virtual keyboard
        listView.isTextFilterEnabled = true

        // Set an setOnItemClickListener on the ListView
        listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val textView = view.findViewById<TextView>(R.id.text)
            // Display a Toast message indicting the selected item
            Toast.makeText(
                this@ListViewActivity,
                textView.text, Toast.LENGTH_SHORT
            ).show()
        }
    }
}