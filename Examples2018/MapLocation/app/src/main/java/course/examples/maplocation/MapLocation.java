package course.examples.maplocation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

// Several Activity lifecycle methods are instrumented to emit LogCat output
// so you can follow this class' lifecycle

public class MapLocation extends Activity {

    private final String TAG = "MapLocation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Required call through to Activity.onCreate()
        // Restore any saved instance state
        super.onCreate(savedInstanceState);

        // Set content view
        setContentView(R.layout.main);

        // Initialize UI elements
        final EditText addrText = findViewById(R.id.location);
        final Button button = findViewById(R.id.mapButton);

        // Link UI elements to actions in code        
        button.setOnClickListener(new OnClickListener() {

            // Called when user clicks the Show Map button
            public void onClick(View v) {
                try {

                    // Process text for network transmission
                    String address = addrText.getText().toString();
                    address = address.replace(' ', '+');

                    // Create Intent object for starting Google Maps application 
                    Intent geoIntent = new Intent(
                            android.content.Intent.ACTION_VIEW, Uri
                            .parse("geo:0,0?q=" + address));

                    if (getPackageManager().resolveActivity(geoIntent, 0) != null) {
                        // Use the Intent to start Google Maps application using Activity.startActivity()
                        startActivity(geoIntent);
                    }

                } catch (Exception e) {
                    // Log any error messages to LogCat using Log.e()
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "The activity is visible and about to be started.");
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "The activity is visible and about to be restarted.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "The activity is and has focus (it is now \"resumed\")");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,
                "Another activity is taking focus (this activity is about to be \"paused\")");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "The activity is no longer visible (it is now \"stopped\")");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "The activity is about to be destroyed.");
    }
}
