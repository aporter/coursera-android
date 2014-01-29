package course.examples.Fragments.ProgrammaticLayout;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import course.examples.Fragments.ProgrammaticLayout.TitlesFragment.ListSelectionListener;

public class QuoteViewerActivity extends Activity implements ListSelectionListener {

	public static String[] mTitleArray;
	public static String[] mQuoteArray;
	private final QuotesFragment mQuoteFragment = new QuotesFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mTitleArray = getResources().getStringArray(R.array.Titles);
		mQuoteArray = getResources().getStringArray(R.array.Quotes);
		
		setContentView(R.layout.main);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.add(R.id.title_frame, new TitlesFragment());
		fragmentTransaction.add(R.id.quote_frame, mQuoteFragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onListSelection(int index) {
		if (mQuoteFragment.getShownIndex() != index) {
			mQuoteFragment.showQuoteAtIndex(index);
		}
	}
}