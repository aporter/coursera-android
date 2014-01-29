package course.examples.UI.MapView;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapActivity extends Activity {

	private GoogleMap mMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		if (mMap != null) {

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29,
					-88), 0));

			mMap.addMarker(new MarkerOptions().position(
					new LatLng(38.8895, -77.0352)).title(
					getString(R.string.in_washington_string)));
			mMap.addMarker(new MarkerOptions().position(
					new LatLng(19.13, -99.4)).title(
					getString(R.string.in_mexico_string)));
		}
	}
}