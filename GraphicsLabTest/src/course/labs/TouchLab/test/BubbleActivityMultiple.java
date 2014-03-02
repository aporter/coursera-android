package course.labs.TouchLab.test;

import course.labs.GraphicsLab.BubbleActivity;

import com.robotium.solo.*;
import android.test.ActivityInstrumentationTestCase2;


public class BubbleActivityMultiple extends ActivityInstrumentationTestCase2<BubbleActivity> {
  	private Solo solo;
  	
  	public BubbleActivityMultiple() {
		super(BubbleActivity.class);
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
		// Wait for activity: 'course.labs.TouchLab.BubbleActivity'
		solo.waitForActivity(course.labs.GraphicsLab.BubbleActivity.class, 2000);

		solo.sleep(1000);

		// Click on action bar item
		solo.clickOnActionBarItem(0x1);
		
		solo.sleep(1000);
		
		// Click to create a bubble
		solo.clickOnScreen(250, 250);
		
		solo.sleep(1000);
	
		// Click to remove the same bubble
		solo.clickOnScreen(500, 500);
		
		solo.sleep(1000);

	}
}
