package course.examples.ui.alertdialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        mShutdownButton = findViewById(R.id.shutdownButton);
    }

    public void onShutDownButtonClicked(@SuppressWarnings("unused") View view) {
        showDialogFragment(ALERTTAG);
    }

    // Show desired Dialog
    private void showDialogFragment(int dialogID) {

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
    void continueShutdown(boolean shouldContinue) {
        if (shouldContinue) {

            // Prevent further interaction with the ShutDown Button
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

}