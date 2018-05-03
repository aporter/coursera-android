package course.examples.contentproviders.stringcontentprovider;

import java.util.Random;

class DataRecord {

	private final String mData;
    private final int mId;

	DataRecord(String data) {
		this.mData = data;
		mId = new Random().nextInt(Integer.MAX_VALUE);
	}

    DataRecord(int id, String data) {
        this.mData = data;
        mId = id;
    }

	String getData() {
		return mData;
	}

	int getID() {
		return mId;
	}

    @Override
    public String toString() {
        return "\"" + mData + "\"" + ":" + String.valueOf(mId) ;
    }
}
