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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TimeZone;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.deskclock.Alarms;
import com.android.deskclock.DeskClock;
import com.android.deskclock.Log;
import com.android.deskclock.R;
import com.android.deskclock.SettingsActivity;
import com.android.deskclock.Utils;

/**
 * Cities chooser for the world clock
 */
public class CitiesActivity extends Activity implements OnCheckedChangeListener, View.OnClickListener {

    /** This must be false for production.  If true, turns on logging,
        test code, etc. */
    static final boolean DEBUG = false;
    static final String TAG = "CitiesActivity";

    private LayoutInflater mFactory;
    private ListView mCitiesList;
    private CityAdapter mAdapter;
    private HashMap<String, CityObj> mUserSelectedCities;
    private Calendar mCalendar;
    private final Collator mCollator = Collator.getInstance();


/***
* Adapter for a list of cities with the respected time zone.
* The Adapter sorts the list alphabetically and create an indexer.
***/

    private class CityAdapter extends BaseAdapter implements SectionIndexer {
        private static final String DELETED_ENTRY = "C0";
        private Object [] mAllTheCitiesList;                      // full list of the cities
        private final HashMap<String, CityObj> mSelectedCitiesList; // Selected cities by the use
        private final LayoutInflater mInflater;
        private boolean mIs24HoursMode;                            // AM/PM or 24 hours mode
        private Object [] mSectionHeaders;
        private Object [] mSectionPositions;

        public CityAdapter(
                Context context,  HashMap<String, CityObj> selectedList, LayoutInflater factory) {
            super();
            loadCitiesDataBase(context);
            mSelectedCitiesList = selectedList;
            mInflater = factory;
            mCalendar = Calendar.getInstance();
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            set24HoursMode(context);
        }

        @Override
        public int getCount() {
            return mAllTheCitiesList.length;
        }

        @Override
        public Object getItem(int p) {
            if (p >=0 && p < mAllTheCitiesList.length) {
                return mAllTheCitiesList [p];
            }
            return null;
        }

        @Override
        public long getItemId(int p) {
            return p;
        }

        @Override
        public boolean isEnabled(int p) {
            return ((CityObj)mAllTheCitiesList[p]).mCityId != null;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (position < 0 || position >=  mAllTheCitiesList.length) {
                return null;
            }
            CityObj c = (CityObj)mAllTheCitiesList [position];
            // Header view (A CityObj with nothing but the first letter as the name
            if (c.mCityId == null) {
                if (view == null || view.findViewById(R.id.header) == null) {
                    view =  mInflater.inflate(R.layout.city_list_header, parent, false);
                }
                TextView header = (TextView)view.findViewById(R.id.header);
                header.setText(c.mCityName);
            } else { // City view
                // Make sure to recycle a City view only
                if (view == null || view.findViewById(R.id.city_name) == null) {
                    view = mInflater.inflate(R.layout.city_list_item, parent, false);
                }
                view.setOnClickListener(CitiesActivity.this);
                TextView name = (TextView)view.findViewById(R.id.city_name);
                TextView tz = (TextView)view.findViewById(R.id.city_time);
                CheckBox cb = (CheckBox)view.findViewById(R.id.city_onoff);
                cb.setTag(c);
                cb.setChecked(mSelectedCitiesList.containsKey(c.mCityId));
                cb.setOnCheckedChangeListener(CitiesActivity.this);
                mCalendar.setTimeZone(TimeZone.getTimeZone(c.mTimeZone));
                tz.setText(DateFormat.format(mIs24HoursMode ? "k:mm" : "h:mmaa", mCalendar));
                name.setText(c.mCityName);
            }
            return view;
        }

        public void set24HoursMode(Context c) {
            mIs24HoursMode = Alarms.get24HourMode(c);
            notifyDataSetChanged();
        }

