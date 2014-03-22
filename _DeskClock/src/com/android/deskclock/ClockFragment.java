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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.android.deskclock.worldclock.WorldClockAdapter;

/**
 * Fragment that shows  the clock (analog or digital), the next alarm info and the world clock.
 */

public class ClockFragment extends DeskClockFragment implements OnSharedPreferenceChangeListener {

    private static final String BUTTONS_HIDDEN_KEY = "buttons_hidden";
    private final static String TAG = "ClockFragment";

    private View mButtons;
    private boolean mButtonsHidden = false;
    private View mDigitalClock, mAnalogClock, mClockFrame;
    private WorldClockAdapter mAdapter;
    private ListView mList;
    private SharedPreferences mPrefs;
    private String mDateFormat;
    private String mDateFormatForAccessibility;
    private String mDefaultClockStyle;
    private String mClockStyle;

    private PendingIntent mQuarterlyIntent;
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
            @Override
        public void onReceive(Context context, Intent intent) {
            boolean changed = intent.getAction().equals(Intent.ACTION_TIME_CHANGED)
                    || intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED);
            if (changed || intent.getAction().equals(Utils.ACTION_ON_QUARTER_HOUR)) {
                Utils.updateDate(mDateFormat, mDateFormatForAccessibility,mClockFrame);
                if (mAdapter != null) {
                    // *CHANGED may modify the need for showing the Home City
                    if (changed && (mAdapter.hasHomeCity() != mAdapter.needHomeCity())) {
                        mAdapter.loadData(context);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
            if (changed || intent.getAction().equals(Alarms.ALARM_DONE_ACTION)
                    || intent.getAction().equals(Alarms.ALARM_SNOOZE_CANCELLED)) {
                Utils.refreshAlarm(getActivity(), mClockFrame);
            }
        }
    };

    private final Handler mHandler = new Handler();

    public ClockFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle icicle) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.clock_fragment, container, false);
        mButtons = v.findViewById(R.id.clock_buttons);
        if (icicle != null) {
            mButtonsHidden = icicle.getBoolean(BUTTONS_HIDDEN_KEY, false);
        }
        mList = (ListView)v.findViewById(R.id.cities);
        mList.setDivider(null);
        View headerView = inflater.inflate(R.layout.blank_header_view, mList, false);
        mList.addHeaderView(headerView);
        mClockFrame = inflater.inflate(R.layout.main_clock_frame, mList, false);
        mDigitalClock = mClockFrame.findViewById(R.id.digital_clock);
        mAnalogClock = mClockFrame.findViewById(R.id.analog_clock);
        mList.addHeaderView(mClockFrame, null, false);
        View footerView = inflater.inflate(R.layout.blank_footer_view, mList, false);
        footerView.setBackgroundResource(R.color.blackish);
        mList.addFooterView(footerView);
        mAdapter = new WorldClockAdapter(getActivity());
        mList.setAdapter(mAdapter);
        mList.setOnTouchListener(new OnTouchListener() {
            private final float MAX_MOVEMENT_ALLOWED = 20;
            private float mLastTouchX, mLastTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        long time = Utils.getTimeNow();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(getActivity(), ScreensaverActivity.class));
                            }
                        }, ViewConfiguration.getLongPressTimeout());
                        mLastTouchX = event.getX();
                        mLastTouchY = event.getY();
                        break;
                    case (MotionEvent.ACTION_MOVE):
                        float xDiff = Math.abs(event.getX()-mLastTouchX);
                        float yDiff = Math.abs(event.getY()-mLastTouchY);
                        if (xDiff >= MAX_MOVEMENT_ALLOWED || yDiff >= MAX_MOVEMENT_ALLOWED) {
                            mHandler.removeCallbacksAndMessages(null);
                        }
                        break;
                    default:
                        mHandler.removeCallbacksAndMessages(null);
                }
                return false;
            }
        });
        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mDefaultClockStyle = getActivity().getResources().getString(R.string.default_clock_style);
        return v;
    }

    @Override
    public void onResume () {
        super.onResume();
        mPrefs.registerOnSharedPreferenceChangeListener(this);
        mDateFormat = getString(R.string.abbrev_wday_month_day_no_year);
        mDateFormatForAccessibility = getString(R.string.full_wday_month_day_no_year);

        long alarmOnQuarterHour = Utils.getAlarmOnQuarterHour();
        mQuarterlyIntent = PendingIntent.getBroadcast(
                getActivity(), 0, new Intent(Utils.ACTION_ON_QUARTER_HOUR), 0);
        ((AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE)).setRepeating(
                AlarmManager.RTC, alarmOnQuarterHour, AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                mQuarterlyIntent);
        // Besides monitoring when quarter-hour changes, monitor other actions that
        // effect clock time
        IntentFilter filter = new IntentFilter(Utils.ACTION_ON_QUARTER_HOUR);
        filter.addAction(Alarms.ALARM_DONE_ACTION);
        filter.addAction(Alarms.ALARM_SNOOZE_CANCELLED);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        getActivity().registerReceiver(mIntentReceiver, filter);

        mButtons.setAlpha(mButtonsHidden ? 0 : 1);

        // Resume can invoked after changing the cities list.
        if (mAdapter != null) {
            mAdapter.reloadData(getActivity());
        }
        // Resume can invoked after changing the clock style.
        View clockView = Utils.setClockStyle(getActivity(), mDigitalClock, mAnalogClock,
                SettingsActivity.KEY_CLOCK_STYLE);
        mClockStyle = (clockView == mDigitalClock ?
                Utils.CLOCK_TYPE_DIGITAL : Utils.CLOCK_TYPE_ANALOG);
        mAdapter.notifyDataSetChanged();

        Utils.updateDate(mDateFormat, mDateFormatForAccessibility,mClockFrame);
        Utils.refreshAlarm(getActivity(), mClockFrame);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPrefs.unregisterOnSharedPreferenceChangeListener(this);
        ((AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE)).cancel(
                mQuarterlyIntent);
        getActivity().unregisterReceiver(mIntentReceiver);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        outState.putBoolean(BUTTONS_HIDDEN_KEY, mButtonsHidden);
        super.onSaveInstanceState(outState);
    }

    public void showButtons(boolean show) {
        if (mButtons == null) {
            return;
        }
        if (show && mButtonsHidden) {
            mButtons.startAnimation(
                    AnimationUtils.loadAnimation(getActivity(), R.anim.unhide));
            mButtonsHidden = false;
        } else if (!show && !mButtonsHidden) {
            mButtons.startAnimation(
                    AnimationUtils.loadAnimation(getActivity(), R.anim.hide));
            mButtonsHidden = true;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key == SettingsActivity.KEY_CLOCK_STYLE) {
            mClockStyle = prefs.getString(SettingsActivity.KEY_CLOCK_STYLE, mDefaultClockStyle);
            mAdapter.notifyDataSetChanged();
        }
    }
 }
