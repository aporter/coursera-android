package course.examples.ui.togglebutton;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.ToggleButton;

public class ToggleButtonActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        // Get references to a background containers
        final FrameLayout top = findViewById(R.id.top_frame);
        final FrameLayout bottom = findViewById(R.id.bottom_frame);


		// Get a reference to the ToggleButton
        final ToggleButton toggleButton = findViewById(R.id.togglebutton);

        // Set an setOnCheckedChangeListener on the ToggleButton
        setListener(toggleButton, top);

        // Get a reference to the Switch
        final Switch switcher = findViewById(R.id.switcher);

        // Set an OnCheckedChangeListener on the Switch
        setListener(switcher, bottom);

    }

    private void setListener(CompoundButton button, final View background) {

        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                // Toggle the Background color between a light and dark color
                if (isChecked) {
                    background.setBackgroundColor(getResources().getColor(R.color.primary_light));
                } else {
                    background.setBackgroundColor(Color.TRANSPARENT);
                }
			}
		});
	}
}