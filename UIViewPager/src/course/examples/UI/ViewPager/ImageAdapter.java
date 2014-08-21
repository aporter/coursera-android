package course.examples.UI.ViewPager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

// Manages Fragments holding ImageViews
public class ImageAdapter extends FragmentStatePagerAdapter {

	// List of IDs corresponding to the images
	private Integer[] mImageIds = { R.drawable.sample_1, R.drawable.sample_2,
			R.drawable.sample_3, R.drawable.sample_4, R.drawable.sample_5,
			R.drawable.sample_6, R.drawable.sample_7 };

	public ImageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new ImageHolderFragment();
		Bundle args = new Bundle();
		args.putInt(ImageHolderFragment.RES_ID, mImageIds[i]);
		args.putString(ImageHolderFragment.POS, String.valueOf(i));
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		return mImageIds.length;
	}

}