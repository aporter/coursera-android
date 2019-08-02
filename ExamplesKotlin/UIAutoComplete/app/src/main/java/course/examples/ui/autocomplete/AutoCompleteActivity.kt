package course.examples.ui.autocomplete

import android.app.Activity
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast

class AutoCompleteActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Get a reference to the AutoCompleteTextView
        val textView =
            findViewById<AutoCompleteTextView>(R.id.autocomplete_country)

        // Create an ArrayAdapter containing country names
        val adapter = ArrayAdapter(
            this,
            R.layout.list_item,
            resources.getStringArray(R.array.country_names)
        )

        // Set the adapter for the AutoCompleteTextView
        textView.setAdapter(adapter)

        // Display a Toast Message when the user clicks on an item in the AutoCompleteTextView
        textView.onItemClickListener =
            OnItemClickListener { parent, _, position, _ ->
                val selection = parent.adapter.getItem(position) as String
                Toast.makeText(
                    applicationContext,
                    getString(R.string.winner_is_string, selection),
                    Toast.LENGTH_LONG
                ).show()
                textView.setText(selection)
            }
    }
}