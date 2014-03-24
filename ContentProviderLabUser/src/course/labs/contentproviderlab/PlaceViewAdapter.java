package course.labs.contentproviderlab;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import course.labs.contentproviderlab.provider.PlaceBadgesContract;

public class PlaceViewAdapter extends CursorAdapter {

	private static final String APP_DIR = "ContentProviderLab/Badges";
	private ArrayList<PlaceRecord> list = new ArrayList<PlaceRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;
	private String mBitmapStoragePath;

	public PlaceViewAdapter(Context context, Cursor cursor, int flags) {
		super(context, cursor, flags);

		mContext = context;
		inflater = LayoutInflater.from(mContext);

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			try {

				String root = mContext.getExternalFilesDir(null)
						.getCanonicalPath();

				if (null != root) {

					File bitmapStorageDir = new File(root, APP_DIR);
					bitmapStorageDir.mkdirs();
					mBitmapStoragePath = bitmapStorageDir.getCanonicalPath();

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Cursor swapCursor(Cursor newCursor) {
		super.swapCursor(newCursor);

		if (null != newCursor) {

        // TODO - clear the ArrayList list so it contains
		// the current set of PlaceRecords. Use the 
		// getPlaceRecordFromCursor() method to add the
		// current place to the list
		

            
            
            
            
            
            // Set the NotificationURI for the new cursor
			newCursor.setNotificationUri(mContext.getContentResolver(),
					PlaceBadgesContract.CONTENT_URI);

		}
		return newCursor;

	}

	// returns a new PlaceRecord for the data at the cursor's
	// current position
	private PlaceRecord getPlaceRecordFromCursor(Cursor cursor) {

		String flagBitmapPath = cursor.getString(cursor
				.getColumnIndex(PlaceBadgesContract.FLAG_BITMAP_PATH));
		String countryName = cursor.getString(cursor
				.getColumnIndex(PlaceBadgesContract.COUNTRY_NAME));
		String placeName = cursor.getString(cursor
				.getColumnIndex(PlaceBadgesContract.PLACE_NAME));
		double lat = cursor.getDouble(cursor
				.getColumnIndex(PlaceBadgesContract.LAT));
		double lon = cursor.getDouble(cursor
				.getColumnIndex(PlaceBadgesContract.LON));

		return new PlaceRecord(null, flagBitmapPath, countryName, placeName,
				lat, lon);

	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {

		ImageView flag;
		TextView country;
		TextView place;

	}

	public boolean intersects(Location location) {
		for (PlaceRecord item : list) {
			if (item.intersects(location)) {
				return true;
			}
		}
		return false;
	}

	public void add(PlaceRecord listItem) {

		String lastPathSegment = Uri.parse(listItem.getFlagUrl())
				.getLastPathSegment();
		String filePath = mBitmapStoragePath + "/" + lastPathSegment;

		if (storeBitmapToFile(listItem.getFlagBitmap(), filePath)) {

			listItem.setFlagBitmapPath(filePath);
			list.add(listItem);

			// TODO - Insert new record into the ContentProvider

			

		
        
        
        
        }

	}

	public ArrayList<PlaceRecord> getList() {
		return list;
	}

	public void removeAllViews() {

		list.clear();

		// TODO - delete all records in the ContentProvider


        
        
        
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		ViewHolder holder = (ViewHolder) view.getTag();

		holder.flag.setImageBitmap(getBitmapFromFile(cursor.getString(cursor
				.getColumnIndex(PlaceBadgesContract.FLAG_BITMAP_PATH))));
		holder.country.setText("Country: "
				+ cursor.getString(cursor
						.getColumnIndex(PlaceBadgesContract.COUNTRY_NAME)));
		holder.place.setText("Place: "
				+ cursor.getString(cursor
						.getColumnIndex(PlaceBadgesContract.PLACE_NAME)));

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		View newView;
		ViewHolder holder = new ViewHolder();

		newView = inflater.inflate(R.layout.place_badge_view, null);
		holder.flag = (ImageView) newView.findViewById(R.id.flag);
		holder.country = (TextView) newView.findViewById(R.id.country_name);
		holder.place = (TextView) newView.findViewById(R.id.place_name);

		newView.setTag(holder);

		return newView;
	}

	private Bitmap getBitmapFromFile(String filePath) {
		return BitmapFactory.decodeFile(filePath);
	}

	private boolean storeBitmapToFile(Bitmap bitmap, String filePath) {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			try {

				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(filePath));

				bitmap.compress(CompressFormat.PNG, 100, bos);

				bos.flush();
				bos.close();

			} catch (FileNotFoundException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
			return true;
		}
		return false;
	}
}
