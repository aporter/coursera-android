package course.examples.ui.relativelayout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class RelativeLayoutActivity extends Activity {

    private EditText mTextEntry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTextEntry = findViewById(R.id.entry);

    }

    public void cancelButtonClicked(@SuppressWarnings("unused") View view) {
        mTextEntry.setText("");
    }

    public void okButtonClicked(@SuppressWarnings("unused") View view) {
        hideKeyboard();
        Toast.makeText(RelativeLayoutActivity.this,
                R.string.thanks_string,
                Toast.LENGTH_LONG).show();
    }

    private void hideKeyboard() {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}