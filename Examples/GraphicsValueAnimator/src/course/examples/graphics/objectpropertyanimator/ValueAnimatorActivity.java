package course.examples.graphics.objectpropertyanimator;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ValueAnimatorActivity extends Activity {

	protected static final String TAG = "ValueAnimatorActivity";
	final private static int RED = Color.RED;
	final private static int BLUE = Color.BLUE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Button startButton = (Button) findViewById(R.id.start_animation_button);
		startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startAnimation();
			}
		});
		
	}

	public void startAnimation() {
		
		final ImageView imageView = (ImageView) findViewById(R.id.image_view);
		
		ValueAnimator anim = ValueAnimator.ofObject(new ArgbEvaluator(), RED,
				BLUE);

		anim.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				imageView.setBackgroundColor((Integer) animation
						.getAnimatedValue());
			}
		});
		
		anim.setDuration(10000);
		anim.start();
	}

}
