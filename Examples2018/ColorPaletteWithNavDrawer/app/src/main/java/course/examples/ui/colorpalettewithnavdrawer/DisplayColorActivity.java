package course.examples.ui.colorpalettewithnavdrawer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class DisplayColorActivity extends Activity {

    private final int[] colorArray = {
            R.array.reds, R.array.pinks, R.array.purples,
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
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_color_activity);

        // Create the adapter for the color palettes
        ColorPaletteAdapter colorPaletteAdapter = new ColorPaletteAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(colorPaletteAdapter);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(
                this,
                R.layout.drawer_list_item,
                colorNames));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    /**
     * Handle click on the NavigationDrawer
     */
    private void selectItem(int position) {

        // Highlight the selected item
        mDrawerList.setItemChecked(position, true);

        // Set the ViewPager's item to the currently selected position
        mViewPager.setCurrentItem(position);

        // Close the NavigationDrawer
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    /*
     * The ListFragment that displays the color view for the current color palette.
     */
    public static class ColorPaletteFragment extends ListFragment {
        private int mResourceID;

        public static ColorPaletteFragment newInstance(int resourceID) {
            ColorPaletteFragment fragment = new ColorPaletteFragment();
            fragment.mResourceID = resourceID;
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setRetainInstance(true);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            PaletteNameAdapter adapter = new PaletteNameAdapter(
                    getActivity(),
                    R.layout.color_view,
                    mResourceID);
            setListAdapter(adapter);
        }
    }

    /**
     * Listener that responds to clicks on the NavigationDrawer
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Adapter for color palette color view
     */
    public class ColorPaletteAdapter extends FragmentPagerAdapter {
        public ColorPaletteAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * getItem() is called to instantiate the fragment for the given color palette.
         * Returns a PlaceholderFragment (defined as a static inner class below).
         */
        @Override
        public Fragment getItem(int position) {
            return ColorPaletteFragment.newInstance(colorArray[position]);
        }

        @Override
        public int getCount() {
            return colorArray.length;
        }

        /*
         * Returns the title (name) for the current color palette.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return colorNames[position];
        }
    }
}
