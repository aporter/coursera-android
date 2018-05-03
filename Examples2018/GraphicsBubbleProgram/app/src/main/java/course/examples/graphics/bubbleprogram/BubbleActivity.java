package course.examples.graphics.bubbleprogram;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BubbleActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        RelativeLayout relativeLayout = findViewById(R.id.frame);

        ImageView bubbleView = new ImageView(getApplicationContext());

        BitmapDrawable tmp = (BitmapDrawable) getDrawable(R.drawable.b512);
        if (null != tmp) {
            tmp.setTint(Color.WHITE);
            bubbleView.setImageDrawable(tmp);
        }

        int width = (int) getResources().getDimension(R.dimen.image_width);
        int height = (int) getResources().getDimension(R.dimen.image_height);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                width, height);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        bubbleView.setLayoutParams(params);

        relativeLayout.addView(bubbleView);
    }
}