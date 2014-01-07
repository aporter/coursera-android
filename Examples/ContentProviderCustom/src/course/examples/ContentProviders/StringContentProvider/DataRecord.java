package course.examples.ContentProviders.StringContentProvider;

class DataRecord {

	private static int id;

	// Unique ID
	private final int _id;

	// Display Name
	private final String _data;

	DataRecord(String _data) {
		this._data = _data;
		this._id = id++;
	}

	String getData() {
		return _data;
	}

	int getID() {
		return _id;
	}

}
