package course.labs.permissionslab.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import course.labs.permissionslab.ActivityLoaderActivity;

public class TestBookmarks extends
		ActivityInstrumentationTestCase2<ActivityLoaderActivity> {
	private Solo solo;

	public TestBookmarks() {
		super(ActivityLoaderActivity.class);
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
		
		// Wait for activity:
		// 'course.labs.permissionslab.ActivityLoaderActivity'
		assertTrue(
				"course.labs.permissionslab.ActivityLoaderActivity is not found!",
				solo.waitForActivity(course.labs.permissionslab.ActivityLoaderActivity.class));

		// Click on Bookmarks Activity
		solo.clickOnView(solo
				.getView(course.labs.permissionslab.R.id.start_bookmarks_button));

		// Wait for activity: 'course.labs.permissionslab.BookmarksActivity'
		assertTrue(
				"course.labs.permissionslab.BookmarksActivity is not found!",
				solo.waitForActivity(course.labs.permissionslab.BookmarksActivity.class));

		// Click on Get Bookmarks
		solo.clickOnView(solo
				.getView(course.labs.permissionslab.R.id.get_bookmarks_button));

		// Check for at least one bookmark
		assertTrue("'www.google.com' is not displayed!",
				solo.waitForText("http"));

		// Assert Log message created - 'Entered startBookMarksActivity()'
		assertTrue("'Entered startBookMarksActivity()' Log message not found.",
				solo.waitForLogMessage("Entered startBookMarksActivity()",timeout));

		// Assert Log message created - 'Entered loadBookmarks()'
		assertTrue("'Entered loadBookmarks()' Log message not found.",
				solo.waitForLogMessage("Entered loadBookmarks()",timeout));

		// Assert Log message created - 'Bookmarks loaded'
		assertTrue("'Bookmarks loaded' Log message not found.",
				solo.waitForLogMessage("Bookmarks loaded",timeout));

	}
}
