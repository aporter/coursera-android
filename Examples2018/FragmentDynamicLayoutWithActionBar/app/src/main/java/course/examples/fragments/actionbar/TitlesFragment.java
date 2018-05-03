package course.examples.fragments.actionbar;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "TitlesFragment";
    private ListSelectionListener mListener;
    private int mCurrIdx = ListView.INVALID_POSITION;


    public void unCheckSelection() {
        if (getListView().getCheckedItemCount() > 0) {
            getListView().setItemChecked(getListView().getCheckedItemPosition(), false);
        }
        mCurrIdx = ListView.INVALID_POSITION;
    }

    // Called when the user selects an item from the List
    @Override
    public void onListItemClick(ListView l, View v, int pos, long id) {
        if (mCurrIdx != pos) {
            mCurrIdx = pos;
            // Indicates the selected item has been checked
            l.setItemChecked(mCurrIdx, true);
            // Inform the QuoteViewerActivity that the item in position pos has been selected
            mListener.onListSelection(mCurrIdx);
        }
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
        Log.i(TAG, getClass().getSimpleName() + ":entered onActivityCreated()");
        super.onActivityCreated(savedState);

        // Set the list adapter for the ListView
        // Discussed in more detail in the user interface classes lesson
        setListAdapter(new ArrayAdapter<>(getActivity(),
                R.layout.title_item, getResources().getStringArray(R.array.Titles)));

        // Set the list choice mode to allow only one selection at a time
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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

    @Override
    public void onStart() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onStop()");
        super.onStop();
    }

    @Override
    public void onDetach() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDetach()");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, getClass().getSimpleName() + ":entered onDestroyView()");
        super.onDestroyView();
    }

}