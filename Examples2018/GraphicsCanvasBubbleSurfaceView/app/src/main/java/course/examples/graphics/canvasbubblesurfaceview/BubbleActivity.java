package course.examples.graphics.canvasbubblesurfaceview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import java.util.Random;

public class BubbleActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final RelativeLayout relativeLayout = findViewById(R.id.frame);
        final BubbleView bubbleView = new BubbleView(getApplicationContext(),
                BitmapFactory.decodeResource(getResources(), R.drawable.b512));

        relativeLayout.addView(bubbleView);
    }

    private class BubbleView extends SurfaceView implements
            SurfaceHolder.Callback {

        private static final int STEP = 40;

        private final Bitmap mBitmap;
        private final int mBitmapHeightAndWidth, mBitmapWidthAndHeightAdj;
        private final float mBitmapHeightAndWidthPadded;
        private final float mBitmapPadding = 80.0f;

        private final DisplayMetrics mDisplay;
        private final int mDisplayWidth, mDisplayHeight;

        private float mRotation;
        private static final float ROT_STEP = 1.0f;

        private final SurfaceHolder mSurfaceHolder;
        private Thread mDrawingThread;
        private final Paint mPainter = new Paint();

        private final int mBackgroundColor;
        final private Coords mDxDy;
        private Coords mCurrent;


        public BubbleView(Context context, Bitmap bitmap) {
            super(context);

            // Resize bubble
            mBackgroundColor = context.getResources().getColor(R.color.background_color, null);
            mBitmapHeightAndWidth = (int) getResources().getDimension(
                    R.dimen.image_height_width);
            this.mBitmap = Bitmap.createScaledBitmap(bitmap,
                    mBitmapHeightAndWidth, mBitmapHeightAndWidth, false);

            // Set useful parameters
            mBitmapWidthAndHeightAdj = mBitmapHeightAndWidth / 2;
            mBitmapHeightAndWidthPadded = mBitmapHeightAndWidth + mBitmapPadding;

            // Determing screen size
            mDisplay = new DisplayMetrics();
            BubbleActivity.this.getWindowManager().getDefaultDisplay()
                    .getMetrics(mDisplay);
            mDisplayWidth = mDisplay.widthPixels;
            mDisplayHeight = mDisplay.heightPixels;

            // Set random starting point for BubbleView
            Random r = new Random();
            float x = (float) r.nextInt( mDisplayWidth / 4) + mDisplayWidth / 4;
            float y = (float) r.nextInt(mDisplayHeight / 4) + mDisplayHeight / 4;
            mCurrent = new Coords(x, y);

            // Set random movement direction and speed, and set rotation
            float dy = Math.max(r.nextFloat(), 0.1f) * STEP;
            dy *= r.nextInt(2) == 1 ? 1 : -1;
            float dx = Math.max(r.nextFloat(), 0.1f) * STEP;
            dx *= r.nextInt(2) == 1 ? 1 : -1;
            mDxDy = new Coords(dx, dy);
            mRotation = 1.0f;

            mPainter.setAntiAlias(true);
            mPainter.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));

            // Prepare surface for drawing
            mSurfaceHolder = getHolder();
            mSurfaceHolder.addCallback(this);
        }

        private void drawBubble(Canvas canvas) {
            canvas.drawColor(mBackgroundColor);
            mRotation += ROT_STEP;
            canvas.rotate(mRotation, mCurrent.mX + mBitmapWidthAndHeightAdj, mCurrent.mY
                    + mBitmapWidthAndHeightAdj);
            canvas.drawBitmap(mBitmap, mCurrent.mX, mCurrent.mY, mPainter);
        }

        private boolean moveWhileOnscreen() {
            mCurrent = mCurrent.move(mDxDy);

            return !(mCurrent.mY < 0 - mBitmapHeightAndWidthPadded
                    || mCurrent.mY > mDisplayHeight + mBitmapHeightAndWidthPadded
                    || mCurrent.mX < 0 - mBitmapHeightAndWidthPadded
                    || mCurrent.mX > mDisplayWidth + mBitmapHeightAndWidthPadded);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // Not implemented
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mDrawingThread = new Thread(new Runnable() {
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && moveWhileOnscreen()) {
                        Canvas canvas = mSurfaceHolder.lockCanvas();
                        if (null != canvas) {
                            drawBubble(canvas);
                            mSurfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            });
            mDrawingThread.start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (null != mDrawingThread)
                mDrawingThread.interrupt();
        }

    }
}