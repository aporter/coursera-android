package course.examples.broadcastreceiver.singlebroadcaststaticregistration;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SimpleBroadcastActivity extends Activity {

    private static final String CUSTOM_INTENT = "course.examples.broadcastreceiver.singlebroadcaststaticregistration.SHOW_TOAST";
    private static final String TAG = "SimpleBroadcastActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onClick(@SuppressWarnings("unused") View v) {
        Log.i(TAG, "Broadcast sent");

        Intent intent = new Intent(CUSTOM_INTENT);
        intent.setPackage("course.examples.broadcastreceiver.singlebroadcaststaticregistration");
        sendBroadcast(intent, Manifest.permission.VIBRATE);
    }
}