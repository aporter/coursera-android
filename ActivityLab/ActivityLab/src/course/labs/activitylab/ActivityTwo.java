package course.labs.activitylab;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityTwo extends Activity {

	private static final String RESTART_KEY = "restart";
	private static final String RESUME_KEY = "resume";
	private static final String START_KEY = "start";
	private static final String CREATE_KEY = "create";

	// String for LogCat documentation
	private final static String TAG = "Lab-ActivityTwo";

	// Lifecycle counters

	// TODO:
	// Create counter variables for onCreate(), onRestart(), onStart() and
	// onResume(), called mCreate, etc.
	// You will need to increment these variables' values when their
	// corresponding lifecycle methods get called

	private int mCreate = 0;
	private int mRestart = 0;
	private int mStart = 0;
	private int mResume = 0;
	

	// TODO: Create variables for each of the TextViews, called
        // mTvCreate, etc. 
	private TextView mTvCreate;
	private TextView mTvRestart;
	private TextView mTvStart;
	private TextView mTvResume;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two);

		// TODO: Assign the appropriate TextViews to the TextView variables
		// Hint: Access the TextView by calling Activity's findViewById()
		// textView1 = (TextView) findViewById(R.id.textView1);
		mTvCreate = (TextView) findViewById(R.id.create);
		mTvRestart = (TextView) findViewById(R.id.restart);
		mTvStart = (TextView) findViewById(R.id.start);
		mTvResume = (TextView) findViewById(R.id.resume);
	




		Button closeButton = (Button) findViewById(R.id.bClose); 
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO:
				// This function closes Activity Two
				// Hint: use Context's finish() method
				finish();


			
			}
		});

		// Check for previously saved state
		if (savedInstanceState != null) {

			// TODO:
			// Restore value of counters from saved state
			// Only need 4 lines of code, one for every count variable



		}

		// TODO: Emit LogCat message
		Log.i(TAG, "Entered the OnCreate method");


		// TODO:
		// Update the appropriate count variable
		++mCreate;
		// Update the user interface via the displayCounts() method
		displayCounts();



	}

	// Lifecycle callback methods overrides

	@Override
	public void onStart() {
		super.onStart();

		// TODO: Emit LogCat message
		Log.i(TAG, "Entered the onStart method");

		// TODO:
		// Update the appropriate count variable
		++mStart;
		// Update the user interface
		displayCounts();


	}

	@Override
	public void onResume() {
		super.onResume();

		// TODO: Emit LogCat message
		Log.i(TAG, "Entered the onResume method");

		// TODO:
		// Update the appropriate count variable
		++mResume;
		// Update the user interface
		displayCounts();



	}

	@Override
	public void onPause() {
		super.onPause();

		// TODO: Emit LogCat message
		Log.i(TAG, "Entered the onPause method");


	}

	@Override
	public void onStop() {
		super.onStop();

		// TODO: Emit LogCat message
		Log.i(TAG, "Entered the onStop method");


	}

	@Override
	public void onRestart() {
		super.onRestart();

		// TODO: Emit LogCat message
		Log.i(TAG, "Entered the onRestart method");

		// TODO:
		// Update the appropriate count variable
		++mRestart;
		// Update the user interface
		displayCounts();


	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// TODO: Emit LogCat message
		Log.i(TAG, "Entered the onDestroy method");
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {

		// TODO:
		// Save counter state information with a collection of key-value pairs
		// 4 lines of code, one for every count variable
		savedInstanceState.putInt(RESTART_KEY, mRestart);
		savedInstanceState.putInt(RESUME_KEY, mResume);
		savedInstanceState.putInt(CREATE_KEY, mCreate);
		savedInstanceState.putInt(START_KEY, mStart);





	
	}

	// Updates the displayed counters
	public void displayCounts() {

		mTvCreate.setText("onCreate() calls: " + mCreate);
		mTvStart.setText("onStart() calls: " + mStart);
		mTvResume.setText("onResume() calls: " + mResume);
		mTvRestart.setText("onRestart() calls: " + mRestart);
	
	}
}
