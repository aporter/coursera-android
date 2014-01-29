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
	private static final int ALERTTAG = 0, PROGRESSTAG = 1;
	protected static final String TAG = "AlertDialogActivity";
	private Button mShutdownButton = null;
	private DialogFragment mDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mShutdownButton = (Button) findViewById(R.id.shutdownButton);
		mShutdownButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialogFragment(ALERTTAG);
			}
		});
	}

	void showDialogFragment(int dialogID) {
		switch (dialogID) {
		case ALERTTAG:
			mDialog = AlertDialogFragment.newInstance();
			mDialog.show(getFragmentManager(), "Alert");
			break;
		case PROGRESSTAG:
			mDialog = ProgressDialogFragment.newInstance();
			mDialog.show(getFragmentManager(), "Shutdown");
			break;
		}
	}

	protected void continueShutdown(boolean shouldContinue) {
		if (shouldContinue) {
			mShutdownButton.setEnabled(false);
			showDialogFragment(PROGRESSTAG);
			finishShutdown();
		} else {
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

	public static class AlertDialogFragment extends DialogFragment {

		public static AlertDialogFragment newInstance() {
			return new AlertDialogFragment();
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setMessage("Do you really want to exit?")
					.setCancelable(false)
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									((AlertDialogActivity) getActivity())
											.continueShutdown(false);
								}
							})
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

	public static class ProgressDialogFragment extends DialogFragment {

		public static ProgressDialogFragment newInstance() {
			return new ProgressDialogFragment();
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final ProgressDialog dialog = new ProgressDialog(getActivity());
			dialog.setMessage("Activity Shutting Down.");
			dialog.setIndeterminate(true);
			return dialog;
		}
	}
}