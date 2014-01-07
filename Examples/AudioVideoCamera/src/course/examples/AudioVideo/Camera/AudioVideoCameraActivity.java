package course.examples.AudioVideo.Camera;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class AudioVideoCameraActivity extends Activity {

	private static final int PREVIEW_PAUSE = 2000;
	private static final String TAG = "AudioVideoCameraActivity";
	private Camera mCamera;
	private LinearLayout mFrame;
	private SurfaceHolder mSurfaceHolder;

	private enum PreviewState {
		RUNNING, STOPPED
	};

	private PreviewState mPreviewState = PreviewState.STOPPED;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup application window
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);

		// Get camera instance
		mCamera = getCamera();

		if (null == mCamera)
			finish();

		// Setup touch listener for taking pictures

		mFrame = (LinearLayout) findViewById(R.id.frame);
		mFrame.setEnabled(false);
		mFrame.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getActionMasked() == (MotionEvent.ACTION_UP)) {

					mCamera.takePicture(mShutterCallback, null,
							mPictureCallback);

				}
				return true;
			}
		});

		// Setup SurfaceView for previewing camera image

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.cameraView);
		mSurfaceHolder = surfaceView.getHolder();
		mSurfaceHolder.addCallback(mSurfaceHolderCallback);

	}

	// Get camera instance
	private Camera getCamera() {

		try {
			
			// Returns first back-facing camera 
			// May take a long time to complete
			// Consider moving this to an AsyncTask
			mCamera = Camera.open();
			
		} catch (RuntimeException e) {
			
			Log.e(TAG, "Failed to acquire camera");
		}
		return mCamera;
	}

	SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
				mPreviewState = PreviewState.RUNNING;
			} catch (IOException e) {
				Log.e(TAG, "Failed to start preview");
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {

			// Shutdown current preview

			if (mSurfaceHolder.getSurface() == null) {
				return;
			}
			
			
			mFrame.setEnabled(false);

			if (mPreviewState == PreviewState.RUNNING) {
				try {
					mCamera.stopPreview();
					mPreviewState = PreviewState.STOPPED;
				} catch (Exception e) {
				}
			}

			// Change camera parameters
			Camera.Parameters p = mCamera.getParameters();

			// Find closest supported preview size
			Camera.Size bestSize = findBestSize(p, width, height);

			p.setPreviewSize(bestSize.width, bestSize.height);

			mCamera.setParameters(p);

			// Restart preview
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (IOException e) {
				Log.e(TAG, "Failed to set preview display");
			}
			try {
				mCamera.startPreview();
				mPreviewState = PreviewState.RUNNING;
				mFrame.setEnabled(true);
			} catch (RuntimeException e) {
				Log.e(TAG, "Failed to start preview");
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// Do Nothing
		}
	};

	// System Shutter Sound
	Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		@Override
		public void onShutter() {
			mPreviewState = PreviewState.STOPPED;
		}
	};

	// Freeze the Preview for a few seconds and then restart the preview
	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				// Give the user some time to view the image
				Thread.sleep(PREVIEW_PAUSE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Would normally save the image here

			// Restart the preview
			try {
				mCamera.startPreview();
				mPreviewState = PreviewState.RUNNING;
			} catch (Exception e) {
				Log.e(TAG, "Failed to start preview");
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();

		// Release camera so other applications can use it.

		mFrame.setEnabled(false);

		if (null != mCamera) {

			if (mPreviewState == PreviewState.RUNNING) {
				try {
					mCamera.stopPreview();
					mPreviewState = PreviewState.STOPPED;
				} catch (Exception e) {
					Log.e(TAG, "Failed to start preview");
				}
			}
			mCamera.release();
			mCamera = null;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null == mCamera)
			mCamera = getCamera();
		if (null == mCamera)
			finish();
	}

	// Determine the right size for the preview
	private Camera.Size findBestSize(Camera.Parameters parameters, int width,
			int height) {

		List<Camera.Size> supportedSizes = parameters
				.getSupportedPreviewSizes();

		Camera.Size bestSize = supportedSizes.remove(0);

		for (Camera.Size size : supportedSizes) {
			if ((size.width * size.height) > (bestSize.width * bestSize.height)) {
				bestSize = size;
			}
		}

		return bestSize;
	}

}