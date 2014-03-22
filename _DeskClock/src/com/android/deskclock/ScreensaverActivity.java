/*
 * Copyright (C) 2012 The Android Open Source Project
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

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.BatteryManager;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.deskclock.Utils.ScreensaverMoveSaverRunnable;

public class ScreensaverActivity extends Activity {
    static final boolean DEBUG = false;
    static final String TAG = "DeskClock/ScreensaverActivity";

    // This value must match android:defaultValue of
    // android:key="screensaver_clock_style" in dream_settings.xml
    static final String DEFAULT_CLOCK_STYLE = "digital";

    private View mContentView, mSaverView;
    private View mAnalogClock, mDigitalClock;

    private final Handler mHandler = new Handler();
    private final ScreensaverMoveSaverRunnable mMoveSaverRunnable;
    private String mDateFormat;
    private String mDateFormatForAccessibility;
    private PendingIntent mQuarterlyIntent;
    private String mClockStyle;
    private boolean mPluggedIn = true;
    private final int mFlags = (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean changed = intent.getAction().equals(Intent.ACTION_TIME_CHANGED)
                    || intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED);
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                mPluggedIn = true;
                setWakeLock();
            } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                mPluggedIn = false;
                setWakeLock();
            } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
                finish();
            } else if (intent.getAction().equals(Utils.ACTION_ON_QUARTER_HOUR) || changed) {
                Utils.updateDate(mDateFormat, mDateFormatForAccessibility, mContentView);
            }

            if (changed) {
                Utils.refreshAlarm(ScreensaverActivity.this, mContentView);
            }

        }
    };

    public ScreensaverActivity() {
        if (DEBUG) Log.d(TAG, "Screensaver allocated");
        mMoveSaverRunnable = new ScreensaverMoveSaverRunnable(mHandler);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Utils.ACTION_ON_QUARTER_HOUR);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(mIntentReceiver, filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent chargingIntent =
                registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = chargingIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        mPluggedIn = plugged == BatteryManager.BATTERY_PLUGGED_AC
                || plugged == BatteryManager.BATTERY_PLUGGED_USB
                || plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS;

        mDateFormat = getString(R.string.abbrev_wday_month_day_no_year);
        mDateFormatForAccessibility = getString(R.string.full_wday_month_day_no_year);

        setWakeLock();
        layoutClockSaver();
        mHandler.post(mMoveSaverRunnable);

        long alarmOnQuarterHour = Utils.getAlarmOnQuarterHour();
        mQuarterlyIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(Utils.ACTION_ON_QUARTER_HOUR), 0);
        ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).setRepeating(
                AlarmManager.RTC, alarmOnQuarterHour, AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                mQuarterlyIntent);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mMoveSaverRunnable);
        ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).cancel(mQuarterlyIntent);
        finish();
        super.onPause();
    }

    @Override
    public void onStop() {
        unregisterReceiver(mIntentReceiver);
        super.onStop();
   }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (DEBUG) Log.d(TAG, "Screensaver configuration changed");
        super.onConfigurationChanged(newConfig);
        mHandler.removeCallbacks(mMoveSaverRunnable);
        layoutClockSaver();
        mHandler.postDelayed(mMoveSaverRunnable, 250);
    }

    @Override
    public void onUserInteraction() {
        // We want the screen saver to exit upon user interaction.
        finish();
    }

    private void setWakeLock() {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (mPluggedIn)
            winParams.flags |= mFlags;
        else
            winParams.flags &= (~mFlags);
        win.setAttributes(winParams);
    }

    private void setClockStyle() {
        Utils.setClockStyle(this, mDigitalClock, mAnalogClock,
                SettingsActivity.KEY_CLOCK_STYLE);
        mSaverView = findViewById(R.id.main_clock);
        mClockStyle = (mSaverView == mDigitalClock ?
                Utils.CLOCK_TYPE_DIGITAL : Utils.CLOCK_TYPE_ANALOG);
        Utils.dimClockView(true, mSaverView);
    }

    private void layoutClockSaver() {
        setContentView(R.layout.desk_clock_saver);
        mDigitalClock = findViewById(R.id.digital_clock);
        mAnalogClock = findViewById(R.id.analog_clock);
        setClockStyle();
        mContentView = (View) mSaverView.getParent();
        mContentView.forceLayout();
        mSaverView.forceLayout();
        mSaverView.setAlpha(0);

        mMoveSaverRunnable.registerViews(mContentView, mSaverView);

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Utils.updateDate(mDateFormat, mDateFormatForAccessibility,mContentView);
        Utils.refreshAlarm(ScreensaverActivity.this, mContentView);
    }

}
