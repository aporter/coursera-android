package course.examples.UI.FragmentActionBar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuoteFragment extends Fragment {

	private TextView mQuoteView = null;
	private int mCurrIdx = -1;
	private int mQuoteArrLen = 0;

	public int getShownIndex() {
		return mCurrIdx;
	}

	// Show the Quote string at position newIndex
	public void showQuoteAtIndex(int newIndex) {
		if (newIndex < 0 || newIndex >= mQuoteArrLen)
			return;
		mCurrIdx = newIndex;
		mQuoteView.setText(QuoteViewerActivity.QuoteArray[mCurrIdx]);
	}

	// Called to create the content view for this Fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout defined in quote_fragment.xml
		// The last parameter is false because the returned view does not need to be attached to the container ViewGroup
		return inflater.inflate(R.layout.detail_fragment, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
		setHasOptionsMenu(true);
	}

	// Set up some information about the mQuoteView TextView 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mQuoteView = (TextView) getActivity().findViewById(R.id.quoteView);
		mQuoteArrLen = QuoteViewerActivity.QuoteArray.length;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCurrIdx = -1;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		// Inflate the options Menu using quote_menu.xml
		inflater.inflate(R.menu.quote_menu, menu);
	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// Show Toast Messages. Toast Messages are discussed in the lesson on user interface classes
		// return value true indicates that the menu click has been handled

		case R.id.detail_menu_item_main:
			Toast.makeText(getActivity().getApplicationContext(),
					"This action provided by the QuoteFragment",
					Toast.LENGTH_SHORT).show();
			return true;

		case R.id.detail_menu_item_secondary:
			Toast.makeText(getActivity().getApplicationContext(),
					"This action is also provided by the QuoteFragment",
					Toast.LENGTH_SHORT).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
