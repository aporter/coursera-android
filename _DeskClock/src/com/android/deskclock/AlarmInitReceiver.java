/*
 * Copyright (C) 2007 The Android Open Source Project
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;

import com.android.deskclock.timer.TimerObj;

public class AlarmInitReceiver extends BroadcastReceiver {

    /**
     * Sets alarm on ACTION_BOOT_COMPLETED.  Resets alarm on
     * TIME_SET, TIMEZONE_CHANGED
     */
    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (Log.LOGV) Log.v("AlarmInitReceiver " + action);

        final PendingResult result = goAsync();
        final WakeLock wl = AlarmAlertWakeLock.createPartialWakeLock(context);
        wl.acquire();
        AsyncHandler.post(new Runnable() {
            @Override public void run() {
                // Remove the snooze alarm after a boot.
                if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                    Alarms.saveSnoozeAlert(context, Alarms.INVALID_ALARM_ID, -1);
                    Alarms.disableExpiredAlarms(context);

                    // Clear stopwatch and timers data
                    SharedPreferences prefs =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    Log.v("AlarmInitReceiver - Cleaning old timer and stopwatch data");
                    TimerObj.cleanTimersFromSharedPrefs(prefs);
                    Utils.clearSwSharedPref(prefs);
                }
                Alarms.setNextAlert(context);
                result.finish();
                Log.v("AlarmInitReceiver finished");
                wl.release();
            }
        });
    }
}
