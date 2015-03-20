package course.examples.ui.tablayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		ImageView imageView = new ImageView(getApplicationContext());
		imageView
				.setImageResource(i.getIntExtra(GridFragment.EXTRA_RES_ID, -1));
		setContentView(imageView);
	}
}