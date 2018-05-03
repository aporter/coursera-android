package course.examples.ui.checkbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class CheckBoxActivity extends Activity {

    private static final String CHECKBOX_CHECKED_STATE_KEY = "CHECKBOX_CHECKED_STATE_KEY";
    private static final String CHECKBOX_VISIBILITY_STATE_KEY = "CHECKBOX_VISIBILITY_STATE_KEY";
    private static final String BUTTON_TEXT_STATE_KEY = "BUTTON_TEXT_STATE_KEY";
    private CheckBox mCheckBox;
    private Button mButton;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Get a reference to the CheckBox
        mCheckBox = findViewById(R.id.checkbox);

        // Get a reference to the Button
        mButton = findViewById(R.id.button);

        if (null != savedInstanceState) {
            restoreUI(savedInstanceState);
        }
    }

    private void restoreUI(Bundle savedInstanceState) {
        mButton.setText(savedInstanceState.getCharSequence(BUTTON_TEXT_STATE_KEY));
        mCheckBox.setVisibility(savedInstanceState.getInt(CHECKBOX_VISIBILITY_STATE_KEY));
        mCheckBox.setChecked(savedInstanceState.getBoolean(CHECKBOX_CHECKED_STATE_KEY));
        setCheckedStateAndText();
    }
    // Set with android:onClick
    public void checkBoxClickCallback(@SuppressWarnings("unused") View view) {
        setCheckedStateAndText();
    }

    private void setCheckedStateAndText() {
        // Check whether CheckBox is currently checked
        // Set CheckBox text accordingly
        if (mCheckBox.isChecked()) {
            mCheckBox.setText(R.string.im_checked_string);
        } else {
            mCheckBox.setText(R.string.im_not_checked_string);
        }
    }

    // Set with android:onClick
    public void buttonClickCallback(@SuppressWarnings("unused") View view) {

        // Toggle the CheckBox's visibility state
        // Set the Button text accordingly
        if (mCheckBox.isShown()) {
            mCheckBox.setVisibility(View.INVISIBLE);
            mButton.setText(R.string.unhide_checkbox_string);
        } else {
            mCheckBox.setVisibility(View.VISIBLE);
            mButton.setText(R.string.hide_checkbox_string);
        }
    }

    // Save instance state
    @Override
    public void onSaveInstanceState(Bundle bundle) {

        // CheckBox (checkedState, visibility)
        bundle.putBoolean(CHECKBOX_CHECKED_STATE_KEY, mCheckBox.isChecked());
        bundle.putInt(CHECKBOX_VISIBILITY_STATE_KEY, mCheckBox.getVisibility());

        // Button (text)
        bundle.putCharSequence(BUTTON_TEXT_STATE_KEY, mButton.getText());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(bundle);
    }
}