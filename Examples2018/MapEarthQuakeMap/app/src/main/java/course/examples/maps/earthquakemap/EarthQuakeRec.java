package course.examples.maps.earthquakemap;

class EarthQuakeRec {
	private final double lat;
	private final double lng;
	private final double magnitude;

	EarthQuakeRec(double lat, double lng, double magnitude) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.magnitude = magnitude;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public double getMagnitude() {
		return magnitude;
	}
}
