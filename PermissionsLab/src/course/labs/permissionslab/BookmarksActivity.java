package course.labs.permissionslab;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BookmarksActivity extends Activity {
	
	private static final String TAG = "Lab-Permissions";

	static final String[] projection = { Browser.BookmarkColumns.TITLE,
			Browser.BookmarkColumns.URL };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmarks_activity);

		Button getBookmarksButton = (Button) findViewById(R.id.get_bookmarks_button);
		getBookmarksButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				loadBookmarks();

			}
		});

		Button goToDangerousActivityButton = (Button) findViewById(R.id.go_to_dangerous_activity_button);
		goToDangerousActivityButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startGoToDangerousActivity();

			}
		});

	}

	private void loadBookmarks() {

		Log.i(TAG, "Entered loadBookmarks()");

		String text = "";

		Cursor query = getContentResolver().query(Browser.BOOKMARKS_URI,
				projection, null, null, null);

		query.moveToFirst();
		while (query.moveToNext()) {

			text += query.getString(query
					.getColumnIndex(Browser.BookmarkColumns.TITLE));
			text += "\n";
			text += query.getString(query
					.getColumnIndex(Browser.BookmarkColumns.URL));
			text += "\n\n";

		}

		TextView box = (TextView) findViewById(R.id.text);
		box.setText(text);

		Log.i(TAG, "Bookmarks loaded");
	}

	private void startGoToDangerousActivity() {

		Log.i(TAG, "Entered startGoToDangerousActivity()");

		// TODO - Start the GoToDangerousActivity
		

	}

}
