package course.labs.todomanager.test;

import course.labs.todomanager.ToDoManagerActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;

public class TestSubmit extends
		ActivityInstrumentationTestCase2<ToDoManagerActivity> {
	private Solo solo;

	public TestSubmit() {
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
				"course.labs.todomanager.ToDoManagerActivity is not found!",
				solo.waitForActivity(
						course.labs.todomanager.ToDoManagerActivity.class, 2000));

		// Click on action bar item to delete all items
		solo.clickOnActionBarItem(0x1);

		// Click on Add New ToDo Item
		solo.clickOnView(solo.getView(course.labs.todomanager.R.id.footerView));

		// Wait for activity: 'course.labs.todomanager.AddToDoActivity'
		assertTrue(
				"course.labs.todomanager.AddToDoActivity is not found!",
				solo.waitForActivity(course.labs.todomanager.AddToDoActivity.class));

		// Hide the soft keyboard
		solo.hideSoftKeyboard();

		// Enter the text: 't1'
		solo.clearEditText((android.widget.EditText) solo
				.getView(course.labs.todomanager.R.id.title));
		solo.enterText((android.widget.EditText) solo
				.getView(course.labs.todomanager.R.id.title), "t1");

		// Hide the soft keyboard
		solo.hideSoftKeyboard();

		// Click on Done:
		solo.clickOnView(solo.getView(course.labs.todomanager.R.id.statusDone));
		// Click on Low
		solo.clickOnView(solo.getView(course.labs.todomanager.R.id.lowPriority));
		// Click on Choose Date
		solo.clickOnView(solo
				.getView(course.labs.todomanager.R.id.date_picker_button));

		// Wait for dialog
		solo.waitForDialogToOpen(10000);
		// Enter the text: 'Feb'
		solo.clearEditText((android.widget.EditText) solo
				.getView("numberpicker_input"));
		solo.enterText(
				(android.widget.EditText) solo.getView("numberpicker_input"),
				"Feb");
		// Enter the text: '28'
		solo.clearEditText((android.widget.EditText) solo.getView(
				"numberpicker_input", 1));
		solo.enterText(
				(android.widget.EditText) solo.getView("numberpicker_input", 1),
				"28");
		// Enter the text: '2014'
		solo.clearEditText((android.widget.EditText) solo.getView(
				"numberpicker_input", 2));
		solo.enterText(
				(android.widget.EditText) solo.getView("numberpicker_input", 2),
				"2014");

		// Really set the date
		solo.setDatePicker(0, 2014, 1, 28);

		// Click on Done
		solo.clickOnView(solo.getView(android.R.id.button1));

		// Click on Choose Time
		solo.clickOnView(solo
				.getView(course.labs.todomanager.R.id.time_picker_button));
		// Wait for dialog
		solo.waitForDialogToOpen(10000);
		// Enter the text: '9'
		solo.clearEditText((android.widget.EditText) solo
				.getView("numberpicker_input"));
		solo.enterText(
				(android.widget.EditText) solo.getView("numberpicker_input"),
				"9");
		// Enter the text: '19'
		solo.clearEditText((android.widget.EditText) solo.getView(
				"numberpicker_input", 1));
		solo.enterText(
				(android.widget.EditText) solo.getView("numberpicker_input", 1),
				"19");

		// Really set the time
		solo.setTimePicker(0, 9, 19);

		// Click on Done
		solo.clickOnView(solo.getView(android.R.id.button1));

		// Click on Submit
		solo.clickOnView(solo
				.getView(course.labs.todomanager.R.id.submitButton));

		// Wait for activity: 'course.labs.todomanager.ToDoManagerActivity'
		assertTrue(
				"course.labs.todomanager.ToDoManagerActivity is not found!",
				solo.waitForActivity(
						course.labs.todomanager.ToDoManagerActivity.class, 2000));

		// Click on action bar item to dump items to log
		solo.clickOnActionBarItem(0x2);
		
		assertTrue("Menu didn't close", solo.waitForDialogToClose());
		
		
		// Wait for Log Message 'Item 0:
		// Title:t1,Priority:LOW,Status:DONE,Date:2014-02-28 09:19:00'
		assertTrue(
				"Log message: 'Title:t1,Priority:LOW,Status:DONE,Date:2014-02-28 09:19:00' not found",
				solo.waitForLogMessage(
						"Title:t1,Priority:LOW,Status:DONE,Date:2014-02-28 09:19:00",
						timeout));

	}
}
