package course.examples.Graphics.BubbleProgram;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import course.examples.Graphics.Bubble.R;

public class BubbleActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.frame);

		ImageView bubbleView = new ImageView(getApplicationContext());
		bubbleView
				.setImageDrawable(getResources().getDrawable(R.drawable.b128));

		int width = (int) getResources().getDimension(R.dimen.image_width);
		int height = (int) getResources().getDimension(R.dimen.image_height);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				width, height);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);

		bubbleView.setLayoutParams(params);

		relativeLayout.addView(bubbleView);
	}
}