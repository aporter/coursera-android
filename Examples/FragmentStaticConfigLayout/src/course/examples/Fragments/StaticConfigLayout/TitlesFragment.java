package course.examples.Fragments.StaticConfigLayout;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class TitlesFragment extends ListFragment {

	private static final String TAG = null;
	private ListSelectionListener mListener = null;
	private int mCurrIdx = -1;

	public interface ListSelectionListener {
		public void onListSelection(int index);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (ListSelectionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnArticleSelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);

		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		setListAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.list_item, QuoteViewerActivity.mTitleArray));

		if (-1 != mCurrIdx)
			getListView().setItemChecked(mCurrIdx, true);
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		if (mCurrIdx != pos) {
			mCurrIdx = pos;
			mListener.onListSelection(pos);
		}

		l.setItemChecked(mCurrIdx, true);
	}

	@Override
	public void onDestroy() {

		Log.i(TAG, getClass().getSimpleName() + ":onDestroy()");

		super.onDestroy();
	}

	@Override
	public void onDestroyView() {

		Log.i(TAG, getClass().getSimpleName() + ":onDestroyView()");

		super.onDestroyView();
	}

	@Override
	public void onDetach() {

		Log.i(TAG, getClass().getSimpleName() + ":onDetach()");

		super.onDetach();
	}

	@Override
	public void onPause() {

		Log.i(TAG, getClass().getSimpleName() + ":onPause()");

		super.onPause();
	}

	@Override
	public void onResume() {

		Log.i(TAG, getClass().getSimpleName() + ":onResume()");

		super.onResume();
	}

	@Override
	public void onStart() {

		Log.i(TAG, getClass().getSimpleName() + ":onStart()");

		super.onStart();
	}

	@Override
	public void onStop() {

		Log.i(TAG, getClass().getSimpleName() + ":onStop()");

		super.onStop();
	}

}