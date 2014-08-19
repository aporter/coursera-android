package course.examples.Fragments.ProgrammaticLayout;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import course.examples.Fragments.ProgrammaticLayout.TitlesFragment.ListSelectionListener;

//Several Activity lifecycle methods are instrumented to emit LogCat output
//so you can follow this class' lifecycle
public class QuoteViewerActivity extends Activity implements ListSelectionListener {

	public static String[] mTitleArray;
	public static String[] mQuoteArray;

	// Get a reference to the QuotesFragment
	private final QuotesFragment mQuoteFragment = new QuotesFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the string arrays with the titles and qutoes
		mTitleArray = getResources().getStringArray(R.array.Titles);
		mQuoteArray = getResources().getStringArray(R.array.Quotes);
		
		setContentView(R.layout.main);

		// Get a reference to the FragmentManager
		FragmentManager fragmentManager = getFragmentManager();
		
		// Begin a new FragmentTransaction
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		
		// Add the TitleFragment
		fragmentTransaction.add(R.id.title_frame, new TitlesFragment());

		// Add the QuoteFragment
		fragmentTransaction.add(R.id.quote_frame, mQuoteFragment);
		
		// Commit the FragmentTransaction
		fragmentTransaction.commit();
	}

	// Called when the user selects an item in the TitlesFragment
	@Override
	public void onListSelection(int index) {
		if (mQuoteFragment.getShownIndex() != index) {
	
			// Tell the QuoteFragment to show the quote string at position index
			mQuoteFragment.showQuoteAtIndex(index);
		}
	}
}