package course.labs.activitylab.test;

import course.labs.activitylab.ActivityOne;
import com.robotium.solo.*;

import android.test.ActivityInstrumentationTestCase2;


public class Test3 extends ActivityInstrumentationTestCase2<ActivityOne> {
  	private Solo solo;
  	
  	public Test3() {
		super(ActivityOne.class);
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
		// Wait for activity: 'course.labs.activitylab.ActivityOne'
		assertTrue("course.labs.activitylab.ActivityOne is not found!", solo.waitForActivity(course.labs.activitylab.ActivityOne.class));


		// Check for proper counts
		assertTrue("onCreate() count was off.", solo.searchText("onCreate\\(\\) calls: 1"));
		assertTrue("onStart() count was off.", solo.searchText("onStart\\(\\) calls: 1"));
		assertTrue("onResume() count was off.", solo.searchText("onResume\\(\\) calls: 1"));
		assertTrue("onRestart() count was off.", solo.searchText("onRestart\\(\\) calls: 0"));
		
		assertTrue("Log message for 'onCreate()' was not found in the Log", solo.waitForLogMessage("onCreate",5));
		assertTrue("Log message for 'onStart()' was not found in Log", solo.waitForLogMessage("onStart",5));
		assertTrue("Log message for 'onResume()' was not found in Log", solo.waitForLogMessage("onResume",5));

		solo.clearLog();
		
		// Click on Start Activity Two
		solo.clickOnView(solo.getView(course.labs.activitylab.R.id.bLaunchActivityTwo));
		// Wait for activity: 'course.labs.activitylab.ActivityTwo'
		assertTrue("course.labs.activitylab.ActivityTwo is not found!", solo.waitForActivity(course.labs.activitylab.ActivityTwo.class));
		
		// Check for proper counts
		assertTrue("onCreate() count was off.", solo.searchText("onCreate\\(\\) calls: 1"));
		assertTrue("onStart() count was off.", solo.searchText("onStart\\(\\) calls: 1"));
		assertTrue("onResume() count was off.", solo.searchText("onResume\\(\\) calls: 1"));
		assertTrue("onRestart() count was off.", solo.searchText("onRestart\\(\\) calls: 0"));

		assertTrue("Log message for 'onPause()' was not found in the Log", solo.waitForLogMessage("onPause",5));
		assertTrue("Log message for 'onCreate()' was not found in the Log", solo.waitForLogMessage("onCreate",5));
		assertTrue("Log message for onStart()'  was not found in the Log", solo.waitForLogMessage("onStart",5));
		assertTrue("Log message for onResume()' was not found in the Log", solo.waitForLogMessage("onResume",5));

		solo.clearLog();
		
		// Click on Close Activity
		solo.clickOnView(solo.getView(course.labs.activitylab.R.id.bClose));

		// Wait for activity: 'course.labs.activitylab.ActivityOne'
		assertTrue("course.labs.activitylab.ActivityOne is not found!", solo.waitForActivity(course.labs.activitylab.ActivityOne.class));

		// Check for proper counts
		assertTrue("onCreate() count was off.", solo.searchText("onCreate\\(\\) calls: 1"));
		assertTrue("onStart() count was off.", solo.searchText("onStart\\(\\) calls: 2"));
		assertTrue("onResume() count was off.", solo.searchText("onResume\\(\\) calls: 2"));
		assertTrue("onRestart() count was off.", solo.searchText("onRestart\\(\\) calls: 1"));
		
		assertTrue("Log message for 'onPause()' was not found in the Log", solo.waitForLogMessage("onPause",5));
		assertTrue("Log message for onRestart()'  was not found in the Log", solo.waitForLogMessage("onRestart",5));
		assertTrue("Log message for onStart()'  was not found in the Log", solo.waitForLogMessage("onStart",5));
		assertTrue("Log message for onResume()' was not found in the Log", solo.waitForLogMessage("onResume",5));

		solo.clearLog();
		
		// Click on Start Activity Two
		solo.clickOnView(solo.getView(course.labs.activitylab.R.id.bLaunchActivityTwo));
		// Wait for activity: 'course.labs.activitylab.ActivityTwo'
		assertTrue("course.labs.activitylab.ActivityTwo is not found!", solo.waitForActivity(course.labs.activitylab.ActivityTwo.class));

		// Check for proper counts
		assertTrue("onCreate() count was off.", solo.searchText("onCreate\\(\\) calls: 1"));
		assertTrue("onStart() count was off.", solo.searchText("onStart\\(\\) calls: 1"));
		assertTrue("onResume() count was off.", solo.searchText("onResume\\(\\) calls: 1"));
		assertTrue("onRestart() count was off.", solo.searchText("onRestart\\(\\) calls: 0"));

		assertTrue("Log message for 'onPause()' was not found in the Log", solo.waitForLogMessage("onPause",5));
		assertTrue("Log message for onCreate()' was not found in the Log", solo.waitForLogMessage("onCreate",5));
		assertTrue("Log message for onStart()'  was not found in the Log", solo.waitForLogMessage("onStart",5));
		assertTrue("Log message for onResume()' was not found in the Log", solo.waitForLogMessage("onResume",5));

	}
}
