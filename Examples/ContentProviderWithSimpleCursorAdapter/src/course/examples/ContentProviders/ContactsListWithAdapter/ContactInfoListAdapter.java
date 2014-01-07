package course.examples.ContentProviders.ContactsListWithAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;

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

	private Bitmap mNoPictureBitmap;
	private String TAG = "ContactInfoListAdapter";

	public ContactInfoListAdapter(Context context, int layout, Cursor c,
			int flags) {

		super(context, layout, c, flags);
		
		// default thumbnail photo
		mNoPictureBitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_contact_picture);

	}

	// Create and return a new contact data view
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		return inflater.inflate(R.layout.list_item, parent, false);

	}

	// Update and return a contact data view
	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		TextView textView = (TextView) view.findViewById(R.id.name);
		textView.setText(cursor.getString(cursor
				.getColumnIndex(Contacts.DISPLAY_NAME)));

		// default thumbnail photo
		ImageView imageView = (ImageView) view.findViewById(R.id.photo);
		Bitmap photoBitmap = mNoPictureBitmap;

		// Get actual thumbnail photo if it exists
		String photoContentUri = cursor.getString(cursor
				.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI));

		if (null != photoContentUri) {

			InputStream input = null;

			try {

				// Read thumbnail data from input stream
				input = context.getContentResolver().openInputStream(
						Uri.parse(photoContentUri));

				if (input != null) {

					photoBitmap = BitmapFactory.decodeStream(input);

				}

			} catch (FileNotFoundException e) {

				Log.i(TAG, "FileNotFoundException");

			}
		}

		// Set thumbnail image
		imageView.setImageBitmap(photoBitmap);

	}
}
