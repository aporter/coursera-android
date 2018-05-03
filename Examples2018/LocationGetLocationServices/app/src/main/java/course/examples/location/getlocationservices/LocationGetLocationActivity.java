package course.examples.location.getlocationservices;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LocationGetLocationActivity extends Activity {

    private static final long ONE_MIN = 1000 * 60;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static final long MEASURE_TIME = 1000 * 30;
    private static final long POLLING_FREQ = 1000 * 10;
    private static final long FASTEST_UPDATE_FREQ = 1000 * 2;
    private static final float MIN_ACCURACY = 25.0f;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;
    private static final int FINE_LOC_PERM_REQ = 200;
    private static final int REQUEST_CHECK_SETTINGS = 201;

    // Views for display location information
    private TextView mAccuracyView;
    private TextView mTimeView;
    private TextView mLatView;
    private TextView mLngView;

    private Location mBestReading;
    private boolean mFirstUpdate = true;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mLocationClient;
    private LocationCallback mLocationCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mAccuracyView = findViewById(R.id.accuracy_view);
        mTimeView = findViewById(R.id.time_view);
        mLatView = findViewById(R.id.lat_view);
        mLngView = findViewById(R.id.lng_view);

        mLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOC_PERM_REQ);
        } else {
            getLastKnownLocation();
        }

    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (LastLocationIsAccurate(location)) {
                                mBestReading = location;
                                updateDisplay(mBestReading);
                            } else {
                                mAccuracyView.setText(R.string.no_initial_reading_string);

                                // Give user time to read message, then continue
                                new Handler(getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Acquire new location readings
                                        continueAcquiringLocations();
                                    }
                                }, 5000);
                            }
                        }
                    });
        }
    }

    private void continueAcquiringLocations() {

        // Start location services
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(POLLING_FREQ);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_FREQ);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        // Used if needed to turn on settings related to location services
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize location requests here.
                if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocationCallback = getLocationCallback();
                    mLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

                    // Schedule a runnable to stop location updates after a period of time
                    Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                        @Override
                        public void run() {
                            mLocationClient.removeLocationUpdates(mLocationCallback);
                        }
                    }, MEASURE_TIME, TimeUnit.MILLISECONDS);
                }
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(LocationGetLocationActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @NonNull
    private LocationCallback getLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                ensureColor();

                // Get new location
                Location location = locationResult.getLastLocation();

                // Determine whether new location is better than current best estimate
                if (null == mBestReading
                        || location.getAccuracy() < mBestReading.getAccuracy()) {

                    // Update best location
                    mBestReading = location;

                    // Update display
                    updateDisplay(location);

                    // Turn off location updates if location reading is sufficiently accurate
                    if (mBestReading.getAccuracy() < MIN_ACCURACY) {
                        mLocationClient.removeLocationUpdates(mLocationCallback);
                    }
                }
            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CHECK_SETTINGS == requestCode) {
            if (resultCode == RESULT_OK) {
                // Try to start location updates one more time
                continueAcquiringLocations();
            } else {
                Toast.makeText(getApplicationContext(), R.string.location_services_not_enabled_string, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (FINE_LOC_PERM_REQ == requestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastKnownLocation();
        }
    }

    @Override
    protected void onStop() {
        // Stop updates
        mLocationClient.removeLocationUpdates(mLocationCallback);

        super.onStop();
    }

    // Determine whether best reading is as accurate as minAccuracy and
    // was taken no longer then maxAge milliseconds ago
    private boolean LastLocationIsAccurate(Location location) {
        return (null != location && location.getAccuracy() <= LocationGetLocationActivity.MIN_LAST_READ_ACCURACY && ((System.currentTimeMillis() - location.getTime()) < LocationGetLocationActivity.FIVE_MIN));
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