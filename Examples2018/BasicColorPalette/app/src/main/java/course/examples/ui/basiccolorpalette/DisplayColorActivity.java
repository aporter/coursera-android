package course.examples.ui.basiccolorpalette;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;


public class DisplayColorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_color);

        if (savedInstanceState == null) {
            PlaceholderFragment fragment =
                    PlaceholderFragment.newInstance(getIntent().getIntExtra(
                            PaletteAdapter.COLOR_EXTRA,
                            PaletteAdapter.NO_COLOR));
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    /**
     * A placeholder fragment containing a color palette.
     */
    public static class PlaceholderFragment extends ListFragment {

        /**
         * The fragment argument representing the color palette for this fragment.
         */
        private int mResourceID;

        /**
         * Returns a new instance of this fragment for the given color palette.
         */
        public static PlaceholderFragment newInstance(int resourceID) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.mResourceID = resourceID;
            return fragment;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setRetainInstance(true);
        }

        /**
         * Create and set the PaletteAdapter for the selected color palette.
         */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            PaletteAdapter adapter = new PaletteAdapter(getActivity(),
                    R.layout.color_view, mResourceID);
            setListAdapter(adapter);

        }
    }
}

