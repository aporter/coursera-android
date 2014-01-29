package course.examples.Services.MusicService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MusicServiceClient extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		
		// Intent used for starting the MusicService
		final Intent musicServiceIntent = new Intent(getApplicationContext(),
				MusicService.class);

		final Button startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(new OnClickListener() {
			public void onClick(View src) {
				
				// Start the MusicService using the Intent
				startService(musicServiceIntent);

			}
		});

		final Button stopButton = (Button) findViewById(R.id.stop_button);
		stopButton.setOnClickListener(new OnClickListener() {
			public void onClick(View src) {

				// Stop the MusicService using the Intent
				stopService(musicServiceIntent);

			}
		});

	}



}