package course.examples.Bluetooth.SetupAndTransferData;

import java.io.IOException;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class ConnectThread extends Thread {
	private BluetoothSocket mBluetoothSocket;
	private final BluetoothDevice mDevice;
	private final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
			.getDefaultAdapter();
	private final Handler mHandler;

	public ConnectThread(String deviceID, Handler handler) {
		mDevice = mBluetoothAdapter.getRemoteDevice(deviceID);
		mHandler = handler;
		try {
			mBluetoothSocket = mDevice
					.createRfcommSocketToServiceRecord(DataTransferActivity.APP_UUID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		mBluetoothAdapter.cancelDiscovery();
		try {
			mBluetoothSocket.connect();
			manageConnectedSocket();
		} catch (IOException connectException) {
			try {
				mBluetoothSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void manageConnectedSocket() {
		ConnectionThread conn = new ConnectionThread(
				mBluetoothSocket, mHandler);
		mHandler.obtainMessage(
				DataTransferActivity.SOCKET_CONNECTED, conn)
				.sendToTarget();
		conn.start();
	}
	
	public void cancel() {
		try {
			mBluetoothSocket.close();
		} catch (IOException e) {
		}
	}

}
