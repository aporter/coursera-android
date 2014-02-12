package course.labs.intentslab.test;

import course.labs.intentslab.ActivityLoaderActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

public class ExplicitTest extends
		ActivityInstrumentationTestCase2<ActivityLoaderActivity> {
	private Solo solo;

	public ExplicitTest() {
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

		// Wait for activity: 'course.labs.intentslab.ActivityLoaderActivity'
		assertTrue(
				"course.labs.intentslab.ActivityLoaderActivity is not found!",
				solo.waitForActivity(course.labs.intentslab.ActivityLoaderActivity.class));

		// Click on Explicit Activation
		solo.clickOnView(solo
				.getView(course.labs.intentslab.R.id.explicit_activation_button));

		// Wait for activity: 'course.labs.intentslab.ExplicitlyLoadedActivity'
		assertTrue(
				"course.labs.intentslab.ExplicitlyLoadedActivity is not found!",
				solo.waitForActivity(course.labs.intentslab.ExplicitlyLoadedActivity.class));
		// Hide the soft keyboard
		solo.hideSoftKeyboard();
		// Enter the text: 'test'
		solo.clearEditText((android.widget.EditText) solo
				.getView(course.labs.intentslab.R.id.editText));
		solo.enterText((android.widget.EditText) solo
				.getView(course.labs.intentslab.R.id.editText), "test");
		// Hide the soft keyboard
		solo.hideSoftKeyboard();
		// Click on Enter
		solo.clickOnView(solo.getView(course.labs.intentslab.R.id.enter_button));

		// Assert that: 'textView1' is shown
		assertTrue("textView1 is not shown!", solo.waitForView(solo
				.getView(course.labs.intentslab.R.id.textView1)));
		// assert that the string 'test' is found on the display
		assertTrue("'test' is not displayed!", solo.searchText("test"));

		// Assert that: Log Message 'Entered startExplicitActivation() is shown
		assertTrue(
				"Log message - 'Entered startExplicitActivation()' is not shown!",
				solo.waitForLogMessage("Entered startExplicitActivation()",
						timeout));
		// Assert that: Log Message 'Entered enterClicked()' is shown
		assertTrue("Log message - 'Entered enterClicked()' is not shown",
				solo.waitForLogMessage("Entered enterClicked()", timeout));

		// Assert that: Log Message 'Entered onActivityResult()' is shown
		assertTrue("Log message - 'Entered onActivityResult()",
				solo.waitForLogMessage("Entered onActivityResult()", timeout));

	}
}
