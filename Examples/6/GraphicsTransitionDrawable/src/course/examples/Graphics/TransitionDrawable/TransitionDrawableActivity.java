package course.examples.Graphics.TransitionDrawable;

import android.app.Activity;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class TransitionDrawableActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		
		TransitionDrawable transition = (TransitionDrawable) getResources()
				.getDrawable(R.drawable.shape_transition);

		transition.setCrossFadeEnabled(true);

		((ImageView) findViewById(R.id.image_view)).setImageDrawable(transition);
		
		transition.startTransition(5000);
	}
}