package course.examples.graphics.canvasbubblesurfaceview;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

public class BubbleActivity extends Activity {

	BubbleView mBubbleView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.frame);
		final BubbleView bubbleView = new BubbleView(getApplicationContext(),
				BitmapFactory.decodeResource(getResources(), R.drawable.b128));

		relativeLayout.addView(bubbleView);
	}

	private class BubbleView extends SurfaceView implements
			SurfaceHolder.Callback {

		private final Bitmap mBitmap;
		private final int mBitmapHeightAndWidth, mBitmapHeightAndWidthAdj;
		private final DisplayMetrics mDisplay;
		private final int mDisplayWidth, mDisplayHeight;
		private float mX, mY, mDx, mDy, mRotation;
		private final SurfaceHolder mSurfaceHolder;
		private final Paint mPainter = new Paint();
		private Thread mDrawingThread;

		private static final int MOVE_STEP = 1;
		private static final float ROT_STEP = 1.0f;

		public BubbleView(Context context, Bitmap bitmap) {
			super(context);

			mBitmapHeightAndWidth = (int) getResources().getDimension(
					R.dimen.image_height_width);
			this.mBitmap = Bitmap.createScaledBitmap(bitmap,
					mBitmapHeightAndWidth, mBitmapHeightAndWidth, false);

			mBitmapHeightAndWidthAdj = mBitmapHeightAndWidth / 2;

			mDisplay = new DisplayMetrics();
			BubbleActivity.this.getWindowManager().getDefaultDisplay()
					.getMetrics(mDisplay);
			mDisplayWidth = mDisplay.widthPixels;
			mDisplayHeight = mDisplay.heightPixels;

			Random r = new Random();
			mX = (float) r.nextInt(mDisplayHeight);
			mY = (float) r.nextInt(mDisplayWidth);
			mDx = (float) r.nextInt(mDisplayHeight) / mDisplayHeight;
			mDx *= r.nextInt(2) == 1 ? MOVE_STEP : -1 * MOVE_STEP;
			mDy = (float) r.nextInt(mDisplayWidth) / mDisplayWidth;
			mDy *= r.nextInt(2) == 1 ? MOVE_STEP : -1 * MOVE_STEP;
			mRotation = 1.0f;

			mPainter.setAntiAlias(true);

			mSurfaceHolder = getHolder();
			mSurfaceHolder.addCallback(this);
		}

		private void drawBubble(Canvas canvas) {
			canvas.drawColor(Color.DKGRAY);
			mRotation += ROT_STEP;
			canvas.rotate(mRotation, mY + mBitmapHeightAndWidthAdj, mX
					+ mBitmapHeightAndWidthAdj);
			canvas.drawBitmap(mBitmap, mY, mX, mPainter);
		}

		private boolean move() {
			mX += mDx;
			mY += mDy;
			if (mX < 0 - mBitmapHeightAndWidth
					|| mX > mDisplayHeight + mBitmapHeightAndWidth
					|| mY < 0 - mBitmapHeightAndWidth
					|| mY > mDisplayWidth + mBitmapHeightAndWidth) {
				return false;
			} else {
				return true;
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			mDrawingThread = new Thread(new Runnable() {
				public void run() {
					Canvas canvas = null;
					while (!Thread.currentThread().isInterrupted() && move()) {
						canvas = mSurfaceHolder.lockCanvas();
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