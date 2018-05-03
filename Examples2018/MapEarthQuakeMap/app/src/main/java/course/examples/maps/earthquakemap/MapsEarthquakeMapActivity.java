package course.examples.maps.earthquakemap;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsEarthquakeMapActivity extends Activity implements RetainedFragment.OnFragmentInteractionListener, OnMapReadyCallback {

    // Coordinates used for centering the Map

    private static final double CAMERA_LNG = 100.0;
    private static final double CAMERA_LAT = 28.0;

    // The Map Object
    private GoogleMap mMap;

    // URL for getting the earthquake
    // replace with your own user name

    @SuppressWarnings("unused")
    public static final String TAG = "MapsEarthquakeMapActivity";
    private RetainedFragment mRetainedFragment;
    private boolean mMapReady;
    private boolean mDataReady;

    // Set up UI and get earthquake data
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        if (null != savedInstanceState) {
            mRetainedFragment = (RetainedFragment) getFragmentManager()
                    .findFragmentByTag(RetainedFragment.TAG);
            onDownloadfinished();
        } else {
            mRetainedFragment = new RetainedFragment();
            getFragmentManager().beginTransaction()
                    .add(mRetainedFragment, RetainedFragment.TAG)
                    .commit();
            mRetainedFragment.onButtonPressed();
        }

        // The GoogleMap instance underlying the GoogleMapFragment defined in main.xml
        MapFragment map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        map.getMapAsync(this);
    }

    // Called when data is downloaded
    public void onDownloadfinished() {
        mDataReady = true;
        if (mMapReady) {
            placeMarkers();
            mDataReady = false;
        }
    }

    private void placeMarkers() {

        // Add a marker for every earthquake
        for (EarthQuakeRec rec : mRetainedFragment.getData()) {

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
    }


    // Called when Map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapReady = true;
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(CAMERA_LAT, CAMERA_LNG)));
        if (mDataReady) {
            placeMarkers();
            mMapReady = false;
        }
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


