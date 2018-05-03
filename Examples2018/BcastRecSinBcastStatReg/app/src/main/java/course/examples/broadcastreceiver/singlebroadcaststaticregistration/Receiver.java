package course.examples.broadcastreceiver.singlebroadcaststaticregistration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {

    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "Receiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "Broadcast Received");

        Vibrator v = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);

        Toast.makeText(context, "Broadcast Received by Receiver", Toast.LENGTH_LONG).show();

    }

}
