package course.examples.ui.alertdialog

import android.app.AlertDialog
import android.app.Dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment

// Class that creates the AlertDialog
class AlertDialogFragment : DialogFragment() {

    companion object {

        fun newInstance(): AlertDialogFragment {
            return AlertDialogFragment()
        }
    }

    // Build AlertDialog using AlertDialog.Builder
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
            .setMessage("Do you really want to exit?")

            // User cannot dismiss dialog by hitting back button
            .setCancelable(false)

            // Set up No Button
            .setNegativeButton(
                "No"
            ) { _, _ ->
                (activity as AlertDialogActivity)
                    .continueShutdown(false)
            }

            // Set up Yes Button
            .setPositiveButton(
                "Yes"
            ) { _, _ ->
                (activity as AlertDialogActivity)
                    .continueShutdown(true)
            }.create()
    }
}
