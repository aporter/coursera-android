package course.examples.ContentProviders.ContactsList;

import android.net.Uri;

public class ContactInfo {

	// Display name for this contact
	String mName;

	// Uri pointing to contact's thumbnail 
	Uri mUri;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public Uri getUri() {
		return mUri;
	}

	// Convert String to Uri
	public void setUri(String uri) {
		this.mUri = Uri.parse(uri);
	}
}
