package course.examples.notification.toastwithcustomview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NotificationToastActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		Button button = (Button) findViewById(R.id.toast_button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        
		        Toast toast = new Toast(getApplicationContext());

		        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		        toast.setDuration(Toast.LENGTH_LONG);
		        
		        toast.setView(getLayoutInflater().inflate(R.layout.custom_toast,null));
 
		        toast.show();
			}
		});

    }
}