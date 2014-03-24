package course.labs.contentproviderlab.provider;

import android.net.Uri;

public final class PlaceBadgesContract {

	public static final String AUTHORITY = "course.labs.contentproviderlab.provider";
	public static final Uri BASE_URI = Uri
			.parse("content://" + AUTHORITY + "/");

	public static final String BADGES_TABLE_NAME = "badges";

	// The URI for this table.
	public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,
			BADGES_TABLE_NAME);

	public static final String _ID = "_id";
	public static final String FLAG_BITMAP_PATH = "flagBitmapPath";
	public static final String COUNTRY_NAME = "countryName";
	public static final String PLACE_NAME = "placeName";
	public static final String LAT = "lat";
	public static final String LON = "lon";

}
