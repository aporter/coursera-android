package course.examples.datamanagement.preferencefragment

import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.Preference


class ViewAndUpdatePreferencesActivity : AppCompatActivity() {

    companion object {
        private const val USERNAME = "uname"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.user_prefs_fragment)

    }

    // Fragment that displays the username preference
    class UserPreferenceFragment : PreferenceFragmentCompat() {

        private lateinit var mListener: OnSharedPreferenceChangeListener
        private lateinit var mUserNamePreference: Preference

        override fun onCreatePreferences(p0: Bundle?, p1: String?) {
            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.user_prefs)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Get the username Preference
            mUserNamePreference = preferenceManager
                .findPreference(USERNAME)

            // Attach a listener to update summary when username changes
            mListener = OnSharedPreferenceChangeListener { sharedPreferences, _ ->
                mUserNamePreference.summary = sharedPreferences.getString(
                    USERNAME, "None Set"
                )
            }

            // Get SharedPreferences object managed by the PreferenceManager for
            // this Fragment
            val prefs = preferenceManager.sharedPreferences

            // Register a listener on the SharedPreferences object
            prefs.registerOnSharedPreferenceChangeListener(mListener)

            // Invoke callback manually to display the current username
            mListener.onSharedPreferenceChanged(prefs, USERNAME)

        }
    }
}
