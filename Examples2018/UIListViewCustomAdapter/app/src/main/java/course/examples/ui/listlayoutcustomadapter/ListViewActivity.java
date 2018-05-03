package course.examples.ui.listlayoutcustomadapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewActivity extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a new Adapter containing a list of colors
        // Set the adapter on this ListActivity's built-in ListView
        setListAdapter(new ListViewAdapter(
                this,
                R.layout.list_item,
                getResources().getStringArray(R.array.colors)));

        // Enable filtering when the user types in the virtual keyboard
        getListView().setTextFilterEnabled(true);

        // Set an setOnItemClickListener on the ListView
        getListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                TextView textView = view.findViewById(R.id.text);
                // Display a Toast message indicting the selected item
                Toast.makeText(ListViewActivity.this,
                        textView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}