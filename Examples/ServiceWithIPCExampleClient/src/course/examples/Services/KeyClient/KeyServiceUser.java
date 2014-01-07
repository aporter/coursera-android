package course.examples.Services.KeyClient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import course.examples.Services.KeyCommon.KeyGenerator;

public class KeyServiceUser extends Activity {

	protected static final String TAG = "KeyServiceUser";
	private KeyGenerator mKeyGeneratorService;
	private boolean mIsBound;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.main);

		final TextView output = (TextView) findViewById(R.id.output);

		final Button goButton = (Button) findViewById(R.id.go_button);
		goButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				try {
					
					// Call KeyGenerator and get a new ID
					if (mIsBound)
						output.setText(mKeyGeneratorService.getKey());

				} catch (RemoteException e) {

					Log.e(TAG, e.toString());

				}
			}
		});
	}

	// Bind to KeyGenerator Service
	@Override
	protected void onResume() {
		super.onResume();

		if (!mIsBound) {
			
			Intent intent = new Intent(KeyGenerator.class.getName());
			bindService(intent, this.mConnection, Context.BIND_AUTO_CREATE);

		}
	}

	// Unbind from KeyGenerator Service
	@Override
	protected void onPause() {

		if (mIsBound) {

			unbindService(this.mConnection);

		}

		super.onPause();
	}

	private final ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder iservice) {

			mKeyGeneratorService = KeyGenerator.Stub.asInterface(iservice);

			mIsBound = true;

		}

		public void onServiceDisconnected(ComponentName className) {

			mKeyGeneratorService = null;

			mIsBound = false;

		}
	};

}
