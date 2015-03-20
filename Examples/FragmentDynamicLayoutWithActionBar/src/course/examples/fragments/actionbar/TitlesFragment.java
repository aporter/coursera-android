package course.examples.fragments.actionbar;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

//Several Activity and Fragment lifecycle methods are instrumented to emit LogCat output
//so you can follow the class' lifecycle
public class TitlesFragment extends ListFragment {
	ListSelectionListener mListener = null;
	int mCurrIdx = -1;

	// Callback interface that allows this Fragment to notify the QuoteViewerActivity when  
	// user clicks on a List Item  
	public interface ListSelectionListener {
		public void onListSelection(int index);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {

			// Set the ListSelectionListener for communicating with the QuoteViewerActivity
			mListener = (ListSelectionListener) activity;
		
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnArticleSelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// This Fragment will add items to the ActionBar  
		setHasOptionsMenu(true);
		
		// Retain this Fragment across Activity Reconfigurations
		setRetainInstance(true);
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);

		// Set the list adapter for the ListView 
		// Discussed in more detail in the user interface classes lesson  
		setListAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.list_item, QuoteViewerActivity.TitleArray));

		// If a title has already been selected in the past, reset the selection state now
		if (mCurrIdx != QuoteViewerActivity.UNSELECTED) {
			setSelection(mCurrIdx);
		}
	}

	// Called when the user selects an item from the List
	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		mCurrIdx = pos;

		// Indicates the selected item has been checked
		getListView().setItemChecked(pos, true);
		
		// Inform the QuoteViewerActivity that the item in position pos has been selected
		mListener.onListSelection(pos);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		// Inflate the options Menu using title_menu.xml
		inflater.inflate(R.menu.title_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.title_menu_item:

			// Show a Toast Message. Toast Messages are discussed in the lesson on user interface classes
			Toast.makeText(getActivity().getApplicationContext(),
					"This action provided by the TitlesFragment",
					Toast.LENGTH_SHORT).show();
			
			// return value true indicates that the menu click has been handled
			return true;
		
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}