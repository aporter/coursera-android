package course.examples.graphics.canvasbubble;

import java.util.Random;

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
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class BubbleActivity extends Activity {
	private static final String TAG = "BubbleActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final RelativeLayout frame = findViewById(R.id.frame);
		final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.b512);
		final BubbleView bubbleView = new BubbleView(getApplicationContext(),
				bitmap);

		frame.addView(bubbleView);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (bubbleView.moveWhileOnscreen()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Log.i(TAG, "InterruptedException");
					}
                    bubbleView.postInvalidate();
				}
			}
		}).start();
	}

	private class BubbleView extends View {

		private static final int STEP = 100;
        private final Bitmap mBitmap;
		
		private Coords mCurrent;
		final private Coords mDxDy;

		final private DisplayMetrics mDisplayMetrics;
		final private int mDisplayWidth;
		final private int mDisplayHeight;
		final private int mBitmapWidthAndHeight, mBitmapWidthAndHeightAdj;
		final private Paint mPainter = new Paint();

		public BubbleView(Context context, Bitmap bitmap) {
			super(context);

			// Scale bitmap
			mBitmapWidthAndHeight = (int) getResources().getDimension(
					R.dimen.image_height);
			this.mBitmap = Bitmap.createScaledBitmap(bitmap,
					mBitmapWidthAndHeight, mBitmapWidthAndHeight, false);
            mBitmapWidthAndHeightAdj = mBitmapWidthAndHeight + 20;

			// Get display size info
             mDisplayMetrics = new DisplayMetrics();
			BubbleActivity.this.getWindowManager().getDefaultDisplay()
					.getMetrics(mDisplayMetrics);
			mDisplayWidth = mDisplayMetrics.widthPixels;
			mDisplayHeight = mDisplayMetrics.heightPixels;

            // Set random starting point
            Random r = new Random();
			float x = (float) r.nextInt(mDisplayWidth - mBitmapWidthAndHeight);
			float y = (float) r.nextInt(mDisplayHeight - mBitmapWidthAndHeight);
			mCurrent = new Coords(x, y);

			// Set random movement direction and speed
            float dy = Math.max(r.nextFloat(),0.1f) * STEP;
			dy *= r.nextInt(2) == 1 ? 1 : -1;
			float dx = Math.max(r.nextFloat(),0.1f) * STEP;
			dx *= r.nextInt(2) == 1 ? 1 : -1;
			mDxDy = new Coords(dx, dy);

			// Add some painting directives
            mPainter.setAntiAlias(true);
            mPainter.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP));

		}

		@Override
		protected void onDraw(Canvas canvas) {
			Coords tmp = mCurrent.getCoords();
			canvas.drawBitmap(mBitmap, tmp.mX, tmp.mY, mPainter);
		}

		boolean moveWhileOnscreen() {
			mCurrent = mCurrent.move(mDxDy);

            return !(mCurrent.mY < 0 - mBitmapWidthAndHeightAdj
                    || mCurrent.mY > mDisplayHeight + mBitmapWidthAndHeightAdj
                    || mCurrent.mX < 0 - mBitmapWidthAndHeightAdj
                    || mCurrent.mX > mDisplayWidth + mBitmapWidthAndHeightAdj);
		}
	}
}