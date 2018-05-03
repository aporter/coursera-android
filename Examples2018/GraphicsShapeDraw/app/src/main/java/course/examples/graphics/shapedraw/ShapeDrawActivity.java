package course.examples.graphics.shapedraw;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ShapeDrawActivity extends Activity {
	private final static int ALPHA = 127;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		int width = (int) getResources().getDimension(R.dimen.image_width);
		int height = (int) getResources().getDimension(R.dimen.image_height);
		int padding = (int) getResources().getDimension(R.dimen.padding);

		// Get container View
		RelativeLayout rl = findViewById(R.id.main_window);

		// Create Cyan Shape
		ShapeDrawable cyanShape = new ShapeDrawable(new OvalShape());
		cyanShape.getPaint().setColor(Color.CYAN);
		cyanShape.setIntrinsicHeight(height);
		cyanShape.setIntrinsicWidth(width);
		cyanShape.setAlpha(ALPHA);

		// Put Cyan Shape into an ImageView
		ImageView cyanView = new ImageView(getApplicationContext());
		cyanView.setImageDrawable(cyanShape);
		cyanView.setPadding(padding, padding, padding, padding);

		// Specify placement of ImageView within RelativeLayout
		RelativeLayout.LayoutParams cyanViewLayoutParams = new RelativeLayout.LayoutParams(
				height, width);
		cyanViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		cyanViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		cyanView.setLayoutParams(cyanViewLayoutParams);
		rl.addView(cyanView);

		// Create Magenta Shape
		ShapeDrawable magentaShape = new ShapeDrawable(new OvalShape());
		magentaShape.getPaint().setColor(Color.MAGENTA);
		magentaShape.setIntrinsicHeight(height);
		magentaShape.setIntrinsicWidth(width);
		magentaShape.setAlpha(ALPHA);

		// Put Magenta Shape into an ImageView
		ImageView magentaView = new ImageView(getApplicationContext());
		magentaView.setImageDrawable(magentaShape);
		magentaView.setPadding(padding, padding, padding, padding);

		// Specify placement of ImageView within RelativeLayout
		RelativeLayout.LayoutParams magentaViewLayoutParams = new RelativeLayout.LayoutParams(
				height, width);
		magentaViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		magentaViewLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		magentaView.setLayoutParams(magentaViewLayoutParams);

		rl.addView(magentaView);

	}
}