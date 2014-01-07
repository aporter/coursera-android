package course.examples.BroadcastReceiver.CompoundOrderedBroadcast;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CompoundOrderedBroadcastWithResultReceiver extends Activity {

	static final String CUSTOM_INTENT = "course.examples.BroadcastReceiver.show_toast";

	private final Receiver1 mReceiver1 = new Receiver1();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		IntentFilter intentFilter = new IntentFilter(CUSTOM_INTENT);
		intentFilter.setPriority(3);
		registerReceiver(mReceiver1, intentFilter);

		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendOrderedBroadcast(new Intent(CUSTOM_INTENT), null,
						new BroadcastReceiver() {
							@Override
							public void onReceive(Context context, Intent intent) {
								Toast.makeText(context,
										"Final Result is " + getResultData(),
										Toast.LENGTH_LONG).show();
							}
						}, null, 0, null, null);
			}
		});
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mReceiver1);
		super.onDestroy();
	}
}