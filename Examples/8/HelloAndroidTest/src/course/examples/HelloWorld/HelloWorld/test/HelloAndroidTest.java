package course.examples.HelloWorld.HelloWorld.test;

// Taken from Hello Testing Tutorial
// http://developer.android.com/resources/tutorials/testing/helloandroid_test.html

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import course.examples.HelloWorld.HelloWorld.HelloAndroid;

public class HelloAndroidTest extends
		ActivityInstrumentationTestCase2<HelloAndroid> {

	private HelloAndroid mActivity;
	private TextView mView;
	private String resourceString;

	public HelloAndroidTest() {
		super("course.examples.HelloWorld.HelloWorld", HelloAndroid.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = this.getActivity();
		mView = (TextView) mActivity
				.findViewById(course.examples.HelloWorld.HelloWorld.R.id.textview);
		resourceString = mActivity
				.getString(course.examples.HelloWorld.HelloWorld.R.string.hello);
	}

	public void testPreconditions() {
		assertNotNull(mView);
	}

	public void testText() {
	      assertEquals(resourceString,(String)mView.getText());
	    }
	
	
}
