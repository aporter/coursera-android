package course.examples.Threading.ThreadingHandlerMessages;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class HandlerMessagesActivity extends Activity {
	private final static int SET_PROGRESS_BAR_VISIBILITY = 0;
	private final static int PROGRESS_UPDATE = 1;
	private final static int SET_BITMAP = 2;

	private ImageView mImageView;
	private ProgressBar mProgressBar;
	private int mDelay = 500;

	static class UIHandler extends Handler {
		WeakReference<HandlerMessagesActivity> mParent;

		public UIHandler(WeakReference<HandlerMessagesActivity> parent) {
			mParent = parent;
		}

		@Override
		public void handleMessage(Message msg) {
			HandlerMessagesActivity parent = mParent.get();
			if (null != parent) {
				switch (msg.what) {
				case SET_PROGRESS_BAR_VISIBILITY: {
					parent.getProgressBar().setVisibility((Integer) msg.obj);
					break;
				}
				case PROGRESS_UPDATE: {
					parent.getProgressBar().setProgress((Integer) msg.obj);
					break;
				}
				case SET_BITMAP: {
					parent.getImageView().setImageBitmap((Bitmap) msg.obj);
					break;
				}
				}
			}
		}

	}

	Handler handler = new UIHandler(new WeakReference<HandlerMessagesActivity>(
			this));

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mImageView = (ImageView) findViewById(R.id.imageView);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

		final Button button = (Button) findViewById(R.id.loadButton);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new Thread(new LoadIconTask(R.drawable.painter, handler))
						.start();
			}
		});

		final Button otherButton = (Button) findViewById(R.id.otherButton);
		otherButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(HandlerMessagesActivity.this, "I'm Working",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	private class LoadIconTask implements Runnable {
		private final int resId;
		private final Handler handler;

		LoadIconTask(int resId, Handler handler) {
			this.resId = resId;
			this.handler = handler;
		}

		public void run() {

			Message msg = handler.obtainMessage(SET_PROGRESS_BAR_VISIBILITY,
					ProgressBar.VISIBLE);
			handler.sendMessage(msg);

			final Bitmap tmp = BitmapFactory.decodeResource(getResources(),
					resId);

			for (int i = 1; i < 11; i++) {
				sleep();
				msg = handler.obtainMessage(PROGRESS_UPDATE, i * 10);
				handler.sendMessage(msg);
			}

			msg = handler.obtainMessage(SET_BITMAP, tmp);
			handler.sendMessage(msg);

			msg = handler.obtainMessage(SET_PROGRESS_BAR_VISIBILITY,
					ProgressBar.INVISIBLE);
			handler.sendMessage(msg);
		}

		private void sleep() {
			try {
				Thread.sleep(mDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public ImageView getImageView() {
		return mImageView;
	}

	public ProgressBar getProgressBar() {
		return mProgressBar;
	}

}
