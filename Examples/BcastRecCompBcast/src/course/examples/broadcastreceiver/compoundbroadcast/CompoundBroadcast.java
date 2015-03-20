package course.examples.broadcastreceiver.compoundbroadcast;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CompoundBroadcast extends Activity {

	private static final String CUSTOM_INTENT = "course.examples.BroadcastReceiver.show_toast";
	private final Receiver1 receiver1 = new Receiver1();
	private final IntentFilter intentFilter = new IntentFilter(CUSTOM_INTENT);

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		registerReceiver(receiver1, intentFilter);

		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendBroadcast(new Intent(CUSTOM_INTENT),android.Manifest.permission.VIBRATE);
			}
		});

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver1);
		super.onDestroy();
	}
}