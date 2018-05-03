package course.examples.ui.gridlayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;

//This application uses some deprecated methods. 
//See UIViewPager for a more modern version of this application

public class GridLayoutActivity extends Activity {

	static final String EXTRA_RES_ID = "POS";

	private final ArrayList<Integer> mThumbIdsFlowers = new ArrayList<>(
			Arrays.asList(R.drawable.image1, R.drawable.image2,
					R.drawable.image3, R.drawable.image4, R.drawable.image5,
					R.drawable.image6, R.drawable.image7, R.drawable.image8,
					R.drawable.image9, R.drawable.image10, R.drawable.image11,
					R.drawable.image12));

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		GridView gridview = findViewById(R.id.gridview);

		// Create a new ImageAdapter and set it as the Adapter for this GridView
		gridview.setAdapter(new ImageAdapter(this, mThumbIdsFlowers));

		// Set an setOnItemClickListener on the GridView
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				
				//Create an Intent to start the ImageViewActivity
				Intent intent = new Intent(GridLayoutActivity.this,
						ImageViewActivity.class);
				
				// Add the ID of the thumbnail to display as an Intent Extra
				intent.putExtra(EXTRA_RES_ID, (int) id);
				
				// Start the ImageViewActivity
				startActivity(intent);
			}
		});
	}
}