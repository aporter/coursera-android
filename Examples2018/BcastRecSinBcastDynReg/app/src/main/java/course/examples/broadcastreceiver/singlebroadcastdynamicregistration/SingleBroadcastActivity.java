package course.examples.broadcastreceiver.singlebroadcastdynamicregistration;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

public class SingleBroadcastActivity extends Activity {

    private static final String CUSTOM_INTENT = "course.examples.broadcastreceiver.singlebroadcastdynamicregistration.SHOW_TOAST";
    private final IntentFilter intentFilter = new IntentFilter(CUSTOM_INTENT);
    private final Receiver receiver = new Receiver();

    private LocalBroadcastManager mBroadcastMgr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBroadcastMgr = LocalBroadcastManager
                .getInstance(getApplicationContext());

        setContentView(R.layout.main);

    }

    // Called when Button is clicked
    public void onClick(@SuppressWarnings("unused") View v) {
        mBroadcastMgr.sendBroadcast(
                new Intent(CUSTOM_INTENT).setFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBroadcastMgr.registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onStop() {
        mBroadcastMgr.unregisterReceiver(receiver);
        super.onStop();
    }
}