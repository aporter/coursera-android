package course.examples.checkbroadcastintents;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CheckBroadcastIntents extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		final EditText intentText = (EditText) findViewById(R.id.editText1);
		final TextView output = (TextView) findViewById(R.id.textView2);
		final Button checkButton = (Button) findViewById(R.id.CheckIntentButton);

		checkButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> acts = CheckBroadcastIntents.getBroadcastReceiversForAction(
						CheckBroadcastIntents.this, intentText.getText().toString());
				String tmp = new String();
				for (String comp : acts) {
					tmp += comp + "\n";
				}
				output.setText(tmp);
			}
		});
	}

	public static List<String> getBroadcastReceiversForAction(Context context,
			String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		final List<ResolveInfo> list = packageManager.queryBroadcastReceivers(intent, 0);
		final List<String> acts = new ArrayList<String>();
		for (ResolveInfo ri : list) {
			acts.add(ri.activityInfo.name);
		}
		return acts;

	}

}