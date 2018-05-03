package course.examples.ui.radiogroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class RadioGroupActivity extends Activity {

    private static final String CHECKEDBOX_ID_KEY = "CHECKEDBOX_ID_KEY";
    private TextView mTextView;
    private RadioGroup mRadioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTextView = findViewById(R.id.textView);
        mRadioGroup = findViewById(R.id.radio_group);

        setupUI(savedInstanceState);
    }

    private void setupUI(Bundle savedInstanceState) {
        RadioButton checkedButton;

        if (null != savedInstanceState) {
            checkedButton = findViewById(savedInstanceState.getInt(CHECKEDBOX_ID_KEY));
        } else {
            checkedButton = findViewById(mRadioGroup.getCheckedRadioButtonId());
        }
        checkedButton.setChecked(true);
        mTextView.setText(getString(R.string.chosen_string, checkedButton.getText()));
    }

    // Define a generic listener for all three RadioButtons in the RadioGroup
    public void radioButtonClickCallback(View v) {
        RadioButton rb = (RadioButton) v;

        // RadioButtons report each click, even if the toggle state doesn't change
        if (rb.isChecked()) {
            mTextView.setText(getString(R.string.chosen_string, rb.getText()));
        }
    }

    // Save instance state
    @Override
    public void onSaveInstanceState(Bundle bundle) {

        // CheckBox (checkedState, visibility)
        bundle.putInt(CHECKEDBOX_ID_KEY, mRadioGroup.getCheckedRadioButtonId());

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(bundle);
    }
}