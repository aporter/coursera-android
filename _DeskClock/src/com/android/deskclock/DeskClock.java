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

import java.util.ArrayList;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.android.deskclock.stopwatch.StopwatchFragment;
import com.android.deskclock.stopwatch.StopwatchService;
import com.android.deskclock.stopwatch.Stopwatches;
import com.android.deskclock.timer.TimerFragment;
import com.android.deskclock.timer.TimerObj;
import com.android.deskclock.timer.Timers;
import com.android.deskclock.worldclock.CitiesActivity;

/**
 * DeskClock clock view for desk docks.
 */
public class DeskClock extends Activity implements LabelDialogFragment.TimerLabelDialogHandler {
    private static final boolean DEBUG = false;

    private static final String LOG_TAG = "DeskClock";

    // Alarm action for midnight (so we can update the date display).
    private static final String KEY_SELECTED_TAB = "selected_tab";
    private static final String KEY_CLOCK_STATE = "clock_state";

    public static final String SELECT_TAB_INTENT_EXTRA = "deskclock.select.tab";

    private ActionBar mActionBar;
    private Tab mTimerTab;
    private Tab mClockTab;
    private Tab mStopwatchTab;

    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;

    public static final int TIMER_TAB_INDEX = 0;
    public static final int CLOCK_TAB_INDEX = 1;
    public static final int STOPWATCH_TAB_INDEX = 2;

    private int mSelectedTab;

