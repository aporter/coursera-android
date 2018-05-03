package course.examples.datamanagement.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseOpenHelper extends SQLiteOpenHelper {
	
	final static String TABLE_NAME = "artists";
	final static String ARTIST_NAME = "name";
	private final static String _ID = "_id";
	final static String[] columns = { _ID, ARTIST_NAME };

	final private static String CREATE_CMD =

	"CREATE TABLE artists (" + _ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ARTIST_NAME + " TEXT NOT NULL)";

	final private static String NAME = "artist_db";
	final private static Integer VERSION = 1;
	final private Context mContext;

	public DatabaseOpenHelper(Context context) {
		super(context, NAME, null, VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CMD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// N/A
	}

	void deleteDatabase() {
		mContext.deleteDatabase(NAME);
	}
}
