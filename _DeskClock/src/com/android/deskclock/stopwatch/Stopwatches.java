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

package com.android.deskclock.stopwatch;

import android.content.Context;

import com.android.deskclock.R;

public class Stopwatches {
    // Private actions processed by the receiver
    public static final String START_STOPWATCH = "start_stopwatch";
    public static final String LAP_STOPWATCH = "lap_stopwatch";
    public static final String STOP_STOPWATCH = "stop_stopwatch";
    public static final String RESET_STOPWATCH = "reset_stopwatch";
    public static final String SHARE_STOPWATCH = "share_stopwatch";
    public static final String RESET_AND_LAUNCH_STOPWATCH = "reset_and_launch_stopwatch";
    public static final String MESSAGE_TIME = "message_time";
    public static final String SHOW_NOTIF = "show_notification";
    public static final String KILL_NOTIF = "kill_notification";
    public static final String PREF_START_TIME  = "sw_start_time";
    public static final String PREF_ACCUM_TIME = "sw_accum_time";
    public static final String PREF_STATE = "sw_state";
    public static final String PREF_LAP_NUM = "sw_lap_num";
    public static final String PREF_LAP_TIME = "sw_lap_time_";
    public static final String PREF_UPDATE_CIRCLE = "sw_update_circle";
    public static final String NOTIF_CLOCK_BASE = "notif_clock_base";
    public static final String NOTIF_CLOCK_ELAPSED = "notif_clock_elapsed";
    public static final String NOTIF_CLOCK_RUNNING = "notif_clock_running";
    public static final String KEY = "sw";

    public static final int STOPWATCH_RESET = 0;
    public static final int STOPWATCH_RUNNING = 1;
    public static final int STOPWATCH_STOPPED = 2;

    public static final int MAX_LAPS = 99;

    public static String getShareTitle(Context context) {
        String [] mLabels = context.getResources().getStringArray(R.array.sw_share_strings);
        return mLabels[(int)(Math.random() * mLabels.length)];
    }

    public static String buildShareResults(Context context, String time, long[] laps) {
        String results = context.getString(R.string.sw_share_main, time + "\n");
        int lapsNum = laps == null? 0 : laps.length;
        if (lapsNum == 0) {
            return results;
        }
        results += context.getString(R.string.sw_share_laps) + "\n";
        for (int i = 1; i <= lapsNum; i ++) {
            results += String.format("%d. %s\n", i, getTimeText(laps[lapsNum-i]));
        }
        return results;
    }

    public static String buildShareResults(Context context, long time, long[] laps) {
        return buildShareResults(context, getTimeText(time), laps);
    }

    /***
     * Sets the string of the time running on the stopwatch up to hundred of a second accuracy
     * @param time - in hundreds of a second since the stopwatch started
     */
    public static String getTimeText(long time) {
        if (time < 0) {
            time = 0;
        }
        long hundreds, seconds, minutes, hours;
        seconds = time / 1000;
        hundreds = (time - seconds * 1000) / 10;
        minutes = seconds / 60;
        seconds = seconds - minutes * 60;
        hours = minutes / 60;
        minutes = minutes - hours * 60;
        if (hours > 99) {
            hours = 0;
        }
        // TODO: must build to account for localization
        String timeStr;
        if (hours >= 10) {
            timeStr = String.format("%02dh %02dm %02ds .%02d", hours, minutes,
                    seconds, hundreds);
        } else if (hours > 0) {
            timeStr = String.format("%01dh %02dm %02ds .%02d", hours, minutes,
                    seconds, hundreds);
        } else if (minutes >= 10) {
            timeStr = String.format("%02dm %02ds .%02d", minutes, seconds,
                    hundreds);
        } else {
            timeStr = String.format("%02dm %02ds .%02d", minutes, seconds,
                    hundreds);
        }
        return timeStr;
    }

}
