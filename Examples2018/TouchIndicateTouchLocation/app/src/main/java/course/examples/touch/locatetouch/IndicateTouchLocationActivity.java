package course.examples.touch.locatetouch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class IndicateTouchLocationActivity extends Activity {

    private static final int MIN_DXDY = 2;

    // Assume no more than 20 simultaneous touches
    final private static int MAX_TOUCHES = 20;

    // Pool of MarkerViews
    final private static LinkedList<MarkerView> mInactiveMarkers = new LinkedList<>();

    // Set of MarkerViews currently visible on the display
    @SuppressLint("UseSparseArrays")
    final private static Map<Integer, MarkerView> mActiveMarkers = new HashMap<>();

    private static final String TAG = "IndicateTouchLoc";

    private FrameLayout mFrame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mFrame = findViewById(android.R.id.content);

        // Initialize pool of View.
        initViews();

        // Create and set on touch listener
        mFrame.setOnTouchListener(new OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.performClick();

                switch (event.getActionMasked()) {

                    // Show new MarkerView

                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN: {

                        int pointerIndex = event.getActionIndex();
                        int pointerID = event.getPointerId(pointerIndex);

                        MarkerView marker = mInactiveMarkers.remove();

                        if (null != marker) {
                            mActiveMarkers.put(pointerID, marker);
                            marker.setXLoc(event.getX(pointerIndex));
                            marker.setYLoc(event.getY(pointerIndex));
                            updateTouches(mActiveMarkers.size());
                            mFrame.addView(marker);
                        }
                        break;
                    }

                    // Remove one MarkerView

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_POINTER_UP: {

                        int pointerIndex = event.getActionIndex();
                        int pointerID = event.getPointerId(pointerIndex);

                        MarkerView marker = mActiveMarkers.remove(pointerID);

                        if (null != marker) {
                            mInactiveMarkers.add(marker);
                            updateTouches(mActiveMarkers.size());
                            mFrame.removeView(marker);
                        }
                        break;
                    }

                    // Move all currently active MarkerViews
                    case MotionEvent.ACTION_MOVE: {

                        for (int idx = 0; idx < event.getPointerCount(); idx++) {

                            int ID = event.getPointerId(idx);

                            MarkerView marker = mActiveMarkers.get(ID);
                            if (null != marker) {

                                // Redraw only if finger has traveled a minimum distance
                                if (Math.abs(marker.getXLoc() - event.getX(idx)) > MIN_DXDY
                                        || Math.abs(marker.getYLoc()
                                        - event.getY(idx)) > MIN_DXDY) {

                                    // Set new location
                                    marker.setXLoc(event.getX(idx));
                                    marker.setYLoc(event.getY(idx));

                                    // Request re-draw
                                    marker.invalidate();
                                }
                            }
                        }

                        break;
                    }

                    default:
                        Log.i(TAG, "unhandled action");
                }

                return true;
            }

            // update number of touches on each active MarkerView
            private void updateTouches(int numActive) {
                for (MarkerView marker : mActiveMarkers.values()) {
                    marker.setTouches(numActive);
                }
            }
        });
    }

    private void initViews() {
        for (int idx = 0; idx < MAX_TOUCHES; idx++) {
            mInactiveMarkers.add(new MarkerView(this, -1, -1));
        }
    }

    private class MarkerView extends View {
        private float mX, mY;
        final static private int MAX_SIZE = 400;
        private int mTouches = 0;
        final private Paint mPaint = new Paint();

        public MarkerView(Context context, float x, float y) {
            super(context);
            mX = x;
            mY = y;
            mPaint.setStyle(Style.FILL);

            Random rnd = new Random();
            mPaint.setARGB(255, rnd.nextInt(256), rnd.nextInt(256),
                    rnd.nextInt(256));
        }

        float getXLoc() {
            return mX;
        }

        void setXLoc(float x) {
            mX = x;
        }

        float getYLoc() {
            return mY;
        }

        void setYLoc(float y) {
            mY = y;
        }

        void setTouches(int touches) {
            mTouches = touches;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(mX, mY, MAX_SIZE / mTouches, mPaint);
        }
    }

}