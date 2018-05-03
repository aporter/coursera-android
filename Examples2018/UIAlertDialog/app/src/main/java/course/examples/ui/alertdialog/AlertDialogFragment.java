package course.examples.ui.alertdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

// Class that creates the AlertDialog
public class AlertDialogFragment extends DialogFragment {

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
