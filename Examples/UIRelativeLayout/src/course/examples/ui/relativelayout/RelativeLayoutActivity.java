package course.examples.ui.relativelayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RelativeLayoutActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final EditText textEntry = (EditText) findViewById(R.id.entry);

		final Button cancelButton = (Button) findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Clear the textEntry
				textEntry.setText("");
			}
		});

		final Button okButton = (Button) findViewById(R.id.ok_button);
		okButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Finish the application
				RelativeLayoutActivity.this.finish();
			}
		});
	}

}