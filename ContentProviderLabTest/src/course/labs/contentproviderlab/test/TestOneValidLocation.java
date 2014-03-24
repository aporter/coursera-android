package course.labs.contentproviderlab.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import course.labs.contentproviderlab.PlaceViewActivity;


public class TestOneValidLocation extends ActivityInstrumentationTestCase2<PlaceViewActivity> {
  	private Solo solo;
  	
  	public TestOneValidLocation() {
		super(PlaceViewActivity.class);
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
		// Wait for activity: 'course.labs.contentproviderlab.PlaceViewActivity'
		solo.waitForActivity(course.labs.contentproviderlab.PlaceViewActivity.class, 4000);

		solo.sleep(2000);

		// Click on action bar item
		solo.clickOnActionBarItem(course.labs.contentproviderlab.R.id.delete_badges);

		solo.sleep(2000);

		// Click on action bar item
		solo.clickOnActionBarItem(course.labs.contentproviderlab.R.id.place_one);

		solo.sleep(2000);
		
		// Click on Get New Place
		solo.clickOnView(solo.getView(course.labs.contentproviderlab.R.id.footer));
		
		solo.sleep(5000);
		
		// Click on action bar item
		solo.clickOnActionBarItem(course.labs.contentproviderlab.R.id.print_badges);

	}
}
