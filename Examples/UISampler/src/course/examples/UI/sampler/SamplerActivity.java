package course.examples.UI.sampler;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SamplerActivity extends Activity {

	// Define a generic OnClickListener for RadioButtons
	private OnClickListener radio_listener = new OnClickListener() {
		public void onClick(View v) {
			// Show Toast message indicating current selection
			RadioButton rb = (RadioButton) v;
			Toast.makeText(SamplerActivity.this, rb.getText(),
					Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// 
		final ImageButton button = (ImageButton) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Show Toast message 
				Toast.makeText(SamplerActivity.this, "Beep Bop",
						Toast.LENGTH_SHORT).show();
			}
		});

		final EditText edittext = (EditText) findViewById(R.id.edittext);
		edittext.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "Done" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Show Toast message 
					Toast.makeText(SamplerActivity.this, edittext.getText(),
							Toast.LENGTH_SHORT).show();
					return true;
				}
				return false;
			}
		});

		final CheckBox checkbox = (CheckBox) findViewById(R.id.checkbox);
		checkbox.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Show Toast message indicating the CheckBox's Checked state
				if (((CheckBox) v).isChecked()) {
					Toast.makeText(SamplerActivity.this, "CheckBox checked",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(SamplerActivity.this, "CheckBox not checked",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		final RadioButton radio_red = (RadioButton) findViewById(R.id.radio_red);
		final RadioButton radio_blue = (RadioButton) findViewById(R.id.radio_blue);
		radio_red.setOnClickListener(radio_listener);
		radio_blue.setOnClickListener(radio_listener);

		
		final ToggleButton togglebutton = (ToggleButton) findViewById(R.id.togglebutton);
		togglebutton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        // Perform action on clicks
		        if (togglebutton.isChecked()) {
		            Toast.makeText(SamplerActivity.this, "ToggleButton checked", Toast.LENGTH_SHORT).show();
		        } else {
		            Toast.makeText(SamplerActivity.this, "ToggleButton not checked", Toast.LENGTH_SHORT).show();
		        }
		    }
		});
		
		final RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingbar);
		ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
		    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
		        Toast.makeText(SamplerActivity.this, "New Rating: " + rating, Toast.LENGTH_SHORT).show();
		    }
		});
	}
}