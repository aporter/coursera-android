package course.examples.UI.FragmentActionBar;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import course.examples.UI.FragmentActionBar.TitlesFragment.ListSelectionListener;

public class QuoteViewerActivity extends Activity implements ListSelectionListener {

	public static String[] TitleArray;
	public static String[] QuoteArray;
	private final TitlesFragment mTitlesFragment = new TitlesFragment();
	private final QuoteFragment mDetailsFragment = new QuoteFragment();
	public static final int UNSELECTED = -1;
	private FragmentManager mFragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the string arrays with the titles and qutoes
		TitleArray = getResources().getStringArray(R.array.Titles);
		QuoteArray = getResources().getStringArray(R.array.Quotes);
		
		setContentView(R.layout.main);

		// Get a reference to the FragmentManager
		mFragmentManager = getFragmentManager();

		// Start a new FragmentTransaction
		FragmentTransaction fragmentTransaction = mFragmentManager
				.beginTransaction();

		// Add the TitleFragment to the layout
		fragmentTransaction.add(R.id.title_fragment_container, mTitlesFragment);
		
		// Commit the FragmentTransaction
		fragmentTransaction.commit();
	
	}

	// Called when the user selects an item in the TitlesFragment
	@Override
	public void onListSelection(int index) {

		// If the QuoteFragment has not been added, add it now
		if (!mDetailsFragment.isAdded()) {
		
			// Start a new FragmentTransaction
			FragmentTransaction fragmentTransaction = mFragmentManager
					.beginTransaction();
			
			// Add the QuoteFragment to the layout
			fragmentTransaction.add(R.id.quote_fragment_container, mDetailsFragment);
			
			// Add this FragmentTransaction to the backstack
			fragmentTransaction.addToBackStack(null);
			
			// Commit the FragmentTransaction
			fragmentTransaction.commit();
			
			// Force Android to execute the committed FragmentTransaction
			mFragmentManager.executePendingTransactions();
		}
		
		if (mDetailsFragment.getShownIndex() != index) {

			// Tell the QuoteFragment to show the quote string at position index
			mDetailsFragment.showQuoteAtIndex(index);
		
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Get a reference to the MenuInflater
		MenuInflater inflater = getMenuInflater();
		
		// Inflate the menu using activity_menu.xml
		inflater.inflate(R.menu.activity_menu, menu);
		
		// Return true to display the menu
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.activity_menu_item:

			// Show a Toast Message. Toast Messages are discussed in the lesson on user interface classes
			Toast.makeText(getApplicationContext(),
					"This action provided by the QuoteViewerActivity", Toast.LENGTH_SHORT)
					.show();
			
			// return value true indicates that the menu click has been handled
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}