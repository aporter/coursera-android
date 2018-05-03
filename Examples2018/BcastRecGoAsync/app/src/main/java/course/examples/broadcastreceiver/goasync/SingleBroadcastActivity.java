package course.examples.broadcastreceiver.goasync;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;

import course.examples.broadcastreceiver.goasync.R;

public class SingleBroadcastActivity extends Activity {

    private static final String CUSTOM_INTENT = "course.examples.broadcastreceiver.goasync.SHOW_TOAST";
    private final IntentFilter intentFilter = new IntentFilter(CUSTOM_INTENT);
    private final Receiver receiver = new Receiver();

    private LocalBroadcastManager mBroadcastMgr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

    }

    // Called when Button is clicked
    public void onClick(@SuppressWarnings("unused") View v) {
        sendBroadcast(new Intent(CUSTOM_INTENT).setPackage("course.examples.broadcastreceiver.goasync"));
    }
}