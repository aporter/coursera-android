package course.examples.datamanagement.preferencefragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PreferencesActivityExample extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Open a User Preferences Entry Activity 
		final Button button = findViewById(R.id.check_pref_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(
						PreferencesActivityExample.this,
						ViewAndUpdatePreferencesActivity.class));
			}
		});

	}
}