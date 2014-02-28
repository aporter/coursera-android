package course.labs.notificationslab.test;

import course.labs.notificationslab.TestFrontEndActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

public class OldFeedWithNotificationTest extends
		ActivityInstrumentationTestCase2<TestFrontEndActivity> {
	private Solo solo;

	public OldFeedWithNotificationTest() {
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

		solo.sleep(shortDelay);

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

		// Assert that: 'Please wait while we download the Tweets!' is shown
		assertTrue("'Please wait while we download the Tweets!' is not shown!",
				solo.waitForView(solo
						.getView(course.labs.notificationslab.R.id.feed_view)));

		solo.sleep(shortDelay);

		// Press menu back key
		solo.goBack();

		solo.sleep(shortDelay);

		// Press menu back key
		solo.goBack();

		// Wait for activity:
		// 'course.labs.notificationslab.TestFrontEndActivity'
		assertTrue(
				"course.labs.notificationslab.TestFrontEndActivity is not found!",
				solo.waitForActivity(course.labs.notificationslab.TestFrontEndActivity.class));

		// Sleep while twitter feed loads
		solo.sleep(longDelay);

	}
}
