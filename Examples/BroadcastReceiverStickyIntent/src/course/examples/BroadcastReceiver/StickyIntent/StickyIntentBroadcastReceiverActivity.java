package course.examples.BroadcastReceiver.StickyIntent;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

public class StickyIntentBroadcastReceiverActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		final TextView currentStateView = (TextView) findViewById(R.id.level);

		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {

					String age = "Reading taken recently";
					if (isInitialStickyBroadcast()) {
						age = "Reading may be stale";
					}

					currentStateView.setText("Current Battery Level:"
							+ String.valueOf(intent.getIntExtra(
									BatteryManager.EXTRA_LEVEL, -1))
							+ System.getProperty("line.separator") + age);
				}
			}
		}, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}
}