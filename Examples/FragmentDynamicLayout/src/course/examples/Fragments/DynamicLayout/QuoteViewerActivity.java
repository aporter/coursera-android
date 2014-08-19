package course.examples.Fragments.DynamicLayout;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import course.examples.Fragments.DynamicLayout.TitlesFragment.ListSelectionListener;

//Several Activity lifecycle methods are instrumented to emit LogCat output
//so you can follow this class' lifecycle
public class QuoteViewerActivity extends Activity implements
		ListSelectionListener {

	public static String[] mTitleArray;
	public static String[] mQuoteArray;


	private final QuotesFragment mQuoteFragment = new QuotesFragment();
	private FragmentManager mFragmentManager;
	private FrameLayout mTitleFrameLayout, mQuotesFrameLayout;

	private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
	private static final String TAG = "QuoteViewerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.i(TAG, getClass().getSimpleName() + ":entered onCreate()");

		super.onCreate(savedInstanceState);

		// Get the string arrays with the titles and qutoes
		mTitleArray = getResources().getStringArray(R.array.Titles);
		mQuoteArray = getResources().getStringArray(R.array.Quotes);

		setContentView(R.layout.main);

		// Get references to the TitleFragment and to the QuotesFragment
		mTitleFrameLayout = (FrameLayout) findViewById(R.id.title_fragment_container);
		mQuotesFrameLayout = (FrameLayout) findViewById(R.id.quote_fragment_container);


		// Get a reference to the FragmentManager
		mFragmentManager = getFragmentManager();

		// Start a new FragmentTransaction
		FragmentTransaction fragmentTransaction = mFragmentManager
				.beginTransaction();

		// Add the TitleFragment to the layout
		fragmentTransaction.add(R.id.title_fragment_container,
				new TitlesFragment());
		
		// Commit the FragmentTransaction
		fragmentTransaction.commit();

		// Add a OnBackStackChangedListener to reset the layout when the back stack changes
		mFragmentManager
				.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
					public void onBackStackChanged() {
						setLayout();
					}
				});
	}

	private void setLayout() {
		
		// Determine whether the QuoteFragment has been added
		if (!mQuoteFragment.isAdded()) {
			
			// Make the TitleFragment occupy the entire layout 
			mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
					MATCH_PARENT, MATCH_PARENT));
			mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
					MATCH_PARENT));
		} else {

			// Make the TitleLayout take 1/3 of the layout's width
			mTitleFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
					MATCH_PARENT, 1f));
			
			// Make the QuoteLayout take 2/3's of the layout's width
			mQuotesFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
					MATCH_PARENT, 2f));
		}
	}

	// Called when the user selects an item in the TitlesFragment
	@Override
	public void onListSelection(int index) {

		// If the QuoteFragment has not been added, add it now
		if (!mQuoteFragment.isAdded()) {
		
			// Start a new FragmentTransaction
			FragmentTransaction fragmentTransaction = mFragmentManager
					.beginTransaction();

			// Add the QuoteFragment to the layout
			fragmentTransaction.add(R.id.quote_fragment_container,
					mQuoteFragment);

			// Add this FragmentTransaction to the backstack
			fragmentTransaction.addToBackStack(null);
			
			// Commit the FragmentTransaction
			fragmentTransaction.commit();
			
			// Force Android to execute the committed FragmentTransaction
			mFragmentManager.executePendingTransactions();
		}
		
		if (mQuoteFragment.getShownIndex() != index) {

			// Tell the QuoteFragment to show the quote string at position index
			mQuoteFragment.showQuoteAtIndex(index);
		
		}
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.i(TAG, getClass().getSimpleName() + ":entered onRestart()");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
		super.onResume();
	}

	@Override
	protected void onStart() {
		Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
		super.onStop();
	}

}