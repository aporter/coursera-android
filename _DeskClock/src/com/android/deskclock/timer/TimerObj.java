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

package com.android.deskclock.timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.android.deskclock.Utils;

public class TimerObj implements Parcelable {

    private static final String TAG = "TimerObj";
    // Max timer length is 9 hours + 99 minutes + 9 seconds
    private static final long MAX_TIMER_LENGTH = (9 * 3600 + 99 * 60  + 60) * 1000;

    public int mTimerId;             // Unique id
    public long mStartTime;          // With mTimeLeft , used to calculate the correct time
    public long mTimeLeft;           // in the timer.
    public long mOriginalLength;     // length set at start of timer and by +1 min after times up
    public long mSetupLength;        // length set at start of timer
    public View mView;
    public int mState;
    public String mLabel;

    public static final int STATE_RUNNING = 1;
    public static final int STATE_STOPPED = 2;
    public static final int STATE_TIMESUP = 3;
    public static final int STATE_DONE = 4;
    public static final int STATE_RESTART = 5;

    private static final String PREF_TIMER_ID = "timer_id_";
    private static final String PREF_START_TIME  = "timer_start_time_";
    private static final String PREF_TIME_LEFT = "timer_time_left_";
    private static final String PREF_ORIGINAL_TIME = "timer_original_timet_";
    private static final String PREF_SETUP_TIME = "timer_setup_timet_";
    private static final String PREF_STATE = "timer_state_";
    private static final String PREF_LABEL = "timer_label_";

    private static final String PREF_TIMERS_LIST = "timers_list";

    public static final Parcelable.Creator<TimerObj> CREATOR = new Parcelable.Creator<TimerObj>() {
        @Override
        public TimerObj createFromParcel(Parcel p) {
            return new TimerObj(p);
        }

        @Override
        public TimerObj[] newArray(int size) {
            return new TimerObj[size];
        }
    };

    public void writeToSharedPref(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        String key = PREF_TIMER_ID + Integer.toString(mTimerId);
        String id = Integer.toString(mTimerId);
        editor.putInt (key, mTimerId);
        key = PREF_START_TIME + id;
        editor.putLong (key, mStartTime);
        key = PREF_TIME_LEFT + id;
        editor.putLong (key, mTimeLeft);
        key = PREF_ORIGINAL_TIME + id;
        editor.putLong (key, mOriginalLength);
        key = PREF_SETUP_TIME + id;
        editor.putLong (key, mSetupLength);
        key = PREF_STATE + id;
        editor.putInt (key, mState);
        Set <String> timersList = prefs.getStringSet(PREF_TIMERS_LIST, new HashSet<String>());
        timersList.add(id);
        editor.putStringSet(PREF_TIMERS_LIST, timersList);
        key = PREF_LABEL + id;
        editor.putString(key, mLabel);
        editor.apply();
    }


    public void readFromSharedPref(SharedPreferences prefs) {
        String id = Integer.toString(mTimerId);
        String key = PREF_START_TIME + id;
        mStartTime = prefs.getLong(key, 0);
        key = PREF_TIME_LEFT + id;
        mTimeLeft = prefs.getLong(key, 0);
        key = PREF_ORIGINAL_TIME + id;
        mOriginalLength = prefs.getLong(key, 0);
        key = PREF_SETUP_TIME + id;
        mSetupLength = prefs.getLong(key, 0);
        key = PREF_STATE + id;
        mState = prefs.getInt(key, 0);
        key = PREF_LABEL + id;
        mLabel = prefs.getString(key, "");
    }

    public void deleteFromSharedPref(SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        String key = PREF_TIMER_ID + Integer.toString(mTimerId);
        String id = Integer.toString(mTimerId);
        editor.remove (key);
        key = PREF_START_TIME + id;
        editor.remove (key);
        key = PREF_TIME_LEFT + id;
        editor.remove (key);
        key = PREF_ORIGINAL_TIME + id;
        editor.remove (key);
        key = PREF_STATE + id;
        editor.remove (key);
        Set <String> timersList = prefs.getStringSet(PREF_TIMERS_LIST, new HashSet<String>());
        timersList.remove(id);
        editor.putStringSet(PREF_TIMERS_LIST, timersList);
        key = PREF_LABEL + id;
        editor.remove(key);
        editor.commit();
        //dumpTimersFromSharedPrefs(prefs);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mTimerId);
        dest.writeLong(mStartTime);
        dest.writeLong(mTimeLeft);
        dest.writeLong(mOriginalLength);
        dest.writeLong(mSetupLength);
        dest.writeInt(mState);
        dest.writeString(mLabel);
    }

