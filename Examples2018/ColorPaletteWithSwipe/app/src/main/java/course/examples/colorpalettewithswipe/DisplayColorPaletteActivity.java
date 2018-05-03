package course.examples.colorpalettewithswipe;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


public class DisplayColorPaletteActivity extends Activity {

    /**
     * Provides a fragment for each color palette.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The ViewPager hosting the color palettes.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_palette_layout);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final int[] colorArray = {R.array.reds,
                R.array.pinks, R.array.purples,
                R.array.deep_purples, R.array.indigo,
                R.array.blue, R.array.light_blue,
                R.array.cyan, R.array.teal,
                R.array.green, R.array.light_green,
                R.array.lime, R.array.yellow,
                R.array.amber, R.array.orange,
                R.array.deep_orange, R.array.brown,
                R.array.grey, R.array.blue_grey};

        private final String[] colorNames = {
                "Reds", "Pinks",
                "Purples", "Deep Purples",
                "Indigo", "Blue",
                "Light Blue", "Cyan",
                "Teal", "Green",
                "Light Green", "Lime",
                "Yellow", "Amber",
                "Orange", "Deep Orange",
                "Brown", "Grey",
                "Blue Grey"};


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // Called to instantiate the fragment for the given page.
        // Returns a PlaceholderFragment
        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(colorArray[position]);
        }

        @Override
        public int getCount() {
            return colorArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return colorNames[position];
        }
    }

    /**
     * A placeholder fragment containing a color palette.
     */
    public static class PlaceholderFragment extends ListFragment {
        /**
         * The fragment argument representing the color palette.
         */

        private int mResourceID;

        /**
         * Returns a new instance of this fragment for the given color palette
         */
        public static PlaceholderFragment newInstance(int resourceID) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            fragment.mResourceID = resourceID;
            return fragment;
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
