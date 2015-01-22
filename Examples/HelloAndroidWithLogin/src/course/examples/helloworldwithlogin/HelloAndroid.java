package course.examples.helloworldwithlogin;

import android.app.Activity;
import android.os.Bundle;

public class HelloAndroid extends Activity {
    public void onCreate(Bundle savedInstanceState) {

        // Required call through to Activity.onCreate()
        // Restore any saved instance state
        super.onCreate(savedInstanceState);

        // Set up the application's user interface (content view)
        setContentView(R.layout.helloandroid);
    }
}