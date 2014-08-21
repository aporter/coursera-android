package course.examples.UI.MapView;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

// This applications requires several set up steps. 
// See https://developers.google.com/maps/documentation/android/start for more information
	
// Requires a device that supports OpenGL ES version 2
// I've run this application successfully on an emulated Nexus5
	
public class GoogleMapActivity extends Activity {

	private GoogleMap mMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// The GoogleMap instance underlying the GoogleMapFragment defined in main.xml 
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		if (mMap != null) {

			// Set the map position
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29,
					-88), 0));

			// Add a marker on Washington, DC, USA
			mMap.addMarker(new MarkerOptions().position(
					new LatLng(38.8895, -77.0352)).title(
					getString(R.string.in_washington_string)));
			
			// Add a marker on Mexico City, Mexico
			mMap.addMarker(new MarkerOptions().position(
					new LatLng(19.13, -99.4)).title(
					getString(R.string.in_mexico_string)));
		}
	}
}