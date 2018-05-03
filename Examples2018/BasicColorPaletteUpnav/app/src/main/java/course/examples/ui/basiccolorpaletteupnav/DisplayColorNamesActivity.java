package course.examples.ui.basiccolorpaletteupnav;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class DisplayColorNamesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_color_names);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    /**
     * A placeholder fragment containing a color palette name
     */
    public static class PlaceholderFragment extends ListFragment {

        private final int[] colorArray = {
                R.array.reds, R.array.pinks,
                R.array.purples, R.array.deep_purples,
                R.array.indigo, R.array.blue,
                R.array.light_blue, R.array.cyan,
                R.array.teal, R.array.green,
                R.array.light_green, R.array.lime,
                R.array.yellow, R.array.amber,
                R.array.orange, R.array.deep_orange,
                R.array.brown, R.array.grey,
                R.array.blue_grey};

        private final String[] colorNames = {
                "Reds", "Pinks", "Purples",
                "Deep Purples", "Indigo",
                "Blue", "Light Blue",
                "Cyan", "Teal",
                "Green", "Light Green",
                "Lime", "Yellow",
                "Amber", "Orange",
                "Deep Orange", "Brown",
                "Grey", "Blue Grey"};


        /**
         * Set up the adapter for the list view
         */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getActivity(), R.layout.fragment_display_color_names, colorNames);
            setListAdapter(adapter);
        }

        /**
         * Start the DisplayColorActivity
         */
        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Intent intent = new Intent(getActivity(), DisplayColorActivity.class);
            intent.putExtra(PaletteAdapter.COLOR_EXTRA, colorArray[position]);
            startActivity(intent);
        }
    }
}

