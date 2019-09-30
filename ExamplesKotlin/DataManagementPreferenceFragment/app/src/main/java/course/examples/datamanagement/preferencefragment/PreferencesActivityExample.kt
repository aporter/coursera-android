package course.examples.datamanagement.preferencefragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class PreferencesActivityExample : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
    }

    // Open a User Preferences Entry Activity
    fun onButtonClick(v: View) {
        startActivity(
            Intent(
                this@PreferencesActivityExample,
                ViewAndUpdatePreferencesActivity::class.java
            )
        )
    }
}