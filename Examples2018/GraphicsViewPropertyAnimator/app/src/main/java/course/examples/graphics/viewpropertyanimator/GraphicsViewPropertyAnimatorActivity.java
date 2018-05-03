package course.examples.graphics.viewpropertyanimator;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

public class GraphicsViewPropertyAnimatorActivity extends Activity {

	private ImageView mImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		mImageView = findViewById(R.id.icon);

		if (hasFocus) {
			fadeIn.run();
		}
	}

	private final Runnable fadeIn = new Runnable() {
		public void run() {
			mImageView.animate().setDuration(3000)
					.setInterpolator(new LinearInterpolator()).alpha(1.0f)
					.withEndAction(rotate);
		}
	};

	private final Runnable rotate = new Runnable() {
		public void run() {
			mImageView.animate().setDuration(4000)
					.setInterpolator(new AccelerateInterpolator())
					.rotationBy(720.0f).withEndAction(translate);
		}
	};

	private final Runnable translate = new Runnable() {
		public void run() {
			float translation = getResources()
					.getDimension(R.dimen.translation);
			mImageView.animate().setDuration(3000)
					.setInterpolator(new OvershootInterpolator())
					.translationXBy(translation).translationYBy(translation)
					.withEndAction(scale);
		}
	};

	private final Runnable scale = new Runnable() {
		public void run() {
			mImageView.animate().setDuration(3000)
					.setInterpolator(new AnticipateInterpolator())
					.scaleXBy(1.0f).scaleYBy(1.0f).withEndAction(fadeOut);
		}
	};

	private final Runnable fadeOut = new Runnable() {
		public void run() {
			mImageView.animate().setDuration(2000)
					.setInterpolator(new DecelerateInterpolator()).alpha(0.0f);
		}
	};

}