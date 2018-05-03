package course.examples.contentproviders.stringcontentprovider;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class DisplayActivity extends ListActivity {

    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContentResolver contentResolver = getContentResolver();

        // Create and set mCursor and list adapter
        mCursor = contentResolver.query(DataContract.CONTENT_URI, null, null, null,
                null);

        setListAdapter(new SimpleCursorAdapter(this, R.layout.list_layout, mCursor,
                DataContract.ALL_COLUMNS, new int[]{R.id.idString,
                R.id.data}, 0));

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mCursor) mCursor.close();
    }
}
