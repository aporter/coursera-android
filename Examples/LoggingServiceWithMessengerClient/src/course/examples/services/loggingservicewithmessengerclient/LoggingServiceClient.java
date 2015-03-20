package course.examples.services.loggingservicewithmessengerclient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoggingServiceClient extends Activity {

	private final static String MESSAGE_KEY = "course.examples.services.loggingservicewithmessenger.MESSAGE";
	private final static int LOG_OP = 1;
	private final static String TAG = "LoggingServiceClient";

	// Intent used for binding to LoggingService
	private final static Intent mLoggingServiceIntent = new Intent(
			"course.examples.services.loggingservicewithmessenger.loggingservice_action");

	private Messenger mMessengerToLoggingService;
	private boolean mIsBound;

	// Object implementing Service Connection callbacks
	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {

			// Messenger object connected to the LoggingService
			mMessengerToLoggingService = new Messenger(service);

			mIsBound = true;

		}

		public void onServiceDisconnected(ComponentName className) {

			mMessengerToLoggingService = null;

			mIsBound = false;

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		final Button buttonStart = (Button) findViewById(R.id.buttonStart);
		buttonStart.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (mIsBound) {

					// Send Message to the Logging Service
					logMessageToService();

				}

			}
		});
	}

	// Create a Message and sent it to the LoggingService
	// via the mMessenger Object

	private void logMessageToService() {

		// Create Message 
		Message msg = Message.obtain(null, LOG_OP);
		Bundle bundle = new Bundle();
		bundle.putString(MESSAGE_KEY, "Log This Message");
		msg.setData(bundle);

		try {
			
			// Send Message to LoggingService using Messenger
			mMessengerToLoggingService.send(msg);

		} catch (RemoteException e) {
			Log.e(TAG, e.toString());
		}
	}

	// Bind to LoggingService
	@Override
	protected void onResume() {
		super.onResume();

		bindService(mLoggingServiceIntent, mConnection,
				Context.BIND_AUTO_CREATE);

	}

	// Unbind from the LoggingService
	@Override
	protected void onPause() {

		if (mIsBound)
			unbindService(mConnection);

		super.onPause();
	}

}