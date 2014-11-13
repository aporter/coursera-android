package course.examples.maps.earthquakemap;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsEarthquakeMapActivity extends Activity {

	// Coordinates used for centering the Map

	private static final double CAMERA_LNG = 87.0;
	private static final double CAMERA_LAT = 17.0;

	// The Map Object
	private GoogleMap mMap;

	// URL for getting the earthquake
	// replace with your own user name

	private final static String UNAME = "aporter";
	private final static String URL = "http://api.geonames.org/earthquakesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&username="
			+ UNAME;

	public static final String TAG = "MapsEarthquakeMapActivity";

	// Set up UI and get earthquake data
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		new HttpGetTask().execute(URL);

	}

	private class HttpGetTask extends
			AsyncTask<String, Void, List<EarthQuakeRec>> {

		AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<EarthQuakeRec> doInBackground(String... params) {

			HttpGet request = new HttpGet(params[0]);
			JSONResponseHandler responseHandler = new JSONResponseHandler();

			try {

				// Get Earthquake data in JSON format
				// Parse data into a list of EarthQuakeRecs

				return mClient.execute(request, responseHandler);

			} catch (ClientProtocolException e) {
				Log.i(TAG, "ClientProtocolException");
			} catch (IOException e) {
				Log.i(TAG, "IOException");
			}

			return null;

		}

		@Override
		protected void onPostExecute(List<EarthQuakeRec> result) {

			// Get Map Object
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			if (null != mMap) {

				// Add a marker for every earthquake

				for (EarthQuakeRec rec : result) {

					// Add a new marker for this earthquake
					mMap.addMarker(new MarkerOptions()

							// Set the Marker's position
							.position(new LatLng(rec.getLat(), rec.getLng()))

							// Set the title of the Marker's information window
							.title(String.valueOf(rec.getMagnitude()))

							// Set the color for the Marker
							.icon(BitmapDescriptorFactory
									.defaultMarker(getMarkerColor(rec
											.getMagnitude()))));

				}

				// Center the map 
				// Should compute map center from the actual data
				
				mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(
						CAMERA_LAT, CAMERA_LNG)));

			}
				
			if (null != mClient)
				mClient.close();

		}

		// Assign marker color
		private float getMarkerColor(double magnitude) {

			if (magnitude < 6.0) {
				magnitude = 6.0;
			} else if (magnitude > 9.0) {
				magnitude = 9.0;
			}

			return (float) (120 * (magnitude - 6));
		}

	}

}