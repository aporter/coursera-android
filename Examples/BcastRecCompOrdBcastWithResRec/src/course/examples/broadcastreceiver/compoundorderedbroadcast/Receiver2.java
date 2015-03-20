package course.examples.broadcastreceiver.compoundorderedbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver2 extends BroadcastReceiver {

	private final String TAG = "Receiver2";

	@Override
	public void onReceive(Context context, Intent intent) {

		Log.i(TAG, "INTENT RECEIVED by Receiver2");

		String tmp = getResultData() == null ? "" : getResultData();
		setResultData(tmp + "Receiver 2:");
	}

}
