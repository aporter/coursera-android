package course.examples.ui.spinner

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class SpinnerActivity : Activity() {

    companion object {

        private var wasTouched: Boolean = false
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        // Indicates whether spinner was touched by user
        wasTouched = false

        // Get a reference to the Spinner
        val spinner = findViewById<Spinner>(R.id.spinner)

        // Create an Adapter that holds a list of colors
        val adapter = ArrayAdapter.createFromResource(
            this, R.array.colors, R.layout.dropdown_item
        )

        // Set the Adapter for the spinner
        spinner.adapter = adapter

        // Set an onTouchListener on the spinner because
        // onItemSelected() can be called multiple times by framework
        spinner.setOnTouchListener { v: View, _ ->
            wasTouched = true
            v.performClick()
            false
        }

        // Set an onItemSelectedListener on the spinner
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View,
                pos: Int, id: Long
            ) {

                if (wasTouched) {

                    // Display a Toast message indicating the currently selected item
                    Toast.makeText(
                        parent.context,
                        "The color is " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    wasTouched = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}