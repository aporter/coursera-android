package course.examples.ContentProviders.ContactsListWithAdapter;

import android.net.Uri;

public class ContactInfo {

	private String mName;
	private Uri mUri;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public Uri getUri() {
		return mUri;
	}

	public void setUri(String uri) {
		this.mUri = Uri.parse(uri);
	}
}
