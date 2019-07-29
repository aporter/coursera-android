package course.examples.helloworldwithlogin

import android.app.Activity
import android.os.Bundle

class HelloAndroid : Activity() {
    public override fun onCreate(savedInstanceState: Bundle?) {

        // Required call through to Activity.onCreate()
        // Restore any saved instance state
        super.onCreate(savedInstanceState)

        // Set up the application's user interface (content view)
        setContentView(R.layout.helloandroid)
    }
}