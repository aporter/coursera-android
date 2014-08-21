package course.examples.UI.Gallery;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
// This applications uses deprecated methods. See the UIViewPager application
// for a more modern implementation
public class GalleryActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Gallery g = (Gallery) findViewById(R.id.gallery);

		// Create a new ImageAdapter and set in as the Adapter for the Gallery
		g.setAdapter(new ImageAdapter(this));

		// Set an setOnItemClickListener on the Gallery
		g.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// Display a Toast message indicate the selected item
				Toast.makeText(GalleryActivity.this, "" + position,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	// The Adapter class used with the Gallery
	public class ImageAdapter extends BaseAdapter {

		private static final int IMAGE_DIM = 800;

		private int mGalleryItemBackground;
		private Context mContext;

		// List of IDs corresponding to the images
		private Integer[] mImageIds = { R.drawable.sample_1,
				R.drawable.sample_2, R.drawable.sample_3, R.drawable.sample_4,
				R.drawable.sample_5, R.drawable.sample_6, R.drawable.sample_7 };

		public ImageAdapter(Context c) {
			mContext = c;
			TypedArray a = obtainStyledAttributes(R.styleable.GalleryActivity);
			mGalleryItemBackground = a.getResourceId(
					R.styleable.GalleryActivity_android_galleryItemBackground,
					0);
			a.recycle();
		}

		public int getCount() {
			return mImageIds.length;
		}

		public Object getItem(int position) {
			return mImageIds[position];
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView = (ImageView) convertView;

			// If convertView is not recycled set it up now
			if (null == imageView) {
				imageView = new ImageView(mContext);

				imageView.setLayoutParams(new Gallery.LayoutParams(IMAGE_DIM,
						IMAGE_DIM));
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setBackgroundResource(mGalleryItemBackground);

			}

			// Set the image for the imageView
			imageView.setImageResource(mImageIds[position]);

			return imageView;
		}
	}
}