        private void loadCitiesDataBase(Context c) {
            Resources r = c.getResources();
            // Read strings array of name,timezone, id
            // make sure the list are the same length
            String [] cities = r.getStringArray(R.array.cities_names);
            String [] timezones = r.getStringArray(R.array.cities_tz);
            String [] ids = r.getStringArray(R.array.cities_id);
            if (cities.length != timezones.length || ids.length != cities.length) {
                Log.wtf("City lists sizes are not the same, cannot use the data");
                return;
             }
             CityObj[] tempList = new CityObj [cities.length];
             for (int i = 0; i < cities.length; i++) {
                tempList[i] = new CityObj(cities[i], timezones[i], ids[i]);
             }
             // Sort alphabetically
            Arrays.sort(tempList, new Comparator<CityObj> () {
                @Override
                public int compare(CityObj c1, CityObj c2) {
                    return mCollator.compare(c1.mCityName, c2.mCityName);
                }
            });
            //Create section indexer and add headers to the cities list
            String val = null;
            ArrayList<String> sections = new ArrayList<String> ();
            ArrayList<Integer> positions = new ArrayList<Integer> ();
            ArrayList<CityObj> items = new ArrayList<CityObj>();
            int count = 0;
            for (int i = 0; i < tempList.length; i++) {
                CityObj city = tempList[i];
                if (city.mCityId.equals(DELETED_ENTRY)) {
                    continue;
                }
                if (!city.mCityName.substring(0, 1).equals(val)) {
                    val = city.mCityName.substring(0, 1);
                    sections.add((new String(val)).toUpperCase());
                    positions.add(count);
                    // Add a header
                    items.add(new CityObj(val, null, null));
                    count++;
                }
                items.add(city);
                count++;
            }
            mSectionHeaders = sections.toArray();
            mSectionPositions = positions.toArray();
            mAllTheCitiesList = items.toArray();
         }

        @Override
        public int getPositionForSection(int section) {
            return (Integer) mSectionPositions[section];
        }

        @Override
        public int getSectionForPosition(int p) {
            for (int i = 0; i < mSectionPositions.length - 1; i++) {
                if (p >= (Integer)mSectionPositions[i] && p < (Integer)mSectionPositions[i + 1]) {
                    return i;
                }
            }
            if (p >= (Integer)mSectionPositions[mSectionPositions.length - 1]) {
                return mSectionPositions.length - 1;
            }
            return 0;
        }

        @Override
        public Object[] getSections() {
            return mSectionHeaders;
        }
    }


    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mFactory = LayoutInflater.from(this);
        updateLayout();
    }

    private void updateLayout() {
        setContentView(R.layout.cities_activity);
        mCitiesList = (ListView) findViewById(R.id.cities_list);
        mCitiesList.setFastScrollAlwaysVisible(true);
        mCitiesList.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        mCitiesList.setFastScrollEnabled(true);
        mUserSelectedCities = Cities.readCitiesFromSharedPrefs(
                PreferenceManager.getDefaultSharedPreferences(this));
        mAdapter = new CityAdapter(this, mUserSelectedCities, mFactory);
        mCitiesList.setAdapter(mAdapter);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP, ActionBar.DISPLAY_HOME_AS_UP);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            mAdapter.set24HoursMode(this);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Cities.saveCitiesToSharedPrefs(PreferenceManager.getDefaultSharedPreferences(this),
                mUserSelectedCities);
        Intent i = new Intent(Cities.WORLDCLOCK_UPDATE_INTENT);
        sendBroadcast(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                startActivity(new Intent(this, SettingsActivity.class));
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
            case android.R.id.home:
                Intent intent = new Intent(this, DeskClock.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cities_menu, menu);
        MenuItem help = menu.findItem(R.id.menu_item_help);
        if (help != null) {
            Utils.prepareHelpMenuItem(this, help);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCheckedChanged(CompoundButton b, boolean checked) {
        CityObj c = (CityObj)b.getTag();
        if (checked) {
            mUserSelectedCities.put(c.mCityId, c);
        } else {
            mUserSelectedCities.remove(c.mCityId);
        }
    }

    @Override
    public void onClick(View v) {
        CompoundButton b = (CompoundButton)v.findViewById(R.id.city_onoff);
        boolean checked = b.isChecked();
        onCheckedChanged(b, checked);
        b.setChecked(!checked);
    }
}
