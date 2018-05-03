package course.examples.touch.gestures;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GesturesActivity extends Activity implements
        OnGesturePerformedListener {
    private static final String NO = "No";
    private static final String YES = "Yes";
    private static final String PREV = "Prev";
    private static final String NEXT = "Next";
    private GestureLibrary mLibrary;
    private int mBgColor = 0;
    private int mFirstColor;
    private final int mStartBgColor = Color.GRAY;
    private FrameLayout mFrame;
    private RelativeLayout mLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mFrame = findViewById(R.id.frame);
        mBgColor = new Random().nextInt(0xFFFFFF) | 0xFF000000;
        mFirstColor = mBgColor;
        mFrame.setBackgroundColor(mBgColor);

        mLayout = findViewById(R.id.main);
        mLayout.setBackgroundColor(mStartBgColor);

        // Add gestures file - contains 4 gestures: Prev, Next, Yes, No
        mLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!mLibrary.load()) {
            finish();
        }

        // Make this the target of gesture detection callbacks
        GestureOverlayView gestureView = findViewById(R.id.gestures_overlay);
        gestureView.addOnGesturePerformedListener(this);

    }

    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

        // Get gesture predictions
        ArrayList<Prediction> predictions = mLibrary.recognize(gesture);

        // Get highest-ranked prediction
        if (predictions.size() > 0) {
            Prediction prediction = predictions.get(0);

            // Ignore weak predictions

            if (prediction.score > 2.0) {
                switch (prediction.name) {

                    case PREV:

                        mBgColor -= 100;
                        mFrame.setBackgroundColor(mBgColor);

                        break;

                    case NEXT:

                        mBgColor += 100;
                        mFrame.setBackgroundColor(mBgColor);

                        break;

                    case YES:

                        mLayout.setBackgroundColor(mBgColor);

                        break;

                    case NO:

                        mLayout.setBackgroundColor(mStartBgColor);
                        mFrame.setBackgroundColor(mFirstColor);

                        break;
                }
                Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(this, "No prediction", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(this, "No prediction", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
