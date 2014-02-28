package course.labs.notificationslab.test;

import course.labs.notificationslab.TestFrontEndActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

public class OldFeedNoNotificationTest extends
		ActivityInstrumentationTestCase2<TestFrontEndActivity> {
	private Solo solo;

	public OldFeedNoNotificationTest() {
		super(TestFrontEndActivity.class);
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

		int shortDelay = 2000;
		int longDelay = 20000;

		// Wait for activity:
		// 'course.labs.notificationslab.TestFrontEndActivity'
		solo.waitForActivity(
				course.labs.notificationslab.TestFrontEndActivity.class, 2000);

		// Click on Make Tweets Old
		solo.clickOnView(solo
				.getView(course.labs.notificationslab.R.id.age_tweets_button));

		solo.sleep(shortDelay);
		
		// Click on Start Main Activty
		solo.clickOnView(solo
				.getView(course.labs.notificationslab.R.id.start_main_button));

		// Wait for activity: 'course.labs.notificationslab.MainActivity'
		assertTrue(
				"course.labs.notificationslab.MainActivity is not found!",
				solo.waitForActivity(course.labs.notificationslab.MainActivity.class));

		solo.sleep(shortDelay);

		// Click on taylorswift13
		solo.clickOnView(solo.getView(android.R.id.text1));

		// Assert that: 'feed_view' is shown
		assertTrue("feed_view' is not shown!", solo.waitForView(solo
				.getView(course.labs.notificationslab.R.id.feed_view)));

		// Assert that: 'Please wait while we download the Tweets!' is shown
		assertTrue("'Please wait while we download the Tweets!' is not shown!",
				solo.searchText("Please wait while we download the Tweets!"));

		solo.sleep(shortDelay);

		// Press menu back key
		solo.goBack();

		// Sleep for 20 seconds while URLs are downloaded
		 solo.sleep(longDelay);

		// Click on taylorswift13
		solo.clickOnView(solo.getView(android.R.id.text1));

		// Assert that: 'feed_view' is shown
		assertTrue("feed_view not shown!", solo.waitForView(solo
				.getView(course.labs.notificationslab.R.id.feed_view)));

		// Assert that: 'Taylor Swift' is shown
		assertTrue("'Taylor Swift' is not shown!",
				solo.searchText("Taylor Swift"));

		solo.sleep(shortDelay);

		// Press menu back key
		solo.goBack();

		solo.sleep(shortDelay);

		// Click on msrebeccablack
		solo.clickOnView(solo.getView(android.R.id.text1, 1));

		// Assert that: 'feed_view' is shown
		assertTrue("'Rebecca Black' is not shown!", solo.waitForView(solo
				.getView(course.labs.notificationslab.R.id.feed_view)));

		// Assert that: 'feed_view' is shown
		assertTrue("'Rebecca Black' is not shown!",
				solo.searchText("Rebecca Black"));

		solo.sleep(shortDelay);

		// Press menu back key
		solo.goBack();

		solo.sleep(shortDelay);

		// Click on ladygaga
		solo.clickOnView(solo.getView(android.R.id.text1, 2));

		// Assert that: 'feed_view' is shown
		assertTrue("'feed_view' is not shown!", solo.waitForView(solo
				.getView(course.labs.notificationslab.R.id.feed_view)));

		// Assert that: 'Lady Gaga' is shown
		assertTrue("'Lady Gaga' is not shown!", solo.searchText("Lady Gaga"));

	}
}
