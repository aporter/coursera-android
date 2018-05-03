package course.examples.ui.alertdialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

// Class that creates the ProgressDialog
public class ProgressDialogFragment extends DialogFragment {

    public static ProgressDialogFragment newInstance() {
        ProgressDialogFragment temp = new ProgressDialogFragment();
        temp.setCancelable(false);
        return temp;
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
