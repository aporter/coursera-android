package course.examples.ContentProviders.StringContentProvider;

import android.content.ContentResolver;
import android.net.Uri;

// Contract Class for accessing ContentResolver 

public final class DataContract {

	public static final String _ID = "_id";
	public static final String DATA = "data";
	public static final String DATA_TABLE = "data_table";

	private static final Uri BASE_URI = Uri
			.parse("content://course.examples.ContentProviders.StringContentProvider/");

	// The URI for this table.
	public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,
			DATA_TABLE);

	// Mime type for a directory of data items
	public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/StringContentProvider.data.text";

	// Mime type for a single data item
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/StringContentProvider.data.text";

	// All columns of this table
	public static final String[] ALL_COLUMNS = { _ID, DATA };

}