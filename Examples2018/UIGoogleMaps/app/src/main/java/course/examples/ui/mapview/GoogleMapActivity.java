package course.examples.ui.mapview;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapActivity extends Activity implements OnMapReadyCallback {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // The GoogleMap instance underlying the GoogleMapFragment defined in main.xml
        MapFragment map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        map.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (map != null) {

            // Set the map position
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29,
                    -88), 3.0f));

            // Add a marker on Washington, DC, USA
            map.addMarker(new MarkerOptions().position(
                    new LatLng(38.8895, -77.0352)).title(
                    getString(R.string.in_washington_string)));

            // Add a marker on Mexico City, Mexico
            map.addMarker(new MarkerOptions().position(
                    new LatLng(19.13, -99.4)).title(
                    getString(R.string.in_mexico_string)));
        }

    }
}