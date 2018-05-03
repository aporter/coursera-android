package course.examples.broadcastreceiver.goasync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class Receiver extends BroadcastReceiver {
    @SuppressWarnings("FieldCanBeLocal")
    private final String TAG = "Receiver";

    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.i(TAG, "Broadcast Received");

        final PendingResult pendingResult = goAsync();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Must call finish() so the BroadcastReceiver can be recycled.
                pendingResult.finish();
            }

        }).start();

        Toast.makeText(context, "Broadcast Received by Receiver", Toast.LENGTH_LONG).show();

    }

}
