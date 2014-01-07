package course.examples.Graphics.CanvasBubble;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class BubbleActivity extends Activity {
	protected static final String TAG = "BubbleActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final RelativeLayout frame = (RelativeLayout) findViewById(R.id.frame);
		final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.b128);
		final BubbleView bubbleView = new BubbleView(getApplicationContext(),
				bitmap);

		frame.addView(bubbleView);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (bubbleView.move()) {
					bubbleView.postInvalidate();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Log.i(TAG, "InterruptedException");
					}

				}
			}
		}).start();
	}

	private class BubbleView extends View {

		private static final int STEP = 100;
		final private Bitmap mBitmap;
		
		private Coords mCurrent;
		final private Coords mDxDy;

		final private DisplayMetrics mDisplayMetrics;
		final private int mDisplayWidth;
		final private int mDisplayHeight;
		final private int mBitmapWidthAndHeight, mBitmapWidthAndHeightAdj;
		final private Paint mPainter = new Paint();

		public BubbleView(Context context, Bitmap bitmap) {
			super(context);

			mBitmapWidthAndHeight = (int) getResources().getDimension(
					R.dimen.image_height);
			this.mBitmap = Bitmap.createScaledBitmap(bitmap,
					mBitmapWidthAndHeight, mBitmapWidthAndHeight, false);

			mBitmapWidthAndHeightAdj = mBitmapWidthAndHeight + 20;
			
			mDisplayMetrics = new DisplayMetrics();
			BubbleActivity.this.getWindowManager().getDefaultDisplay()
					.getMetrics(mDisplayMetrics);
			mDisplayWidth = mDisplayMetrics.widthPixels;
			mDisplayHeight = mDisplayMetrics.heightPixels;

			Random r = new Random();
			float x = (float) r.nextInt(mDisplayWidth);
			float y = (float) r.nextInt(mDisplayHeight);
			mCurrent = new Coords(x, y);

			float dy = (float) r.nextInt(mDisplayHeight) / mDisplayHeight;
			dy *= r.nextInt(2) == 1 ? STEP : -1 * STEP;
			float dx = (float) r.nextInt(mDisplayWidth) / mDisplayWidth;
			dx *= r.nextInt(2) == 1 ? STEP : -1 * STEP;
			mDxDy = new Coords(dx, dy);

			mPainter.setAntiAlias(true);

		}

		@Override
		protected void onDraw(Canvas canvas) {
			Coords tmp = mCurrent.getCoords();
			canvas.drawBitmap(mBitmap, tmp.mX, tmp.mY, mPainter);
		}

		protected boolean move() {
			mCurrent = mCurrent.move(mDxDy);

			if (mCurrent.mY < 0 - mBitmapWidthAndHeightAdj
					|| mCurrent.mY > mDisplayHeight + mBitmapWidthAndHeightAdj
					|| mCurrent.mX < 0 - mBitmapWidthAndHeightAdj
					|| mCurrent.mX > mDisplayWidth + mBitmapWidthAndHeightAdj) {
				return false;
			} else {
				return true;
			}
		}
	}
}