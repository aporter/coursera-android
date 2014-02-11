package course.labs.intentslab;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityLoaderActivity extends Activity {

	static private final int GET_TEXT_REQUEST_CODE = 1;
	static private final String URL = "http://www.google.com";
	static private final String TAG = "Lab-Intents";

	// For use with app chooser
	static private final String CHOOSER_TEXT = "Load " + URL + " with:";

	// TextView that displays user-entered text from ExplicitlyLoadedActivity runs
	private TextView mUserTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loader_activity);
		
		// Get reference to the textView
		mUserTextView = (TextView) findViewById(R.id.textView1);

		// Declare and setup Explicit Activation button
		Button explicitActivationButton = (Button) findViewById(R.id.explicit_activation_button);
		explicitActivationButton.setOnClickListener(new OnClickListener() {

			// Call startExplicitActivation() when pressed
			@Override
			public void onClick(View v) {
				
				startExplicitActivation();
			
			}
		});

		// Declare and setup Implicit Activation button
		Button implicitActivationButton = (Button) findViewById(R.id.implicit_activation_button);
		implicitActivationButton.setOnClickListener(new OnClickListener() {

			// Call startImplicitActivation() when pressed
			@Override
			public void onClick(View v) {
			
				startImplicitActivation();
			
			}
		});

	}

	
	// Start the ExplicitlyLoadedActivity
	
	private void startExplicitActivation() {

		Log.i(TAG,"Entered startExplicitActivation()");
		
		// TODO - Create a new intent to launch the ExplicitlyLoadedActivity class
		
		// TODO - Start an Activity using that intent and the request code defined above


	}

	// Start a Browser Activity to view a web page or its URL
	
	private void startImplicitActivation() {

		Log.i(TAG, "Entered startImplicitActivation()");

		// TODO - Create a base intent for viewing a URL 
		// (HINT:  second parameter uses parse() from the Uri class)
		
		
		// TODO - Create a chooser intent, for choosing which Activity
		// will carry out the baseIntent. Store the Intent in the 
		// chooserIntent variable below. HINT: using the Intent class' 
		// createChooser())
		
		Intent chooserIntent = null;

		Log.i(TAG,"Chooser Intent Action:" + chooserIntent.getAction());
		// TODO - Start the chooser Activity, using the chooser intent
		startActivity(chooserIntent);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "Entered onActivityResult()");
		
		// TODO - Process the result only if this method received both a
		// RESULT_OK result code and a recognized request code
		// If so, update the Textview showing the user-entered text.


	}
}
