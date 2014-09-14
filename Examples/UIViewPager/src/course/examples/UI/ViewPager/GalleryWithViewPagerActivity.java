// This project requires the v13 support library. 
// See http://developer.android.com/tools/support-library/setup.html for more information


package course.examples.UI.ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class GalleryWithViewPagerActivity extends Activity {

	private ImageAdapter mImageAdapter;
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mViewPager = (ViewPager) findViewById(R.id.pager);

		// Create a new ImageAdapter (subclass of FragmentStatePagerAdapter)
		// ViewPager uses support library, so use getSupportFragmentManager()
		// instead of getFragmentManager()
		mImageAdapter = new ImageAdapter(getFragmentManager());

		// Set the Adapter on the ViewPager
		mViewPager.setAdapter(mImageAdapter);

	}
}
