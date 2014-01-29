package course.examples.ContentProviders.ContactsListInsertContacts;

import course.examples.ContentProviders.ContactsListWithInsDel.R;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InsertActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Exit if there are no google accounts
		if (AccountManager.get(this).getAccountsByType("com.google").length == 0)
			finish();

		setContentView(R.layout.main);

		Button insertButton = (Button) findViewById(R.id.insert);
		insertButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Start the DisplayActivity
				
				startActivity(new Intent(InsertActivity.this,
						DisplayActivity.class));

			}
		});
	}
}
