package course.examples.Sensors.ShowValues;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorFilteredValuesActivity extends Activity implements
		SensorEventListener {

	// References to SensorManager and accelerometer

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;

	// Filtering constant

	private final float mAlpha = 0.8f;

	// Arrays for storing filtered values
	private float[] mGravity = new float[3];
	private float[] mAccel = new float[3];

	private TextView mXValueView, mYValueView, mZValueView, 
					mXGravityView, mYGravityView, mZGravityView, 
					mXAccelView, mYAccelView, mZAccelView;

	private long mLastUpdate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mXValueView = (TextView) findViewById(R.id.x_value_view);
		mYValueView = (TextView) findViewById(R.id.y_value_view);
		mZValueView = (TextView) findViewById(R.id.z_value_view);

		mXGravityView = (TextView) findViewById(R.id.x_lowpass_view);
		mYGravityView = (TextView) findViewById(R.id.y_lowpass_view);
		mZGravityView = (TextView) findViewById(R.id.z_lowpass_view);

		mXAccelView = (TextView) findViewById(R.id.x_highpass_view);
		mYAccelView = (TextView) findViewById(R.id.y_highpass_view);
		mZAccelView = (TextView) findViewById(R.id.z_highpass_view);

		// Get reference to SensorManager
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Get reference to Accelerometer
		if (null == (mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)))
			finish();

		mLastUpdate = System.currentTimeMillis();
	}

	// Register listener
	@Override
	protected void onResume() {
		super.onResume();

		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_UI);

	}

	// Unregister listener
	@Override
	protected void onPause() {
		super.onPause();

		mSensorManager.unregisterListener(this);
	}
	
	// Process new reading
	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

			long actualTime = System.currentTimeMillis();

			if (actualTime - mLastUpdate > 500) {

				mLastUpdate = actualTime;

				float rawX = event.values[0];
				float rawY = event.values[1];
				float rawZ = event.values[2];

				// Apply low-pass filter
				mGravity[0] = lowPass(rawX, mGravity[0]);
				mGravity[1] = lowPass(rawY, mGravity[1]);
				mGravity[2] = lowPass(rawZ, mGravity[2]);

				// Apply high-pass filter
				mAccel[0] = highPass(rawX, mGravity[0]);
				mAccel[1] = highPass(rawY, mGravity[1]);
				mAccel[2] = highPass(rawZ, mGravity[2]);

				mXValueView.setText(String.valueOf(rawX));
				mYValueView.setText(String.valueOf(rawY));
				mZValueView.setText(String.valueOf(rawZ));

				mXGravityView.setText(String.valueOf(mGravity[0]));
				mYGravityView.setText(String.valueOf(mGravity[1]));
				mZGravityView.setText(String.valueOf(mGravity[2]));

				mXAccelView.setText(String.valueOf(mAccel[0]));
				mYAccelView.setText(String.valueOf(mAccel[1]));
				mZAccelView.setText(String.valueOf(mAccel[2]));

			}
		}
	}

	// Deemphasize transient forces
	private float lowPass(float current, float gravity) {

		return gravity * mAlpha + current * (1 - mAlpha);

	}

	// Deemphasize constant forces
	private float highPass(float current, float gravity) {

		return current - gravity;

	}



	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// NA
	}
}