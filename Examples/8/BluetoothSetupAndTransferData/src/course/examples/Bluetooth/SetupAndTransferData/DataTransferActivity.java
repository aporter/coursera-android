package course.examples.Bluetooth.SetupAndTransferData;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DataTransferActivity extends Activity {
	private static final String TAG = "DataTransferActivity";
	private static final int REQUEST_ENABLE_BT = 0;
	private static final int SELECT_SERVER = 1;
	public static final int DATA_RECEIVED = 3;
	public static final int SOCKET_CONNECTED = 4;

	public static final UUID APP_UUID = UUID
			.fromString("aeb9f938-a1a3-4947-ace2-9ebd0c67adf1");
	private Button serverButton, clientButton;
	private TextView tv = null;
	
	private BluetoothAdapter mBluetoothAdapter = null;
	private ConnectionThread mBluetoothConnection = null;
	private String data;
	private boolean mServerMode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Log.i(TAG, "Bluetooth not supported");
			finish();
		}

		setContentView(R.layout.main);
		tv = (TextView) findViewById(R.id.text_window);
		serverButton = (Button) findViewById(R.id.server_button);
		serverButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startAsServer();
				mServerMode = true;
			}
		});

		clientButton = (Button) findViewById(R.id.client_button);
		clientButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectServer();
			}
		});

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBluetoothIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
		} else {
			setButtonsEnabled(true);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
			setButtonsEnabled(true);
		} else if (requestCode == SELECT_SERVER
				&& resultCode == RESULT_OK) {
			BluetoothDevice device = data
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			connectToBluetoothServer(device.getAddress());
		}
	}

	private void startAsServer() {
		setButtonsEnabled(false);
		new AcceptThread(mHandler).start();
	}

	private void selectServer() {
		setButtonsEnabled(false);
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		ArrayList<String> pairedDeviceStrings = new ArrayList<String>();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				pairedDeviceStrings.add(device.getName() + "\n"
						+ device.getAddress());
			}
		}
		Intent showDevicesIntent = new Intent(this, ShowDevices.class);
		showDevicesIntent.putStringArrayListExtra("devices", pairedDeviceStrings);
		startActivityForResult(showDevicesIntent, SELECT_SERVER);
	}

	private void connectToBluetoothServer(String id) {
		tv.setText("Connecting to Server...");
		new ConnectThread(id, mHandler).start();
	}

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SOCKET_CONNECTED: {
				mBluetoothConnection = (ConnectionThread) msg.obj;
				if (!mServerMode)
					mBluetoothConnection.write("this is a message".getBytes());
				break;
			}
			case DATA_RECEIVED: {
				data = (String) msg.obj;
				tv.setText(data);
				if (mServerMode)
					mBluetoothConnection.write(data.getBytes());
			}
			default:
				break;
			}
		}
	};

	private void setButtonsEnabled(boolean state) {
		serverButton.setEnabled(state);
		clientButton.setEnabled(state);
	}
	
}