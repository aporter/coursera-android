package course.examples.ContentProviders.ContactsList;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class ContactInfoListAdapter extends ResourceCursorAdapter {

	private final ContentResolver mContentResolver;
	private Bitmap mNoPictureBitmap;

	private String TAG = "ContactInfoListAdapter";

	public ContactInfoListAdapter(Context context, int layout, Cursor c,
			int flags) {

		super(context, layout, c, flags);

		mContentResolver = context.getContentResolver();

		// Default thumbnail bitmap for when contact has no thubnail 
		mNoPictureBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_contact_picture);

	}

	// Called when a new view is needed
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		return inflater.inflate(R.layout.list_item, parent, false);

	}

	// Called when a new data view is needed, but an old view is 
	// available for reuse
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		// Set display name
		TextView textView = (TextView) view.findViewById(R.id.name);
		textView.setText(cursor.getString(cursor
				.getColumnIndex(Contacts.DISPLAY_NAME)));

		// Set default thumbnail
		ImageView imageView = (ImageView) view.findViewById(R.id.photo);
		Bitmap photoBitmap = mNoPictureBitmap;

		// Try to set actual thumbnail, if it's available
		
		String photoContentUri = cursor.getString(cursor
				.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI));

		if (null != photoContentUri) {

			InputStream input = null;

			try {

				// read thumbail data from memory
				
				input = mContentResolver.openInputStream(Uri
						.parse(photoContentUri));

				if (input != null) {

					photoBitmap = BitmapFactory.decodeStream(input);

				}

			} catch (FileNotFoundException e) {

				Log.i(TAG, "FileNotFoundException");

			}
		}

		imageView.setImageBitmap(photoBitmap);
		
	}
}
