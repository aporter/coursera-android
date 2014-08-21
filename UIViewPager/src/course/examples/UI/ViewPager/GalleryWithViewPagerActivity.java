package course.examples.UI.ViewPager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

public class GalleryWithViewPagerActivity extends ActionBarActivity {

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
		mImageAdapter = new ImageAdapter(getSupportFragmentManager());

		// Set the Adapter on the ViewPager
		mViewPager.setAdapter(mImageAdapter);

	}
}
