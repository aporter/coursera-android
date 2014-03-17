package course.labs.locationlab.test;

import course.labs.locationlab.PlaceViewActivity;
import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


public class TestSameLocation extends ActivityInstrumentationTestCase2<PlaceViewActivity> {
  	private Solo solo;
  	
  	public TestSameLocation() {
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
		// Wait for activity: 'course.labs.locationlab.PlaceViewActivity'
		solo.waitForActivity(course.labs.locationlab.PlaceViewActivity.class, 2000);

		// Set default small timeout to 14342 milliseconds
		Timeout.setSmallTimeout(14342);

		// Click on action bar item
		solo.clickOnActionBarItem(course.labs.locationlab.R.id.place_one);

		solo.sleep(2000);
		
		// Click on Get New Place
		solo.clickOnView(solo.getView(course.labs.locationlab.R.id.footer));
		
		solo.sleep(2000);

		// Click on action bar item
		solo.clickOnActionBarItem(course.labs.locationlab.R.id.place_one);
		
		solo.sleep(2000);

		// Click on Get New Place
		solo.clickOnView(solo.getView(course.labs.locationlab.R.id.footer));
		
		solo.sleep(5000);
		
		// Click on action bar item
		solo.clickOnActionBarItem(course.labs.locationlab.R.id.print_badges);


	}
}
