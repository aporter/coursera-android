package course.examples.ui.autocomplete;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class AutoCompleteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String[] mCountries = getResources().getStringArray(R.array.country_names);

        // Get a reference to the AutoCompleteTextView
        final AutoCompleteTextView textView = findViewById(R.id.autocomplete_country);

        // Create an ArrayAdapter containing country names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item, mCountries);

        // Set the adapter for the AutoCompleteTextView
        textView.setAdapter(adapter);

        // Display a Toast Message when the user clicks on an item in the AutoCompleteTextView
        textView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.winner_is_string, arg0.getAdapter().getItem(arg2)),
                        Toast.LENGTH_LONG).show();
                textView.setText("");
            }
        });
    }
}