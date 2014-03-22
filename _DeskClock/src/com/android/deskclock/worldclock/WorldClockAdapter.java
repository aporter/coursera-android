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

package com.android.deskclock.worldclock;

import java.text.Collator;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.deskclock.AnalogClock;
import com.android.deskclock.DigitalClock;
import com.android.deskclock.R;
import com.android.deskclock.SettingsActivity;

public class WorldClockAdapter extends BaseAdapter {
    protected Object [] mCitiesList;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private String mClockStyle;
    private final Collator mCollator = Collator.getInstance();

    public WorldClockAdapter(Context context) {
        super();
        mContext = context;
        loadData(context);
        mInflater = LayoutInflater.from(context);
    }

    public void reloadData(Context context) {
        loadData(context);
        notifyDataSetChanged();
    }

    public void loadData(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mClockStyle = prefs.getString(SettingsActivity.KEY_CLOCK_STYLE,
                mContext.getResources().getString(R.string.default_clock_style));
        mCitiesList = Cities.readCitiesFromSharedPrefs(prefs).values().toArray();
        sortList();
        mCitiesList = addHomeCity();
    }

    /***
     * Adds the home city as the first item of the adapter if the feature is on and the device time
     * zone is different from the home time zone that was set by the user.
     * return the list of cities.
     */
    private Object[] addHomeCity() {
        if (needHomeCity()) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
            String homeTZ = sharedPref.getString(SettingsActivity.KEY_HOME_TZ, "");
            CityObj c = new CityObj(
                    mContext.getResources().getString(R.string.home_label), homeTZ, null);
            Object[] temp = new Object[mCitiesList.length + 1];
            temp[0] = c;
            for (int i = 0; i < mCitiesList.length; i++) {
                temp[i + 1] = mCitiesList[i];
            }
            return temp;
        } else {
            return mCitiesList;
        }
    }

    public boolean needHomeCity() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (sharedPref.getBoolean(SettingsActivity.KEY_AUTO_HOME_CLOCK, false)) {
            String homeTZ = sharedPref.getString(
                    SettingsActivity.KEY_HOME_TZ, TimeZone.getDefault().getID());
            final Date now = new Date();
            return TimeZone.getTimeZone(homeTZ).getOffset(now.getTime())
                    != TimeZone.getDefault().getOffset(now.getTime());
        } else {
            return false;
        }
    }

    public boolean hasHomeCity() {
        return (mCitiesList != null) && mCitiesList.length > 0
                && ((CityObj) mCitiesList[0]).mCityId == null;
    }

    private void sortList() {
        final Date now = new Date();

        // Sort by the Offset from GMT taking DST into account
        // and if the same sort by City Name
        Arrays.sort(mCitiesList, new Comparator<Object>() {
            private int safeCityNameCompare(CityObj city1, CityObj city2) {
                if (city1.mCityName == null && city2.mCityName == null) {
                    return 0;
                } else if (city1.mCityName == null) {
                    return -1;
                } else if (city2.mCityName == null) {
                    return 1;
                } else {
                    return mCollator.compare(city1.mCityName, city2.mCityName);
                }
            }

            @Override
            public int compare(Object object1, Object object2) {
                CityObj city1 = (CityObj) object1;
                CityObj city2 = (CityObj) object2;
                if (city1.mTimeZone == null && city2.mTimeZone == null) {
                    return safeCityNameCompare(city1, city2);
                } else if (city1.mTimeZone == null) {
                    return -1;
                } else if (city2.mTimeZone == null) {
                    return 1;
                }

                int gmOffset1 = TimeZone.getTimeZone(city1.mTimeZone).getOffset(now.getTime());
                int gmOffset2 = TimeZone.getTimeZone(city2.mTimeZone).getOffset(now.getTime());
                if (gmOffset1 == gmOffset2) {
                    return safeCityNameCompare(city1, city2);
                } else {
                    return gmOffset1 - gmOffset2;
                }
            }
        });
    }

    @Override
    public int getCount() {
        // Each item in the list holds 1 or 2 clocks
        return (mCitiesList.length  + 1)/2;
    }

    @Override
    public Object getItem(int p) {
        return null;
    }

    @Override
    public long getItemId(int p) {
        return p;
    }

    @Override
    public boolean isEnabled(int p) {
        return false;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        // Index in cities list
        int index = position * 2;
        if (index < 0 || index >= mCitiesList.length) {
            return null;
        }

        if (view == null) {
            view = mInflater.inflate(R.layout.world_clock_list_item, parent, false);
        }
        // The world clock list item can hold two world clocks
        View rightClock = view.findViewById(R.id.city_right);
        updateView(view.findViewById(R.id.city_left), (CityObj)mCitiesList[index]);
        if (index + 1 < mCitiesList.length) {
            rightClock.setVisibility(View.VISIBLE);
            updateView(rightClock, (CityObj)mCitiesList[index + 1]);
        } else {
            // To make sure the spacing is right , make sure that the right clock style is selected
            // even if the clock is invisible.
            DigitalClock dclock = (DigitalClock)(rightClock.findViewById(R.id.digital_clock));
            AnalogClock aclock = (AnalogClock)(rightClock.findViewById(R.id.analog_clock));
            if (mClockStyle.equals("analog")) {
                dclock.setVisibility(View.GONE);
                aclock.setVisibility(View.INVISIBLE);
            } else {
                dclock.setVisibility(View.INVISIBLE);
                aclock.setVisibility(View.GONE);
            }
            rightClock.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    private void updateView(View clock, CityObj cityObj) {
        View nameLayout= clock.findViewById(R.id.city_name_layout);
        TextView name = (TextView)(nameLayout.findViewById(R.id.city_name));
        TextView dayOfWeek = (TextView)(nameLayout.findViewById(R.id.city_day));
        DigitalClock dclock = (DigitalClock)(clock.findViewById(R.id.digital_clock));
        AnalogClock aclock = (AnalogClock)(clock.findViewById(R.id.analog_clock));

        if (mClockStyle.equals("analog")) {
            dclock.setVisibility(View.GONE);
            aclock.setVisibility(View.VISIBLE);
            aclock.setTimeZone(cityObj.mTimeZone);
            aclock.enableSeconds(false);
        } else {
            dclock.setVisibility(View.VISIBLE);
            aclock.setVisibility(View.GONE);
            dclock.setTimeZone(cityObj.mTimeZone);
        }
        name.setText(cityObj.mCityName);
        final Calendar now = Calendar.getInstance();
        now.setTimeZone(TimeZone.getDefault());
        int myDayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        now.setTimeZone(TimeZone.getTimeZone(cityObj.mTimeZone));
        int cityDayOfWeek = now.get(Calendar.DAY_OF_WEEK);
        if (myDayOfWeek != cityDayOfWeek) {
            dayOfWeek.setText(mContext.getString(R.string.world_day_of_week_label,
                    now.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())));
            dayOfWeek.setVisibility(View.VISIBLE);
        } else {
            dayOfWeek.setVisibility(View.GONE);
        }
    }
}
