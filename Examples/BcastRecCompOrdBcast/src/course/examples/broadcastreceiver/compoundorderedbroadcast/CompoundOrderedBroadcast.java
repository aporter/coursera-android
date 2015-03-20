package course.examples.broadcastreceiver.compoundorderedbroadcast;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CompoundOrderedBroadcast extends Activity {

	static final String CUSTOM_INTENT = "course.examples.BroadcastReceiver.show_toast";

	private final Receiver1 mReceiver = new Receiver1();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		IntentFilter intentFilter = new IntentFilter(CUSTOM_INTENT);
		intentFilter.setPriority(3);
		registerReceiver(mReceiver, intentFilter);

		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendOrderedBroadcast(new Intent(CUSTOM_INTENT),
						android.Manifest.permission.VIBRATE);
			}
		});
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
}