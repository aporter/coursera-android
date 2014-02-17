package course.labs.todomanager.test;

import course.labs.todomanager.ToDoManagerActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

public class TestReset extends
		ActivityInstrumentationTestCase2<ToDoManagerActivity> {
	private Solo solo;

	public TestReset() {
		super(ToDoManagerActivity.class);
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

		int timeout = 10;

		// Wait for activity: 'course.labs.todomanager.ToDoManagerActivity'
		assertTrue(
				"'course.labs.todomanager.ToDoManagerActivity' not found.",
				solo.waitForActivity(
						course.labs.todomanager.ToDoManagerActivity.class, 2000));

		// Click on action bar item
		solo.clickOnActionBarItem(0x1);

		// Click on Add New ToDo Item
		solo.clickOnView(solo.getView(course.labs.todomanager.R.id.footerView));

		// Wait for activity: 'course.labs.todomanager.AddToDoActivity'
		assertTrue(
				"course.labs.todomanager.AddToDoActivity is not found!",
				solo.waitForActivity(course.labs.todomanager.AddToDoActivity.class));

		// Hide the soft keyboard
		solo.hideSoftKeyboard();

		// Enter the text: 't2'
		solo.clearEditText((android.widget.EditText) solo
				.getView(course.labs.todomanager.R.id.title));
		solo.enterText((android.widget.EditText) solo
				.getView(course.labs.todomanager.R.id.title), "t2");

		// Hide the soft keyboard
		solo.hideSoftKeyboard();
		// Click on Done:
		solo.clickOnView(solo.getView(course.labs.todomanager.R.id.statusDone));

		// Click on Reset
		solo.clickOnView(solo.getView(course.labs.todomanager.R.id.resetButton));

		// Enter the text: 'Empty Text View'
		solo.clearEditText((android.widget.EditText) solo
				.getView(course.labs.todomanager.R.id.title));
		solo.enterText((android.widget.EditText) solo
				.getView(course.labs.todomanager.R.id.title), "");
		// Hide the soft keyboard
		solo.hideSoftKeyboard();

		// Enter the text: 't3'
		solo.clearEditText((android.widget.EditText) solo
				.getView(course.labs.todomanager.R.id.title));
		solo.enterText((android.widget.EditText) solo
				.getView(course.labs.todomanager.R.id.title), "t3");

		// Hide the soft keyboard
		solo.hideSoftKeyboard();
		// Click on High
		solo.clickOnView(solo
				.getView(course.labs.todomanager.R.id.highPriority));

		// Click on Submit
		solo.clickOnView(solo
				.getView(course.labs.todomanager.R.id.submitButton));

		// Click on Empty Text View
		assertFalse(solo.isCheckBoxChecked(0));

		// Change status to DONE
		solo.clickOnCheckBox(0);

		// Wait for activity: 'course.labs.todomanager.ToDoManagerActivity'
		assertTrue(
				"course.labs.todomanager.ToDoManagerActivity is not found!",
				solo.waitForActivity(
						course.labs.todomanager.ToDoManagerActivity.class, 2000));

		// Click on action bar item to dump items to log
		solo.clickOnActionBarItem(0x2);
		
		assertTrue("Menu didn't close", solo.waitForDialogToClose());

		// Wait for Log Message 'Item 0: Title:t3,Priority:HIGH,Status:DONE'
		assertTrue("Log message: 'Title:t3,Priority:HIGH,Status:DONE' not found",
				solo.waitForLogMessage("Title:t3,Priority:HIGH,Status:DONE",
						timeout));

	}
}
