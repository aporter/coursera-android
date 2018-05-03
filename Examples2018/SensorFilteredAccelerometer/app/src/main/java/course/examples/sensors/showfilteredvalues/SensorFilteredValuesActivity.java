package course.examples.sensors.showfilteredvalues;

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

    // Arrays for storing filtered values
    private final float[] mGravity = new float[3];
    private final float[] mAccel = new float[3];

    private TextView mXValueView, mYValueView, mZValueView,
            mXGravityView, mYGravityView, mZGravityView,
            mXAccelView, mYAccelView, mZAccelView;

    private long mLastUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mXValueView = findViewById(R.id.x_value_view);
        mYValueView = findViewById(R.id.y_value_view);
        mZValueView = findViewById(R.id.z_value_view);

        mXGravityView = findViewById(R.id.x_lowpass_view);
        mYGravityView = findViewById(R.id.y_lowpass_view);
        mZGravityView = findViewById(R.id.z_lowpass_view);

        mXAccelView = findViewById(R.id.x_highpass_view);
        mYAccelView = findViewById(R.id.y_highpass_view);
        mZAccelView = findViewById(R.id.z_highpass_view);

        // Get reference to SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (null != mSensorManager) {
            // Get reference to Accelerometer
            mAccelerometer = mSensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (null == mAccelerometer) finish();

            mLastUpdate = System.currentTimeMillis();
        }
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

        float mAlpha = 0.8f;
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