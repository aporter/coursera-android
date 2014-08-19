package course.examples.quoteviewer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TitlesListActivity extends ListActivity {

	public static String[] mTitleArray;
	public static String[] mQuoteArray;

	public static final String INDEX = "index";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the string arrays with the titles and qutoes
		mTitleArray = getResources().getStringArray(R.array.Titles);
		mQuoteArray = getResources().getStringArray(R.array.Quotes);

		// Set the list adapter for the ListView 
		// Discussed in more detail in the user interface classes lesson  
		setListAdapter(new ArrayAdapter<String>(TitlesListActivity.this,
				R.layout.list_text_item_layout, TitlesListActivity.mTitleArray));

	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		
		// Create implicity Intent to start the QuoteListActivity class
		Intent showItemIntent = new Intent(TitlesListActivity.this,
				QuoteListActivity.class);
		
		// Add an Extra representing the currently selected position
		// The name of the Extra is stored in INDEX
		showItemIntent.putExtra(INDEX, mQuoteArray[pos]);
		
		// Start the QuoteListActivity using Activity.startActivity()
		startActivity(showItemIntent);
	}

}
