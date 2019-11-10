/* 
 * Requires Google Play Services 
 * Doesn't run on emulator 
*/

package course.examples.location.getlocationservices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationGetLocationActivity extends Activity implements
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, LocationListener {

	private static final long ONE_MIN = 1000 * 60;
	private static final long TWO_MIN = ONE_MIN * 2;
	private static final long FIVE_MIN = ONE_MIN * 5;
	private static final long MEASURE_TIME = 1000 * 30;
	private static final long POLLING_FREQ = 1000 * 10;
	private static final long FASTES_UPDATE_FREQ = 1000 * 2;
	private static final float MIN_ACCURACY = 25.0f;
	private static final float MIN_LAST_READ_ACCURACY = 500.0f;

	// Define an object that holds accuracy and frequency parameters
	LocationRequest mLocationRequest;

	// Views for display location information
	private TextView mAccuracyView;
	private TextView mTimeView;
	private TextView mLatView;
	private TextView mLngView;

	private int mTextViewColor = Color.GRAY;

	// Current best location estimate
	private Location mBestReading;

	private final String TAG = "LocationGetLocationActivity";

	private boolean mFirstUpdate = true;
	private GoogleApiClient mGoogleApiClient = null;
	private Location mCurrentLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!servicesAvailable())
			finish();

		setContentView(R.layout.main);

		mAccuracyView = (TextView) findViewById(R.id.accuracy_view);
		mTimeView = (TextView) findViewById(R.id.time_view);
		mLatView = (TextView) findViewById(R.id.lat_view);
		mLngView = (TextView) findViewById(R.id.lng_view);

		// Create new Location Client. This class will handle callbacks
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();

		// Create and define the LocationRequest
		mLocationRequest = LocationRequest.create();

		// Use high accuracy
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		// Update every 10 seconds
		mLocationRequest.setInterval(POLLING_FREQ);

		// Recieve updates no more often than every 2 seconds
		mLocationRequest.setFastestInterval(FASTES_UPDATE_FREQ);

	}
	
	@Override
	protected void onStart() {
		super.onStart();

		// Connect to LocationServices
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {

		mGoogleApiClient.disconnect();

		super.onStop();
	}


	// Called back when location changes

	@Override
	public void onLocationChanged(Location location) {

		ensureColor();

		// Determine whether new location is better than current best
		// estimate

		if (null == mBestReading
				|| location.getAccuracy() < mBestReading.getAccuracy()) {

			// Update best estimate
			mBestReading = location;

			// Update display
			updateDisplay(location);

			if (mBestReading.getAccuracy() < MIN_ACCURACY)
				LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

		}
	}

	@Override
	public void onConnected(Bundle dataBundle) {

		// Get first reading. Get additional location updates if necessary

		if (servicesAvailable()) {
			// Get best last location measurement meeting criteria
			mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY,
					FIVE_MIN);

			// Display last reading information
			if (null != mBestReading) {

				updateDisplay(mBestReading);

			} else {

				mAccuracyView.setText("No Initial Reading Available");

			}

			if (null == mBestReading
					|| mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
					|| mBestReading.getTime() < System.currentTimeMillis()
							- TWO_MIN) {

				LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
				
				// Schedule a runnable to unregister location listeners
				// TODO: fix passing listener into FusedLocationApi.removeLocationUpdates
				/*Executors.newScheduledThreadPool(1).schedule(new Runnable() {

					@Override
					public void run() {

						LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

					}
				}, MEASURE_TIME, TimeUnit.MILLISECONDS);*/
			}

			}
		}

	@Override
	public void onConnectionSuspended(int cause) {
		//TODO
	}

	// Get the last known location from all providers
	// return best reading is as accurate as minAccuracy and
	// was taken no longer then maxAge milliseconds ago

	private Location bestLastKnownLocation(float minAccuracy, long maxAge) {

		Location bestResult = null;
		float bestAccuracy = Float.MAX_VALUE;
		long bestTime = Long.MIN_VALUE;

		// Get the best most recent location currently available
		mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

		if (mCurrentLocation != null) {

			float accuracy = mCurrentLocation.getAccuracy();
			long time = mCurrentLocation.getTime();

			if (accuracy < bestAccuracy) {

				bestResult = mCurrentLocation;
				bestAccuracy = accuracy;
				bestTime = time;

			}
		}

		// Return best reading or null
		if (bestAccuracy > minAccuracy || (System.currentTimeMillis() - bestTime) > maxAge) {
			return null;
		} else {
			return bestResult;
		}
	}


	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.i(TAG, "Connection Failed. Try again later.");
	}

	private boolean servicesAvailable() {

		// Check that Google Play Services are available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		// If Google Play services is available

		return (ConnectionResult.SUCCESS == resultCode);

	}

	// Update display
	private void updateDisplay(Location location) {

		mAccuracyView.setText("Accuracy:" + location.getAccuracy());

		mTimeView.setText("Time:"
				+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale
						.getDefault()).format(new Date(location.getTime())));

		mLatView.setText("Longitude:" + location.getLongitude());

		mLngView.setText("Latitude:" + location.getLatitude());

	}

	private void ensureColor() {
		if (mFirstUpdate) {
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