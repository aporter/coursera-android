package course.examples.ui.tablayout;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

public class TabLayoutActivity extends Activity {

	private static final String ANIMALS_TABSTRING = "Animals";
	private static final String FLOWERS_TABSTRING = "Flowers";
	protected static final String THUMBNAIL_IDS = "thumbNailIDs";

	// List of Flower thumbnail images
	private ArrayList<Integer> mThumbIdsFlowers = new ArrayList<Integer>(
			Arrays.asList(R.drawable.image1, R.drawable.image2,
					R.drawable.image3, R.drawable.image4, R.drawable.image5,
					R.drawable.image6, R.drawable.image7, R.drawable.image8,
					R.drawable.image9, R.drawable.image10, R.drawable.image11,
					R.drawable.image12));

	// List of Animal thumbnail images
	private ArrayList<Integer> mThumbIdsAnimals = new ArrayList<Integer>(
			Arrays.asList(R.drawable.sample_1, R.drawable.sample_2,
					R.drawable.sample_3, R.drawable.sample_4,
					R.drawable.sample_5, R.drawable.sample_6,
					R.drawable.sample_7, R.drawable.sample_0));

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// Put ActionBar in Tab Mode
		final ActionBar tabBar = getActionBar();
		tabBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create a GridFragment for the Flower thumbnails
		GridFragment flowerFrag = new GridFragment();

		// Store the list of thumbnails as an argument to the GridFragment
		Bundle args = new Bundle();
		args.putIntegerArrayList(THUMBNAIL_IDS, mThumbIdsFlowers);
		flowerFrag.setArguments(args);

		// Configure a tab for the Flower thumbnail GridFragment
		tabBar.addTab(tabBar.newTab().setText(FLOWERS_TABSTRING)
				.setTabListener(new TabListener(flowerFrag)));

		// Create a GridFragment for the Animal thumbnails
		GridFragment animalFrag = new GridFragment();

		// Store the list of thumbnails as an argument to the GridFragment
		args = new Bundle();
		args.putIntegerArrayList(THUMBNAIL_IDS, mThumbIdsAnimals);
		animalFrag.setArguments(args);

		// Configure a tab for the Animal thumbnail GridFragment
		tabBar.addTab(tabBar.newTab().setText(ANIMALS_TABSTRING)
				.setTabListener(new TabListener(animalFrag)));

	}

	// This class handles user interaction with the tabs
	public static class TabListener implements ActionBar.TabListener {
		private static final String TAG = "TabListener";
		private final Fragment mFragment;

		public TabListener(Fragment fragment) {
			mFragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}

		// When a tab is selected, change the currently visible Fragment
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Log.i(TAG, "onTabSelected called");

			if (null != mFragment) {
				ft.replace(R.id.fragment_container, mFragment);
			}
		}

		// When a tab is unselected, remove the currently visible Fragment
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			Log.i(TAG, "onTabUnselected called");

			if (null != mFragment)
				ft.remove(mFragment);
		}
	}
}
