package course.examples.permissionexample.boom;

import android.app.Activity;
import android.os.Bundle;

public class BoomActivity extends Activity {

	// String used to represent the dangerous operation
	public static final String ACTION_BOOM = 
			"course.examples.permissionexample.boom.boom_action";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boom_layout);
	}
}
