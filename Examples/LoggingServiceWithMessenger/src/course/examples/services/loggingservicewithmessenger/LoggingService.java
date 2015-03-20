package course.examples.services.loggingservicewithmessenger;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class LoggingService extends Service {

	private final static String MESSAGE_KEY = "course.examples.services.loggingservicewithmessenger.MESSAGE";
	private final static int LOG_OP = 1;

	private static final String TAG = "LoggingService";

	// Messenger Object that receives Messages from connected clients
	final Messenger mMessenger = new Messenger(new IncomingMessagesHandler());

	// Handler for incoming Messages
	static class IncomingMessagesHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			
			case LOG_OP:

				Log.i(TAG, msg.getData().getString(MESSAGE_KEY));

				break;
			
			default:
				
				super.handleMessage(msg);
			
			}
		}
	}

	// Returns the Binder for the mMessenger, which allows
	// the client to send Messages through the Messenger
	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}
}
