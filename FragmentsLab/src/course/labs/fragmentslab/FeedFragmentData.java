package course.labs.fragmentslab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

// Utility class that provides stored Twitter feed data 

public class FeedFragmentData {
	private static final String TAG = "FeedFragmentData";
	private static final int[] IDS = { R.raw.ladygaga, R.raw.rebeccablack,
			R.raw.taylorswift };
	
	private SparseArray<String> mFeeds = new SparseArray<String>();
	private Context mContext;
	
		
	public FeedFragmentData(Context context) {
		mContext = context;
		loadFeeds();
	}

	// Load all stored Twitter feeds into the mFeeds SparseArray. 
	
	private void loadFeeds() {

		for (int id : IDS) {

			InputStream inputStream = mContext.getResources().openRawResource(
					id);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));

			StringBuffer buffer = new StringBuffer("");
			
			
			// Read raw data from resource file
			
			try {

				String line = "";
				while ((line = reader.readLine()) != null) {
				
					buffer.append(line);
				}
			
			} catch (IOException e) {
				Log.i(TAG, "IOException");
			}

			// Convert raw data into a String

			JSONArray feed = null;
			try {
				feed = new JSONArray(buffer.toString());
			} catch (JSONException e) {
				Log.i(TAG, "JSONException");
			}

			mFeeds.put(id, procFeed(feed));
		
		}
	}
	
	
// Convert JSON formatted data to a String
	
	private String procFeed(JSONArray feed) {

		String name = "";
		String tweet = "";

		// string buffer for twitter feeds
		StringBuffer textFeed = new StringBuffer("");

		for (int j = 0; j < feed.length(); j++) {
			try {

				tweet = feed.getJSONObject(j).getString("text");
				JSONObject user = (JSONObject) feed.getJSONObject(j)
						.get("user");
				name = user.getString("name");

			} catch (JSONException e) {

				Log.i(TAG, "JSONException while processing feed");
			}

			textFeed.append(name + " - " + tweet + "\n\n");
		}

		return textFeed.toString();
	}
	
// Return the Twitter feed data for the specified position as a single String
	
 String getFeed (int position) {
		
		return mFeeds.get(IDS[position]);
	
	}
}