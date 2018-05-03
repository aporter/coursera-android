package course.examples.ui.ratingsbar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;


public class RatingsBarActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final TextView tv = findViewById(R.id.textView);
        final RatingBar bar = findViewById(R.id.ratingbar);
      
        bar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
	
        	// Called when the user swipes the RatingBar
        	@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tv.setText(getString(R.string.rating_string, rating));
            }
		});
    }
}