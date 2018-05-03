package course.examples.broadcastreceiver.resultreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver1 extends BroadcastReceiver {
	
	private final String TAG = "Receiver1";

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.i(TAG, "INTENT RECEIVED by Receiver1");

		String tmp = getResultData() == null ? "" : getResultData();
		setResultData(tmp + ":Receiver 1:");
	}

}
