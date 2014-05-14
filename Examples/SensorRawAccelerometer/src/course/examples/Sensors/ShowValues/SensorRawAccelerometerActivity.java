package course.examples.Sensors.ShowValues;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorRawAccelerometerActivity extends Activity implements
		SensorEventListener {

	private static final int UPDATE_THRESHOLD = 500;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;

	private TextView mXValueView, mYValueView, mZValueView;
	private long mLastUpdate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mXValueView = (TextView) findViewById(R.id.x_value_view);
		mYValueView = (TextView) findViewById(R.id.y_value_view);
		mZValueView = (TextView) findViewById(R.id.z_value_view);

		// Get reference to SensorManager
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Get reference to Accelerometer
		if (null == (mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)))
			finish();

	}

	// Register listener
	@Override
	protected void onResume() {
		super.onResume();

		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_UI);

		mLastUpdate = System.currentTimeMillis();

	}

	// Unregister listener
	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(this);
		super.onPause();
	}

	// Process new reading
	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
		
			long actualTime = System.currentTimeMillis();
			
			if (actualTime - mLastUpdate > UPDATE_THRESHOLD) {

				mLastUpdate = actualTime;

				float x = event.values[0], y = event.values[1], z = event.values[2];

				mXValueView.setText(String.valueOf(x));
				mYValueView.setText(String.valueOf(y));
				mZValueView.setText(String.valueOf(z));

			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// N/A
	}
}