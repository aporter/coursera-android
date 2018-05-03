package course.examples.broadcastreceiver.compoundbroadcast;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class CompoundBroadcast extends Activity {

    private static final String CUSTOM_INTENT = "course.examples.broadcastreceiver.compoundbroadcast.SHOW_TOAST";
    private final Receiver1 mReceiver1 = new Receiver1();
    private final IntentFilter mIntentFilter = new IntentFilter(CUSTOM_INTENT);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onClick(@SuppressWarnings("unused") View v) {
        Intent intent = new Intent(CUSTOM_INTENT).setPackage("course.examples.broadcastreceiver.compoundbroadcast").setFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION);
        sendBroadcast(intent, Manifest.permission.VIBRATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mReceiver1, mIntentFilter);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver1);
        super.onStop();
    }
}