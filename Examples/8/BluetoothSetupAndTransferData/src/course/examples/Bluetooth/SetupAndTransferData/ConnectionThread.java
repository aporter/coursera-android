package course.examples.Bluetooth.SetupAndTransferData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class ConnectionThread extends Thread {
	BluetoothSocket mBluetoothSocket;
	private final Handler mHandler;
	private InputStream mInStream;
	private OutputStream mOutStream;

	ConnectionThread(BluetoothSocket socket, Handler handler){
		super();
		mBluetoothSocket = socket;
		mHandler = handler;
		try {
			mInStream = mBluetoothSocket.getInputStream();
			mOutStream = mBluetoothSocket.getOutputStream();
		} catch (IOException e) {
		}
	}

	@Override
	public void run() {
			byte[] buffer = new byte[1024];
			int bytes;
		while (true) {
			try {
				bytes = mInStream.read(buffer);
				String data = new String(buffer, 0, bytes);
				mHandler.obtainMessage(
						DataTransferActivity.DATA_RECEIVED,
						data).sendToTarget();
			} catch (IOException e) {
				break;
			}
		}
	}

	public void write(byte[] bytes) {
		try {
			mOutStream.write(bytes);
		} catch (IOException e) {
		}
	}
}
