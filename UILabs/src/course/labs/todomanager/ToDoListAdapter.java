package course.labs.todomanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import course.labs.todomanager.ToDoItem.Status;

public class ToDoListAdapter extends BaseAdapter {

	// List of ToDoItems
	private final List<ToDoItem> mItems = new ArrayList<ToDoItem>();
	
	private final Context mContext;

	private static final String TAG = "Lab-UserInterface";

	public ToDoListAdapter(Context context) {

		mContext = context;

	}

	// Add a ToDoItem to the adapter
	// Notify observers that the data set has changed

	public void add(ToDoItem item) {

		mItems.add(item);
		notifyDataSetChanged();

	}
	
	// Clears the list adapter of all items.
	
	public void clear(){

		mItems.clear();
		notifyDataSetChanged();
	
	}

	// Returns the number of ToDoItems

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Retrieve the number of ToDoItems

	@Override
	public Object getItem(int pos) {

		return mItems.get(pos);

	}

	// Get the ID for the ToDoItem
	// In this case it's just the position

	@Override
	public long getItemId(int pos) {

		return pos;

	}

	//Create a View to display the ToDoItem 
	// at specified position in mItems

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {


		//TODO - Get the current ToDoItem
		final ToDoItem toDoItem = null;

		//TODO - Inflate the View for this ToDoItem
		// from todo_item.xml.
		RelativeLayout itemLayout = null;
		
		//TODO - Fill in specific ToDoItem data
		// Remember that the data that goes in this View
		// corresponds to the user interface elements defined 
		// in the layout file 

		//TODO - Display Title in TextView

		final TextView titleView = null;
		
		// TODO - Set up Status CheckBox
	
		final CheckBox statusView = null;
		
		
		statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				log("Entered onCheckedChanged()");
				
				// TODO - Set up and implement an OnCheckedChangeListener, which 
				// is called when the user toggles the status checkbox


			
			}
		});

		//TODO - Display Priority in a TextView

		final TextView priorityView = null;

		
		// TODO - Display Time and Date. 
		// Hint - use ToDoItem.FORMAT.format(toDoItem.getDate()) to get date and time String

		final TextView dateView = null;
				

		// Return the View you just created
		return itemLayout;

	}
	
	private void log(String msg) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(TAG, msg);
	}

}