    public TimerObj(Parcel p) {
        mTimerId = p.readInt();
        mStartTime = p.readLong();
        mTimeLeft = p.readLong();
        mOriginalLength = p.readLong();
        mSetupLength = p.readLong();
        mState = p.readInt();
        mLabel = p.readString();
    }

    public TimerObj() {
        init(0);
    }

    public TimerObj(long timerLength) {
      init(timerLength);
    }

    private void init (long length) {
        mTimerId = (int) Utils.getTimeNow();
        mStartTime = Utils.getTimeNow();
        mTimeLeft = mOriginalLength = mSetupLength = length;
        mLabel = "";
    }

    public long updateTimeLeft(boolean forceUpdate) {
        if (isTicking() || forceUpdate) {
            long millis = Utils.getTimeNow();
            mTimeLeft = mOriginalLength - (millis - mStartTime);
        }
        return mTimeLeft;
    }

    public boolean isTicking() {
        return mState == STATE_RUNNING || mState == STATE_TIMESUP;
    }

    public boolean isInUse() {
        return mState == STATE_RUNNING || mState == STATE_STOPPED;
    }

    public void addTime(long time) {
        mTimeLeft = mOriginalLength - (Utils.getTimeNow() - mStartTime);
        if (mTimeLeft < MAX_TIMER_LENGTH - time) {
                mOriginalLength += time;
        }
    }

    public long getTimesupTime() {
        return mStartTime + mOriginalLength;
    }


    public static void getTimersFromSharedPrefs(
            SharedPreferences prefs, ArrayList<TimerObj> timers) {
        Object[] timerStrings =
                prefs.getStringSet(PREF_TIMERS_LIST, new HashSet<String>()).toArray();
        if (timerStrings.length > 0) {
            for (int i = 0; i < timerStrings.length; i++) {
                TimerObj t = new TimerObj();
                t.mTimerId = Integer.parseInt((String)timerStrings[i]);
                t.readFromSharedPref(prefs);
                timers.add(t);
            }
            Collections.sort(timers, new Comparator<TimerObj>() {
                @Override
                public int compare(TimerObj timerObj1, TimerObj timerObj2) {
                   return timerObj2.mTimerId - timerObj1.mTimerId;
                }
            });
        }
    }

    public static void getTimersFromSharedPrefs(
            SharedPreferences prefs, ArrayList<TimerObj> timers, int match) {
        Object[] timerStrings = prefs.getStringSet(PREF_TIMERS_LIST, new HashSet<String>())
                .toArray();
        if (timerStrings.length > 0) {
            for (int i = 0; i < timerStrings.length; i++) {
                TimerObj t = new TimerObj();
                t.mTimerId = Integer.parseInt((String) timerStrings[i]);
                t.readFromSharedPref(prefs);
                if (t.mState == match) {
                    timers.add(t);
                }
            }
        }
    }

    public static void putTimersInSharedPrefs(
            SharedPreferences prefs, ArrayList<TimerObj> timers) {
        if (timers.size() > 0) {
            for (int i = 0; i < timers.size(); i++) {
                TimerObj t = timers.get(i);
                timers.get(i).writeToSharedPref(prefs);
            }
        }
    }

    public static void dumpTimersFromSharedPrefs(
            SharedPreferences prefs) {
        Object[] timerStrings =
                prefs.getStringSet(PREF_TIMERS_LIST, new HashSet<String>()).toArray();
        Log.v(TAG,"--------------------- timers list in shared prefs");
        if (timerStrings.length > 0) {
            for (int i = 0; i < timerStrings.length; i++) {
                int id = Integer.parseInt((String)timerStrings[i]);
                Log.v(TAG,"---------------------timer  " + (i + 1) + ": id - " + id);
            }
        }
    }

    public static void cleanTimersFromSharedPrefs(SharedPreferences prefs) {
        ArrayList<TimerObj> timers = new  ArrayList<TimerObj>();
        getTimersFromSharedPrefs(prefs, timers);
        Iterator<TimerObj> i = timers.iterator();
        while(i.hasNext()) {
            i.next().deleteFromSharedPref(prefs);
        }
    }

}
