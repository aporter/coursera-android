package course.examples.location.getlocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LocationGetLocationActivity extends Activity {

    private static final long ONE_MIN = 1000 * 60;
    private static final long TWO_MIN = ONE_MIN * 2;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static final long MEASURE_TIME = 1000 * 30;
    private static final long POLLING_FREQ = 1000 * 10;
    private static final float MIN_ACCURACY = 25.0f;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;
    private static final float MIN_DISTANCE = 10.0f;
    private static final int REQUEST_FINE_LOC_PERM_ONCREATE = 200;
    private static final int REQUEST_FINE_LOC_PERM_ONRESUME = 201;

    // Views for display location information
    private TextView mAccuracyView;
    private TextView mTimeView;
    private TextView mLatView;
    private TextView mLngView;

    // Current best location estimate
    private Location mBestReading;

    // Reference to the LocationManager and LocationListener
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private final String TAG = "LocationGetLocation";
    private static boolean mFirstUpdate = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mAccuracyView = findViewById(R.id.accuracy_view);
        mTimeView = findViewById(R.id.time_view);
        mLatView = findViewById(R.id.lat_view);
        mLngView = findViewById(R.id.lng_view);

        // Acquire reference to the LocationManager
        if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
            finish();

        mLocationListener = getLocationListener();

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOC_PERM_ONCREATE);
        } else {
            getAndDisplayBestLastKnownLocation();
        }
    }

    private LocationListener getLocationListener() {
        return new LocationListener() {
            // Called back when location changes
            public void onLocationChanged(Location location) {
                ensureColor();

                // Determine whether new location is better than current best estimate
                if (null == mBestReading || location.getAccuracy() <= mBestReading.getAccuracy()) {

                    // Update best estimate
                    mBestReading = location;

                    // Update display
                    updateDisplay(location);

                    // If location is accurate enough stop listening
                    if (mBestReading.getAccuracy() < MIN_ACCURACY)
                        mLocationManager.removeUpdates(mLocationListener);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {/* Not implemented */}

            public void onProviderEnabled(String provider) { /* Not implemented */ }

            public void onProviderDisabled(String provider) { /* Not implemented */ }

        };
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Determine whether initial reading is "good enough".
        // If not, register for further location updates
        if (null == mBestReading
                || mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
                || mBestReading.getTime() < System.currentTimeMillis() - TWO_MIN) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOC_PERM_ONRESUME);
            } else {
                continueOnResume();
            }
        }
    }

    private void continueOnResume() {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Register for network location updates
            if (null != mLocationManager
                    .getProvider(LocationManager.NETWORK_PROVIDER)) {

                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, POLLING_FREQ,
                        MIN_DISTANCE, mLocationListener);
            }

            // Register for GPS location updates
            if (null != mLocationManager
                    .getProvider(LocationManager.GPS_PROVIDER)) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, POLLING_FREQ,
                        MIN_DISTANCE, mLocationListener);
            }

            // Schedule a runnable to unregister LocationListeners
            Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "location updates cancelled");
                    mLocationManager.removeUpdates(mLocationListener);
                }
            }, MEASURE_TIME, TimeUnit.MILLISECONDS);
        }

    }

    // Unregister LocationListeners
    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (REQUEST_FINE_LOC_PERM_ONCREATE == requestCode) {
                getAndDisplayBestLastKnownLocation();
            } else if (REQUEST_FINE_LOC_PERM_ONRESUME == requestCode) {
                continueOnResume();
            }
        } else {
            Toast.makeText(this, "This app requires ACCESS_FINE_LOCATION permission", Toast.LENGTH_LONG).show();
        }
    }

    private void getAndDisplayBestLastKnownLocation() {
        // Get best last location measurement
        mBestReading = getBestLastKnownLocation();

        // Display last reading information
        if (null != mBestReading) {
            updateDisplay(mBestReading);
        } else {
            mAccuracyView.setText(R.string.no_initial_reading_string);
        }
    }

    // Get the last known location from all providers return best reading that is as accurate as minAccuracy and
    // was taken no longer then minAge milliseconds ago. If none, return null.
    private Location getBestLastKnownLocation() {
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestAge = Long.MIN_VALUE;

        List<String> matchingProviders = mLocationManager.getAllProviders();
        for (String provider : matchingProviders) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = mLocationManager.getLastKnownLocation(provider);

                if (location != null) {

                    float accuracy = location.getAccuracy();
                    long time = location.getTime();

                    if (accuracy < bestAccuracy) {
                        bestResult = location;
                        bestAccuracy = accuracy;
                        bestAge = time;
                    }
                }
            }
        }

        // Return best reading or null
        if (bestAccuracy > LocationGetLocationActivity.MIN_LAST_READ_ACCURACY
                || (System.currentTimeMillis() - bestAge) > LocationGetLocationActivity.FIVE_MIN) {
            return null;
        } else {
            return bestResult;
        }
    }

    // Update display
    private void updateDisplay(Location location) {

        mAccuracyView.setText(getString(R.string.accuracy_string, location.getAccuracy()));

        mTimeView.setText(getString(R.string.time_string,
                new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale
                        .getDefault()).format(new Date(location.getTime()))));
        mLatView.setText(getString(R.string.long_string, location.getLongitude()));
        mLngView.setText(getString(R.string.lat_string, location.getLatitude()));

    }

    private void ensureColor() {
        if (mFirstUpdate) {
            int mTextViewColor = Color.GRAY;
            setTextViewColor(mTextViewColor);
            mFirstUpdate = false;
        }
    }

    private void setTextViewColor(int color) {
        mAccuracyView.setTextColor(color);
        mTimeView.setTextColor(color);
        mLatView.setTextColor(color);
        mLngView.setTextColor(color);

    }

}