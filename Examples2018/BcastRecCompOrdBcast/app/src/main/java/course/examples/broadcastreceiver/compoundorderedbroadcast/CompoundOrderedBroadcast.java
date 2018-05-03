package course.examples.broadcastreceiver.compoundorderedbroadcast;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class CompoundOrderedBroadcast extends Activity {

	private static final String CUSTOM_INTENT = "course.examples.BroadcastReceiver.compoundorderedbroadcast.SHOW_TOAST";
	private final Receiver1 mReceiver = new Receiver1();

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }

	public void onClick(@SuppressWarnings("unused") View v) {
		sendOrderedBroadcast(new Intent(CUSTOM_INTENT).setPackage("course.examples.broadcastreceiver.compoundorderedbroadcast").setFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION),
				android.Manifest.permission.VIBRATE);
	}

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(CUSTOM_INTENT);
        intentFilter.setPriority(3);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
	protected void onStop() {
		unregisterReceiver(mReceiver);
		super.onStop();
	}
}