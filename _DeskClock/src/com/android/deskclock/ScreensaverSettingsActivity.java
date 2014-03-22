/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.deskclock;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * Settings for the Alarm Clock Dream (com.android.deskclock.Screensaver).
 */
public class ScreensaverSettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    static final String KEY_CLOCK_STYLE =
            "screensaver_clock_style";
    static final String KEY_NIGHT_MODE =
            "screensaver_night_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.dream_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public boolean onPreferenceChange(Preference pref, Object newValue) {
        if (KEY_CLOCK_STYLE.equals(pref.getKey())) {
            final ListPreference listPref = (ListPreference) pref;
            final int idx = listPref.findIndexOfValue((String) newValue);
            listPref.setSummary(listPref.getEntries()[idx]);
        } else if (KEY_NIGHT_MODE.equals(pref.getKey())) {
            boolean state = ((CheckBoxPreference) pref).isChecked();
        }
        return true;
    }

    private void refresh() {
        ListPreference listPref = (ListPreference) findPreference(KEY_CLOCK_STYLE);
        listPref.setSummary(listPref.getEntry());
        listPref.setOnPreferenceChangeListener(this);

        Preference pref = findPreference(KEY_NIGHT_MODE);
        boolean state = ((CheckBoxPreference) pref).isChecked();
        pref.setOnPreferenceChangeListener(this);
    }

}
