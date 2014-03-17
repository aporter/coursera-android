package course.labs.locationlab;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceViewAdapter extends BaseAdapter {

	private ArrayList<PlaceRecord> list = new ArrayList<PlaceRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;

	public PlaceViewAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View newView = convertView;
		ViewHolder holder;

		PlaceRecord curr = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.place_badge_view, null);
			holder.flag = (ImageView) newView.findViewById(R.id.flag);
			holder.country = (TextView) newView.findViewById(R.id.country_name);
			holder.place = (TextView) newView.findViewById(R.id.place_name);
			newView.setTag(holder);
			
		} else {
			holder = (ViewHolder) newView.getTag();
		}

		holder.flag.setImageBitmap(curr.getFlagBitmap());
		holder.country.setText("Country: " + curr.getCountryName());
		holder.place.setText("Place: " + curr.getPlace());

		return newView;
	}
	
	static class ViewHolder {
	
		ImageView flag;
		TextView country;
		TextView place;
		
	}
	
	public boolean intersects (Location location) {
		for (PlaceRecord item : list) {
			if (item.intersects(location)) {
				return true;
			}
		}
		return false;
	}

	public void add(PlaceRecord listItem) {
		list.add(listItem);
		notifyDataSetChanged();
	}
	
	public ArrayList<PlaceRecord> getList(){
		return list;
	}
	
	public void removeAllViews(){
		list.clear();
		this.notifyDataSetChanged();
	}
}