    @Override
    public void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);
        if (DEBUG) Log.d(LOG_TAG, "onNewIntent with intent: " + newIntent);

        // update our intent so that we can consult it to determine whether or
        // not the most recent launch was via a dock event
        setIntent(newIntent);

        // Timer receiver may ask to go to the timers fragment if a timer expired.
        int tab = newIntent.getIntExtra(SELECT_TAB_INTENT_EXTRA, -1);
        if (tab != -1) {
            if (mActionBar != null) {
                mActionBar.setSelectedNavigationItem(tab);
            }
        }
    }

    private void initViews() {

        if (mTabsAdapter == null) {
            mViewPager = new ViewPager(this);
            mViewPager.setId(R.id.desk_clock_pager);
            mTabsAdapter = new TabsAdapter(this, mViewPager);
            createTabs(mSelectedTab);
        }
        setContentView(mViewPager);
        mActionBar.setSelectedNavigationItem(mSelectedTab);
    }

    private void createTabs(int selectedIndex) {
        mActionBar = getActionBar();

        mActionBar.setDisplayOptions(0);
        if (mActionBar != null) {
            mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            mTimerTab = mActionBar.newTab();
            mTimerTab.setIcon(R.drawable.timer_tab);
            mTimerTab.setContentDescription(R.string.menu_timer);
            mTabsAdapter.addTab(mTimerTab, TimerFragment.class,TIMER_TAB_INDEX);

            mClockTab = mActionBar.newTab();
            mClockTab.setIcon(R.drawable.clock_tab);
            mClockTab.setContentDescription(R.string.menu_clock);
            mTabsAdapter.addTab(mClockTab, ClockFragment.class,CLOCK_TAB_INDEX);
            mStopwatchTab = mActionBar.newTab();
            mStopwatchTab.setIcon(R.drawable.stopwatch_tab);
            mStopwatchTab.setContentDescription(R.string.menu_stopwatch);
            mTabsAdapter.addTab(mStopwatchTab, StopwatchFragment.class,STOPWATCH_TAB_INDEX);
            mActionBar.setSelectedNavigationItem(selectedIndex);
        }
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mSelectedTab = CLOCK_TAB_INDEX;
        if (icicle != null) {
            mSelectedTab = icicle.getInt(KEY_SELECTED_TAB, CLOCK_TAB_INDEX);
        }

        // Timer receiver may ask the app to go to the timer fragment if a timer expired
        Intent i = getIntent();
        if (i != null) {
            int tab = i.getIntExtra(SELECT_TAB_INTENT_EXTRA, -1);
            if (tab != -1) {
                mSelectedTab = tab;
            }
        }
        initViews();
        setHomeTimeZone();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent stopwatchIntent = new Intent(getApplicationContext(), StopwatchService.class);
        stopwatchIntent.setAction(Stopwatches.KILL_NOTIF);
        startService(stopwatchIntent);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Timers.NOTIF_APP_OPEN, true);
        editor.apply();
        Intent timerIntent = new Intent();
        timerIntent.setAction(Timers.NOTIF_IN_USE_CANCEL);
        sendBroadcast(timerIntent);
    }

    @Override
    public void onPause() {

        Intent intent = new Intent(getApplicationContext(), StopwatchService.class);
        intent.setAction(Stopwatches.SHOW_NOTIF);
        startService(intent);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Timers.NOTIF_APP_OPEN, false);
        editor.apply();
        Utils.showInUseNotifications(this);

        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_TAB, mActionBar.getSelectedNavigationIndex());
    }

    public void clockButtonsOnClick(View v) {
        if (v == null)
            return;
        switch (v.getId()) {
            case R.id.alarms_button:
                startActivity(new Intent(this, AlarmClock.class));
                break;
            case R.id.cities_button:
                startActivity(new Intent(this, CitiesActivity.class));
                break;
            case R.id.menu_button:
                showMenu(v);
                break;
            default:
                break;
        }
    }

    private void showMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener () {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_settings:
                        startActivity(new Intent(DeskClock.this, SettingsActivity.class));
                        return true;
                    case R.id.menu_item_help:
                        Intent i = item.getIntent();
                        if (i != null) {
                            try {
                                startActivity(i);
                            } catch (ActivityNotFoundException e) {
                                // No activity found to match the intent - ignore
                            }
                        }
                        return true;
                    case R.id.menu_item_night_mode:
                        startActivity(new Intent(DeskClock.this, ScreensaverActivity.class));
                    default:
                        break;
                }
                return true;
            }
        });
        popupMenu.inflate(R.menu.desk_clock_menu);

        Menu menu = popupMenu.getMenu();
        MenuItem help = menu.findItem(R.id.menu_item_help);
        if (help != null) {
            Utils.prepareHelpMenuItem(this, help);
        }
        popupMenu.show();
    }

    /***
     * Insert the local time zone as the Home Time Zone if one is not set
     */
    private void setHomeTimeZone() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String homeTimeZone = prefs.getString(SettingsActivity.KEY_HOME_TZ, "");
        if (!homeTimeZone.isEmpty()) {
        return;
        }
        homeTimeZone = TimeZone.getDefault().getID();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SettingsActivity.KEY_HOME_TZ, homeTimeZone);
        editor.apply();
        Log.v(LOG_TAG, "Setting home time zone to " + homeTimeZone);
    }

    public boolean isClockTab() {
        return mViewPager.getCurrentItem() == CLOCK_TAB_INDEX;
    }

    /***
     * Adapter for wrapping together the ActionBar's tab with the ViewPager
     */

    private class TabsAdapter extends FragmentPagerAdapter
            implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

        private static final String KEY_TAB_POSITION = "tab_position";

        final class TabInfo {
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(Class<?> _class, int position) {
                clss = _class;
                args = new Bundle();
                args.putInt(KEY_TAB_POSITION, position);
            }

            public int getPosition() {
                return args.getInt(KEY_TAB_POSITION, 0);
            }
        }

        private final ArrayList<TabInfo> mTabs = new ArrayList <TabInfo>();
        ActionBar mMainActionBar;
        Context mContext;
        ViewPager mPager;

        public TabsAdapter(Activity activity, ViewPager pager) {
            super(activity.getFragmentManager());
            mContext = activity;
            mMainActionBar = activity.getActionBar();
            mPager = pager;
            mPager.setAdapter(this);
            mPager.setOnPageChangeListener(this);
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            DeskClockFragment f = (DeskClockFragment) Fragment.instantiate(
                    mContext, info.clss.getName(), info.args);
            return f;
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        public void addTab(ActionBar.Tab tab, Class<?> clss, int position) {
            TabInfo info = new TabInfo(clss, position);
            tab.setTag(info);
            tab.setTabListener(this);
            mTabs.add(info);
            mMainActionBar.addTab(tab);
            notifyDataSetChanged();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Do nothing
        }

        @Override
        public void onPageSelected(int position) {
            mMainActionBar.setSelectedNavigationItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Do nothing
        }

        @Override
        public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
            // Do nothing
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            TabInfo info = (TabInfo)tab.getTag();
            mPager.setCurrentItem(info.getPosition());
        }

        @Override
        public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
            // Do nothing

        }
    }

    public static abstract class OnTapListener implements OnTouchListener {
        private float mLastTouchX;
        private float mLastTouchY;
        private long mLastTouchTime;
        private TextView mMakePressedTextView;
        private int mPressedColor, mGrayColor;
        private final float MAX_MOVEMENT_ALLOWED = 20;
        private final long MAX_TIME_ALLOWED = 500;

        public OnTapListener(Activity activity, TextView makePressedView) {
            mMakePressedTextView = makePressedView;
            mPressedColor = activity.getResources().getColor(Utils.getPressedColorId());
            mGrayColor = activity.getResources().getColor(Utils.getGrayColorId());
        }

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (e.getAction()) {
                case (MotionEvent.ACTION_DOWN):
                    mLastTouchTime = Utils.getTimeNow();
                    mLastTouchX = e.getX();
                    mLastTouchY = e.getY();
                    if (mMakePressedTextView != null) {
                        mMakePressedTextView.setTextColor(mPressedColor);
                    }
                    break;
                case (MotionEvent.ACTION_UP):
                    float xDiff = Math.abs(e.getX()-mLastTouchX);
                    float yDiff = Math.abs(e.getY()-mLastTouchY);
                    long timeDiff = (Utils.getTimeNow() - mLastTouchTime);
                    if (xDiff < MAX_MOVEMENT_ALLOWED && yDiff < MAX_MOVEMENT_ALLOWED
                            && timeDiff < MAX_TIME_ALLOWED) {
                        if (mMakePressedTextView != null) {
                            v = mMakePressedTextView;
                        }
                        processClick(v);
                        resetValues();
                        return true;
                    }
                    resetValues();
                    break;
                case (MotionEvent.ACTION_MOVE):
                    xDiff = Math.abs(e.getX()-mLastTouchX);
                    yDiff = Math.abs(e.getY()-mLastTouchY);
                    if (xDiff >= MAX_MOVEMENT_ALLOWED || yDiff >= MAX_MOVEMENT_ALLOWED) {
                        resetValues();
                    }
                    break;
                default:
                    resetValues();
            }
            return false;
        }

        private void resetValues() {
            mLastTouchX = -1*MAX_MOVEMENT_ALLOWED + 1;
            mLastTouchY = -1*MAX_MOVEMENT_ALLOWED + 1;
            mLastTouchTime = -1*MAX_TIME_ALLOWED + 1;
            if (mMakePressedTextView != null) {
                mMakePressedTextView.setTextColor(mGrayColor);
            }
        }

        protected abstract void processClick(View v);
    }

    /** Called by the LabelDialogFormat class after the dialog is finished. **/
    @Override
    public void onDialogLabelSet(TimerObj timer, String label, String tag) {
        Fragment frag = getFragmentManager().findFragmentByTag(tag);
        if (frag instanceof TimerFragment) {
            ((TimerFragment) frag).setLabel(timer, label);
        }
    }
}
