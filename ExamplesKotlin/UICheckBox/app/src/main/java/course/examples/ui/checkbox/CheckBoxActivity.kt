package course.examples.ui.checkbox

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox

class CheckBoxActivity : Activity() {

    companion object {

        private const val CHECKBOX_CHECKED_STATE_KEY = "CHECKBOX_CHECKED_STATE_KEY"
        private const val CHECKBOX_VISIBILITY_STATE_KEY = "CHECKBOX_VISIBILITY_STATE_KEY"
        private const val BUTTON_TEXT_STATE_KEY = "BUTTON_TEXT_STATE_KEY"
    }

    private lateinit var mCheckBox: CheckBox
    private lateinit var mButton: Button

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

        // Get a reference to the CheckBox
        mCheckBox = findViewById(R.id.checkbox)

        // Get a reference to the Button
        mButton = findViewById(R.id.button)

        savedInstanceState?.run {
            restoreUI(savedInstanceState)
        }
    }

    private fun restoreUI(savedInstanceState: Bundle) {
        mButton.text = savedInstanceState.getCharSequence(BUTTON_TEXT_STATE_KEY)
        mCheckBox.visibility = savedInstanceState.getInt(CHECKBOX_VISIBILITY_STATE_KEY)
        mCheckBox.isChecked = savedInstanceState.getBoolean(CHECKBOX_CHECKED_STATE_KEY)
        setCheckedStateAndText()
    }

    // Set with android:onClick
    fun checkBoxClickCallback(view: View) {
        setCheckedStateAndText()
    }

    private fun setCheckedStateAndText() {
        // Check whether CheckBox is currently checked
        // Set CheckBox text accordingly
        if (mCheckBox.isChecked) {
            mCheckBox.text = getString(R.string.im_checked_string)
        } else {
            mCheckBox.text = getString(R.string.im_not_checked_string)
        }
    }

    // Set with android:onClick
    fun buttonClickCallback(view: View) {

        // Toggle the CheckBox's visibility state
        // Set the Button text accordingly
        if (mCheckBox.isShown) {
            mCheckBox.visibility = View.INVISIBLE
            mButton.text = getString(R.string.unhide_checkbox_string)
        } else {
            mCheckBox.visibility = View.VISIBLE
            mButton.text = getString(R.string.hide_checkbox_string)
        }
    }

    // Save instance state
    public override fun onSaveInstanceState(bundle: Bundle) {

        // CheckBox (checkedState, visibility)
        bundle.putBoolean(CHECKBOX_CHECKED_STATE_KEY, mCheckBox.isChecked)
        bundle.putInt(CHECKBOX_VISIBILITY_STATE_KEY, mCheckBox.visibility)

        // Button (text)
        bundle.putCharSequence(BUTTON_TEXT_STATE_KEY, mButton.text)

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(bundle)
    }
}