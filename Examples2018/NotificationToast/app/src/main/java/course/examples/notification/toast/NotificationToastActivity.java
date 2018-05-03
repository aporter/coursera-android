package course.examples.notification.toast;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NotificationToastActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }

    public void onClick(@SuppressWarnings("unused") View v) {
        Toast.makeText(getApplicationContext(), getString(R.string.youre_toast_string), Toast.LENGTH_LONG).show();
    }
}