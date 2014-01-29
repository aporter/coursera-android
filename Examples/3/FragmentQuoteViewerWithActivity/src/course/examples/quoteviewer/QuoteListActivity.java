package course.examples.quoteviewer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class QuoteListActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String quote = intent.getStringExtra(TitlesListActivity.INDEX);

		if (null != quote) {
			setListAdapter(new ArrayAdapter<String>(QuoteListActivity.this,
					R.layout.list_text_item_layout, new String[] { quote }));
		}
	}
}