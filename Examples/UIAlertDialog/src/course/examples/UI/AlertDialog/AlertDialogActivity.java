package course.examples.UI.AlertDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AlertDialogActivity extends Activity {

	// Identifier for each type of Dialog
	private static final int ALERTTAG = 0, PROGRESSTAG = 1;

	private static final String TAG = "AlertDialogActivity";
	private Button mShutdownButton = null;
	private DialogFragment mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// ShutDown Button
		mShutdownButton = (Button) findViewById(R.id.shutdownButton);
		mShutdownButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogFragment(ALERTTAG);
			}
		});
	}

	// Show desired Dialog
	void showDialogFragment(int dialogID) {

		switch (dialogID) {

		// Show AlertDialog
		case ALERTTAG:

			// Create a new AlertDialogFragment
			mDialog = AlertDialogFragment.newInstance();

			// Show AlertDialogFragment
			mDialog.show(getFragmentManager(), "Alert");

			break;

		// Show ProgressDialog
		case PROGRESSTAG:

			// Create a new ProgressDialogFragment
			mDialog = ProgressDialogFragment.newInstance();

			// Show new ProgressDialogFragment
			mDialog.show(getFragmentManager(), "Shutdown");
			break;
		}
	}

	// Abort or complete ShutDown based on value of shouldContinue
	private void continueShutdown(boolean shouldContinue) {
		if (shouldContinue) {

			// Prevent further interaction with the ShutDown Butotn
			mShutdownButton.setEnabled(false);
			
			// Show ProgressDialog as shutdown process begins 
			showDialogFragment(PROGRESSTAG);
			
			// Finish the ShutDown process
			finishShutdown();
		
		} else {

			// Abort ShutDown and dismiss dialog
			mDialog.dismiss();
		}
	}

	private void finishShutdown() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// Pretend to do something before
					// shutting down
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					Log.i(TAG, e.toString());
				} finally {
					finish();
				}
			}
		}).start();
	}

	// Class that creates the AlertDialog
	public static class AlertDialogFragment extends DialogFragment {

		public static AlertDialogFragment newInstance() {
			return new AlertDialogFragment();
		}

		// Build AlertDialog using AlertDialog.Builder
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setMessage("Do you really want to exit?")
					
					// User cannot dismiss dialog by hitting back button
					.setCancelable(false)
					
					// Set up No Button
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									((AlertDialogActivity) getActivity())
											.continueShutdown(false);
								}
							})
							
					// Set up Yes Button
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog, int id) {
									((AlertDialogActivity) getActivity())
											.continueShutdown(true);
								}
							}).create();
		}
	}

	// Class that creates the ProgressDialog
	public static class ProgressDialogFragment extends DialogFragment {

		public static ProgressDialogFragment newInstance() {
			return new ProgressDialogFragment();
		}

		// Build ProgressDialog
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			//Create new ProgressDialog
			final ProgressDialog dialog = new ProgressDialog(getActivity());
			
			// Set Dialog message
			dialog.setMessage("Activity Shutting Down.");
			
			// Dialog will be displayed for an unknown amount of time
			dialog.setIndeterminate(true);

			return dialog;
		}
	}
}