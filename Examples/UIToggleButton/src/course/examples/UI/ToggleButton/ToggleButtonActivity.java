package course.examples.UI.ToggleButton;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class ToggleButtonActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Get a reference to a background container 
		final LinearLayout bg = (LinearLayout) findViewById(R.id.linearlayout);
		
		// Get a reference to the ToggleButton
		final ToggleButton button = (ToggleButton) findViewById(R.id.togglebutton);
		
		// Set an OnClickListener on the ToggleButton
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// Toggle the Background color between a light and dark color
				if (button.isChecked()) {
					bg.setBackgroundColor(0xFFF3F3F3);
				} else {
					bg.setBackgroundColor(0xFF000000);
				}
			}
		});
		
	}
}
