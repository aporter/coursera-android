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

		mTitleArray = getResources().getStringArray(R.array.Titles);
		mQuoteArray = getResources().getStringArray(R.array.Quotes);
		setListAdapter(new ArrayAdapter<String>(TitlesListActivity.this,
				R.layout.list_text_item_layout, TitlesListActivity.mTitleArray));

	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		Intent showItemIntent = new Intent(TitlesListActivity.this,
				QuoteListActivity.class);
		showItemIntent.putExtra(INDEX, mQuoteArray[pos]);
		startActivity(showItemIntent);
	}

}
