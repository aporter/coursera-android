package course.examples.ui.radiogroup

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

class RadioGroupActivity : Activity() {


    companion object {

        private const val CHECKEDBOX_ID_KEY = "CHECKEDBOX_ID_KEY"
    }

    private lateinit var mTextView: TextView
    private lateinit var mRadioGroup: RadioGroup

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        mTextView = findViewById(R.id.textView)
        mRadioGroup = findViewById(R.id.radio_group)

        setupUI(savedInstanceState)
    }

    private fun setupUI(savedInstanceState: Bundle?) {

        val checkedButton: RadioButton = if (null != savedInstanceState) {
            findViewById(savedInstanceState.getInt(CHECKEDBOX_ID_KEY))
        } else {
            findViewById(mRadioGroup.checkedRadioButtonId)
        }

        checkedButton.isChecked = true
        mTextView.text = getString(R.string.chosen_string, checkedButton.text)
    }

    // Define a generic listener for all three RadioButtons in the RadioGroup
    fun radioButtonClickCallback(v: View) {
        val rb = v as RadioButton

        // RadioButtons report each click, even if the toggle state doesn't change
        if (rb.isChecked) {
            mTextView.text = getString(R.string.chosen_string, rb.text)
        }
    }

    // Save instance state
    public override fun onSaveInstanceState(bundle: Bundle) {

        // CheckBox (checkedState, visibility)
        bundle.putInt(CHECKEDBOX_ID_KEY, mRadioGroup.checkedRadioButtonId)

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(bundle)
    }
}