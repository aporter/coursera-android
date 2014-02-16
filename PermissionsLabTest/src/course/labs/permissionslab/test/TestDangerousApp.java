package course.labs.permissionslab.test;

import course.labs.permissionslab.ActivityLoaderActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


public class TestDangerousApp extends ActivityInstrumentationTestCase2<ActivityLoaderActivity> {
  	private Solo solo;
  	
  	public TestDangerousApp() {
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
		
		// Wait for activity: 'course.labs.permissionslab.ActivityLoaderActivity'
		assertTrue("course.labs.permissionslab.ActivityLoaderActivity is not found!", solo.waitForActivity(course.labs.permissionslab.ActivityLoaderActivity.class));

		// Click on Bookmarks Activity
		solo.clickOnView(solo.getView(course.labs.permissionslab.R.id.start_bookmarks_button));

		// Wait for activity: 'course.labs.permissionslab.BookmarksActivity'
		assertTrue("course.labs.permissionslab.BookmarksActivity is not found!", solo.waitForActivity(course.labs.permissionslab.BookmarksActivity.class));

		// Click on Go To DangerousActivity
		solo.clickOnView(solo.getView(course.labs.permissionslab.R.id.go_to_dangerous_activity_button));
		

		// Wait for activity: 'course.labs.permissionslab.GoToDangerousActivity'
		assertTrue("course.labs.permissionslab.GoToDangerousActivity is not found!", solo.waitForActivity(course.labs.permissionslab.GoToDangerousActivity.class));
		
		// Assert that: 'This button will load a Dangerous Level activity' is shown
		assertTrue("'This button will load a Dangerous Level activity' is not shown!", solo.waitForText(java.util.regex.Pattern.quote("This button will load a Dangerous Level activity")));
		
		// Click on Start Dangerous Activity
		solo.clickOnView(solo.getView(course.labs.permissionslab.R.id.start_dangerous_activity_button));
		
		// Assert Log message created - 'Entered startBookMarksActivity()'
		assertTrue("'Entered startBookMarksActivity()' Log message not found.",
					solo.waitForLogMessage("Entered startBookMarksActivity()",timeout));

		// Assert Log message created - 'Entered startGoToDangerousActivity()'
		assertTrue("'Entered startGoToDangerousActivity()' Log message not found.",
					solo.waitForLogMessage("Entered startGoToDangerousActivity()",timeout));
		
		
		// Assert Log message created - 'Entered startDangerousActivity()'
		assertTrue("'Entered startDangerousActivity()' Log message not found.",
				solo.waitForLogMessage("Entered startDangerousActivity()",timeout));
	
	}
}
