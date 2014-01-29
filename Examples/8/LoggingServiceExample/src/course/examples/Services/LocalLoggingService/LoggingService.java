package course.examples.Services.LocalLoggingService;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class LoggingService extends IntentService {

	public static String EXTRA_LOG = "course.examples.Services.Logging.MESSAGE";
	private static final String TAG = "LoggingService";

	public LoggingService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.i(TAG, intent.getStringExtra(EXTRA_LOG));

	}
}
