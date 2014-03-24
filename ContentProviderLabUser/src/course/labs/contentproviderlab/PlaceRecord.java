package course.labs.contentproviderlab;

import android.graphics.Bitmap;
import android.location.Location;

public class PlaceRecord {

	// URL for retrieving the flag image
	private String mFlagUrl;

	// path to flag image in external memory 
	private String mFlagBitmapPath;
	
	private String mCountryName;
	private String mPlaceName;
	private Bitmap mFlagBitmap;
	private double lat;
	private double lon;

	public PlaceRecord(String flagUrl, String flagBitmapPath,
			String countryName, String placeName, double lat, double lon) {
		mFlagUrl = flagUrl;
		mFlagBitmapPath = flagBitmapPath;
		mCountryName = countryName;
		mPlaceName = placeName;
		setLat(lat);
		setLon(lon);
	}

	public PlaceRecord(Location location) {
		setLat(location.getLatitude());
		setLon(location.getLongitude());
	}

	public void setLocation(Location location) {
		setLat(location.getLatitude());
		setLon(location.getLongitude());
	}

	public String getFlagUrl() {
		return mFlagUrl;
	}

	public void setFlagUrl(String flagUrl) {
		this.mFlagUrl = flagUrl;
	}

	public String getCountryName() {
		return mCountryName;
	}

	public void setCountryName(String country) {
		this.mCountryName = country;
	}

	public String getPlace() {
		return mPlaceName;
	}

	public void setPlace(String place) {
		this.mPlaceName = place;
	}

	public Bitmap getFlagBitmap() {
		return mFlagBitmap;
	}

	public void setFlagBitmap(Bitmap mFlagBitmap) {
		this.mFlagBitmap = mFlagBitmap;
	}

	public boolean intersects(Location location) {

		double tolerance = 1000;
		float[] results = new float[3];

		Location.distanceBetween(location.getLatitude(),
				location.getLongitude(), lat, lon, results);

		return (results[0] <= tolerance);

	}

	@Override
	public String toString() {
		return "Place: " + mPlaceName + " Country: " + mCountryName;

	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getFlagBitmapPath() {
		return mFlagBitmapPath;
	}

	public void setFlagBitmapPath(String flagBitmapPath) {
		this.mFlagBitmapPath = flagBitmapPath;
	}

}
