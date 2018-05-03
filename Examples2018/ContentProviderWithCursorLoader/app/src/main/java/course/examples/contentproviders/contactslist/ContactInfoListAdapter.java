package course.examples.contentproviders.contactslist;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

class ContactInfoListAdapter extends ResourceCursorAdapter {

	private final ContentResolver mContentResolver;
	private static final String TAG = "ContactInfoListAdapter";
	private final Context mApplicationContext;
	private final int mBitmapSize;

	private final BitmapDrawable mNoPictureBitmap;

	public ContactInfoListAdapter(Context context, @SuppressWarnings("SameParameterValue") int layout, @SuppressWarnings("SameParameterValue") Cursor c,
								  @SuppressWarnings("SameParameterValue") int flags) {

		super(context, layout, c, flags);

		mContentResolver = context.getContentResolver();

		mApplicationContext = context.getApplicationContext();

		// Default thumbnail bitmap for when contact has no thumbnail
		mNoPictureBitmap = (BitmapDrawable) context.getResources().getDrawable(
				R.drawable.ic_contact_picture, context.getTheme());
		mBitmapSize = (int) context.getResources().getDimension(
				R.dimen.textview_height);
		mNoPictureBitmap.setBounds(0, 0, mBitmapSize, mBitmapSize);

	}

	// Called when a new view is needed

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//noinspection ConstantConditions
		return inflater.inflate(R.layout.list_item, parent, false);

	}

	// Called when a new data view is needed, but an old view is
	// available for reuse

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		// Set display name
		TextView textView = view.findViewById(R.id.name);
		textView.setText(cursor.getString(cursor
				.getColumnIndex(Contacts.DISPLAY_NAME)));

		// Default photo
		BitmapDrawable photoBitmap = mNoPictureBitmap;

		// Try to set actual thumbnail, if it's available

		String photoContentUri = cursor.getString(cursor
				.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI));

		if (null != photoContentUri) {

			InputStream input;

			try {

				// read thumbnail data from memory

				input = mContentResolver.openInputStream(Uri
						.parse(photoContentUri));

				if (input != null) {

					photoBitmap = new BitmapDrawable(
							mApplicationContext.getResources(), input);
					photoBitmap.setBounds(0, 0, mBitmapSize, mBitmapSize);

				}
			} catch (FileNotFoundException e) {

				Log.i(TAG, "FileNotFoundException");

			}
		}

		textView.setCompoundDrawables(photoBitmap, null, null, null);

	}
}
