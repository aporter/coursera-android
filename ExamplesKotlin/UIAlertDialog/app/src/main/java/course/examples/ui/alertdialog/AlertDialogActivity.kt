package course.examples.ui.alertdialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AlertDialogActivity : FragmentActivity() {

    companion object {

        // Identifier for each type of Dialog
        private const val ALERT_ID = 0
        private const val PROGRESS_ID = 1

        @Suppress("unused")
        private const val TAG = "AlertDialogActivity"
        private const val ALERT_TAG = "AlertDialog"
    }

    private lateinit var mShutdownButton: Button
    private lateinit var mDialog: DialogFragment
    private lateinit var mProgressButton: ProgressBar

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        // ShutDown Button
        mShutdownButton = findViewById(R.id.shutdownButton)

        // Progress Button
        mProgressButton = findViewById(R.id.indeterminateBar)

    }

    fun onShutDownButtonClicked(view: View) {
        showDialogUIElement(ALERT_ID)
    }

    // Show desired Dialog
    private fun showDialogUIElement(dialogID: Int) {

        when (dialogID) {

            // Show AlertDialog
            ALERT_ID -> {

                // Create a new AlertDialogFragment
                mDialog = AlertDialogFragment.newInstance()

                // Show AlertDialogFragment
                mDialog.show(supportFragmentManager, ALERT_TAG)
            }

            // Show ProgressDialog
            PROGRESS_ID ->
                //Make ProgressBar visible
                mProgressButton.visibility = View.VISIBLE
        }
    }

    // Abort or complete ShutDown based on value of shouldContinue
    internal fun continueShutdown(shouldContinue: Boolean) {
        if (shouldContinue) {

            // Prevent further interaction with the ShutDown Button
            mShutdownButton.isEnabled = false


            // Show ProgressDialog as shutdown process begins
            showDialogUIElement(PROGRESS_ID)

            // Finish the ShutDown process
            finishShutdown()

        } else {

            // Abort ShutDown and dismiss dialog
            mDialog.dismiss()
        }
    }

    private fun finishShutdown() {

        GlobalScope.launch(context = Dispatchers.Main) {
            delay(5000)
            finish()
        }
    }
}