package course.labs.fragmentslab.test;

import course.labs.fragmentslab.MainActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

public class PhoneTest extends ActivityInstrumentationTestCase2<MainActivity> {
	private Solo solo;

	public PhoneTest() {
		super(MainActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation());
		getActivity();
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	public void testRun() {

		int timeout = 5; 
		
		// Wait for activity: 'course.labs.fragmentslab.MainActivity'
		assertTrue("MainActivity not found", solo.waitForActivity(
				course.labs.fragmentslab.MainActivity.class, 2000));
		
		// Wait for view: 'android.R.id.text1'
		assertTrue("text1 not found", solo.waitForView(android.R.id.text1));

		// Click on ladygaga
		solo.clickOnView(solo.getView(android.R.id.text1));

		assertTrue("feed_view not found", solo.waitForView(solo
				.getView(course.labs.fragmentslab.R.id.feed_view)));

		// Assert that: 'the audience cheering!' is shown
		assertTrue("'the audience cheering!' is not shown!",
				solo.searchText("the audience cheering!"));

		// Wait for onActivityCreated() Log Message:
		assertTrue("onActivityCreated() Log Message not found",
				solo.waitForLogMessage("Entered onActivityCreated()",timeout));

		// Wait for onItemSelected(0) Log Message:
		assertTrue("onItemSelected(0) Log Message not found",
				solo.waitForLogMessage("Entered onItemSelected(0)",timeout));

		// Wait for updateFeedDisplay() Log Message:
		assertTrue("updateFeedDisplay() Log Message not found",
				solo.waitForLogMessage("Entered updateFeedDisplay()",timeout));

		// Clear log
		solo.clearLog();
		
		// Press menu back key
		solo.goBack();
		
		// Wait for view: 'android.R.id.text1'
		assertTrue("text1 not found", solo.waitForView(android.R.id.text1));
		
		// Click on msrebeccablack
		solo.clickOnView(solo.getView(android.R.id.text1, 1));

		// Assert that: feed_view is shown
		assertTrue("feed_view! is not shown!", solo.waitForView(solo
				.getView(course.labs.fragmentslab.R.id.feed_view)));

		// Assert that: 'save me from school' is shown
		assertTrue("'save me from school' is not shown!",
				solo.searchText("save me from school"));

		// Wait for onActivityCreated() Log Message:
		assertTrue("onActivityCreated() Log Message not found",
				solo.waitForLogMessage("Entered onActivityCreated()",timeout));

		// Wait for Log Message:
		assertTrue("onItemSelected(1) Log Message not found",
				solo.waitForLogMessage("Entered onItemSelected(1)",timeout));

		// Wait for updateFeedDisplay() Log Message:
		assertTrue("updateFeedDisplay() Log Message not found",
				solo.waitForLogMessage("Entered updateFeedDisplay()",timeout));

		// Clear log
		solo.clearLog();

		// Press menu back key
		solo.goBack();

		// Click on taylorswift13
		solo.clickOnView(solo.getView(android.R.id.text1, 2));

		// Assert that: feed_view shown
		assertTrue("feed_view not shown", solo.waitForView(solo
				.getView(course.labs.fragmentslab.R.id.feed_view)));
		
		// Assert that: 'I love you guys so much' is shown
		assertTrue("'I love you guys so much' is not shown!",
				solo.searchText("I love you guys so much"));
		
		// Wait for onActivityCreated() Log Message:
		assertTrue("onActivityCreated() Log Message not found",
				solo.waitForLogMessage("Entered onActivityCreated()",timeout));

		// Wait for onItemSelected(2) Log Message:
		assertTrue("onItemSelected(2) Log Message not found",
				solo.waitForLogMessage("Entered onItemSelected(2)",timeout));

		// Wait for updateFeedDisplay() Log Message:
		assertTrue("updateFeedDisplay() Log Message not found",
				solo.waitForLogMessage("Entered updateFeedDisplay()",timeout));

	}
}
