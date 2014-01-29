package course.examples.Bluetooth.SetupAndTransferData;

import java.util.List;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ShowDevices extends ListActivity {
	BluetoothAdapter mBluetoothAdapter = null;
	ArrayAdapter<String> mArrayAdapter = null;

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				mArrayAdapter
						.add(device.getName() + "\n" + device.getAddress());
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		final ListView lv = getListView();
		final TextView footer = new TextView(this);
		footer.setText("Discover More Devices");
		lv.setFooterDividersEnabled(true);
		lv.addFooterView(footer, null, true);

		final List<String> devices = getIntent().getStringArrayListExtra("devices");
		mArrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item,devices);
		setListAdapter(mArrayAdapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,long id) {
				if (parent.getAdapter().getItemViewType(pos) == AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER) {
					mBluetoothAdapter.startDiscovery();
				} else {
					String tmp = (String) parent.getItemAtPosition(pos);
					BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(tmp.split("\n")[1]);
					Intent data = new Intent();
					data.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
					setResult(RESULT_OK, data);
					finish();
				}
			}
		});
	}

	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
	}

	protected void onPause() {
		unregisterReceiver(mReceiver);
		super.onPause();
	}
}
