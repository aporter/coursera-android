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
import java.util.Iterator;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.deskclock.DeskClock;
import com.android.deskclock.R;
import com.android.deskclock.TimerRingService;
import com.android.deskclock.Utils;

public class TimerReceiver extends BroadcastReceiver {
    private static final String TAG = "TimerReceiver";

    // Make this a large number to avoid the alarm ID's which seem to be 1, 2, ...
    // Must also be different than StopwatchService.NOTIFICATION_ID
    private static final int IN_USE_NOTIFICATION_ID = Integer.MAX_VALUE - 2;

    ArrayList<TimerObj> mTimers;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int timer;
        String actionType = intent.getAction();

        // Get the updated timers data.
        if (mTimers == null) {
            mTimers = new ArrayList<TimerObj> ();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        TimerObj.getTimersFromSharedPrefs(prefs, mTimers);


        if (intent.hasExtra(Timers.TIMER_INTENT_EXTRA)) {
            // Get the alarm out of the Intent
            timer = intent.getIntExtra(Timers.TIMER_INTENT_EXTRA, -1);
            if (timer == -1) {
                Log.d(TAG, " got intent without Timer data: "+actionType);
            }
        } else if (Timers.NOTIF_IN_USE_SHOW.equals(actionType)){
            showInUseNotification(context);
            return;
        } else if (Timers.NOTIF_IN_USE_CANCEL.equals(actionType)) {
            cancelInUseNotification(context);
            return;
        } else {
            // No data to work with, do nothing
            Log.d(TAG, " got intent without Timer data");
            return;
        }

        TimerObj t = Timers.findTimer(mTimers, timer);

        if (intent.getBooleanExtra(Timers.UPDATE_NOTIFICATION, false)) {
            if (Timers.TIMER_STOP.equals(actionType)) {
                if (t == null) {
                    Log.d(TAG, "timer not found in list - can't stop it.");
                    return;
                }
                t.mState = TimerObj.STATE_DONE;
                t.writeToSharedPref(prefs);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(Timers.FROM_NOTIFICATION, true);
                editor.putLong(Timers.NOTIF_TIME, Utils.getTimeNow());
                editor.putInt(Timers.NOTIF_ID, timer);
                editor.apply();

                stopRingtoneIfNoTimesup(context);

                Intent activityIntent = new Intent(context, DeskClock.class);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityIntent.putExtra(DeskClock.SELECT_TAB_INTENT_EXTRA, DeskClock.TIMER_TAB_INDEX);
                context.startActivity(activityIntent);
            }
             return;
        }

        if (Timers.TIMES_UP.equals(actionType)) {
            // Find the timer (if it doesn't exists, it was probably deleted).
            if (t == null) {
                Log.d(TAG, " timer not found in list - do nothing");
                return;
            }

            t.mState = TimerObj.STATE_TIMESUP;
            t.writeToSharedPref(prefs);
            // Play ringtone by using TimerRingService service with a default alarm.
            Log.d(TAG, "playing ringtone");
            Intent si = new Intent();
            si.setClass(context, TimerRingService.class);
            context.startService(si);

            // Update the in-use notification
            if (getNextRunningTimer(mTimers, false, Utils.getTimeNow()) == null) {
                // Found no running timers.
                cancelInUseNotification(context);
            } else {
                showInUseNotification(context);
            }

            // Start the TimerAlertFullScreen activity.
            Intent timersAlert = new Intent(context, TimerAlertFullScreen.class);
            timersAlert.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
            context.startActivity(timersAlert);
        } else if (Timers.TIMER_RESET.equals(actionType)
                || Timers.DELETE_TIMER.equals(actionType)
                || Timers.TIMER_DONE.equals(actionType)) {
            // Stop Ringtone if all timers are not in times-up status
            stopRingtoneIfNoTimesup(context);
        }
        // Update the next "Times up" alarm
        updateNextTimesup(context);
    }

    private void stopRingtoneIfNoTimesup(final Context context) {
        if (Timers.findExpiredTimer(mTimers) == null) {
            // Stop ringtone
            Log.d(TAG, "stopping ringtone");
            Intent si = new Intent();
            si.setClass(context, TimerRingService.class);
            context.stopService(si);
        }
    }

    // Scan all timers and find the one that will expire next.
    // Tell AlarmManager to send a "Time's up" message to this receiver when this timer expires.
    // If no timer exists, clear "time's up" message.
    private void updateNextTimesup(Context context) {
        TimerObj t = getNextRunningTimer(mTimers, false, Utils.getTimeNow());
        long nextTimesup = (t == null) ? -1 : t.getTimesupTime();
        int timerId = (t == null) ? -1 : t.mTimerId;

        Intent intent = new Intent();
        intent.setAction(Timers.TIMES_UP);
        intent.setClass(context, TimerReceiver.class);
        if (!mTimers.isEmpty()) {
            intent.putExtra(Timers.TIMER_INTENT_EXTRA, timerId);
        }
        AlarmManager mngr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent p = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);
        if (t != null) {
            mngr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextTimesup, p);
            Log.d(TAG,"Setting times up to " + nextTimesup);
        } else {
            Log.d(TAG,"canceling times up");
            mngr.cancel(p);
        }
    }

    private void showInUseNotification(final Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean appOpen = prefs.getBoolean(Timers.NOTIF_APP_OPEN, false);
        ArrayList<TimerObj> timersInUse = Timers.timersInUse(mTimers);
        int numTimersInUse = timersInUse.size();

        if (appOpen || numTimersInUse == 0) {
            return;
        }

        String title, contentText;
        Long nextBroadcastTime = null;
        long now = Utils.getTimeNow();
        if (timersInUse.size() == 1) {
            TimerObj timer = timersInUse.get(0);
            String label = timer.mLabel.equals("") ?
                    context.getString(R.string.timer_notification_label) : timer.mLabel;
            title = timer.isTicking() ? label : context.getString(R.string.timer_stopped);
            long timeLeft = timer.isTicking() ? timer.getTimesupTime() - now : timer.mTimeLeft;
            contentText = buildTimeRemaining(context, timeLeft);
            if (timeLeft > 60) {
                nextBroadcastTime = getBroadcastTime(now, timeLeft);
            }
        } else {
            TimerObj timer = getNextRunningTimer(timersInUse, false, now);
            if (timer == null) {
                // No running timers.
                title = String.format(
                        context.getString(R.string.timers_stopped), numTimersInUse);
                contentText = context.getString(R.string.all_timers_stopped_notif);
            } else {
                // We have at least one timer running and other timers stopped.
                title = String.format(
                        context.getString(R.string.timers_in_use), numTimersInUse);
                long completionTime = timer.getTimesupTime();
                long timeLeft = completionTime - now;
                contentText = String.format(context.getString(R.string.next_timer_notif),
                        buildTimeRemaining(context, timeLeft));
                if (timeLeft <= 60) {
                    TimerObj timerWithUpdate = getNextRunningTimer(timersInUse, true, now);
                    if (timerWithUpdate != null) {
                        completionTime = timerWithUpdate.getTimesupTime();
                        timeLeft = completionTime - now;
                        nextBroadcastTime = getBroadcastTime(now, timeLeft);
                    }
                } else {
                    nextBroadcastTime = getBroadcastTime(now, timeLeft);
                }
            }
        }
        showCollapsedNotificationWithNext(context, title, contentText, nextBroadcastTime);
    }

    private long getBroadcastTime(long now, long timeUntilBroadcast) {
        long seconds = timeUntilBroadcast / 1000;
        seconds = seconds - ( (seconds / 60) * 60 );
        return now + (seconds * 1000);
    }

    /** Public and static to allow timer fragment to update notification with new label. **/
    public static void showExpiredAlarmNotification(Context context, TimerObj t) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Timers.TIMER_INTENT_EXTRA, t.mTimerId);
        broadcastIntent.setAction(Timers.TIMER_STOP);
        broadcastIntent.putExtra(Timers.UPDATE_NOTIFICATION, true);
        PendingIntent pendingBroadcastIntent = PendingIntent.getBroadcast(
                context, 0, broadcastIntent, 0);
        String label = t.mLabel.equals("") ? context.getString(R.string.timer_notification_label) :
            t.mLabel;
        String contentText = context.getString(R.string.timer_times_up);
        showCollapsedNotification(context, label, contentText, Notification.PRIORITY_MAX,
                pendingBroadcastIntent, t.mTimerId, true);
    }

    private void showCollapsedNotificationWithNext(
            final Context context, String title, String text, Long nextBroadcastTime) {
        Intent activityIntent = new Intent(context, DeskClock.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.putExtra(DeskClock.SELECT_TAB_INTENT_EXTRA, DeskClock.TIMER_TAB_INDEX);
        PendingIntent pendingActivityIntent = PendingIntent.getActivity(context, 0, activityIntent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);
        showCollapsedNotification(context, title, text, Notification.PRIORITY_HIGH,
                pendingActivityIntent, IN_USE_NOTIFICATION_ID, false);

        if (nextBroadcastTime == null) {
            return;
        }
        Intent nextBroadcast = new Intent();
        nextBroadcast.setAction(Timers.NOTIF_IN_USE_SHOW);
        PendingIntent pendingNextBroadcast =
                PendingIntent.getBroadcast(context, 0, nextBroadcast, 0);
        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, nextBroadcastTime, pendingNextBroadcast);
    }

    private static void showCollapsedNotification(final Context context, String title, String text,
            int priority, PendingIntent pendingIntent, int notificationId, boolean showTicker) {
        Notification.Builder builder = new Notification.Builder(context)
        .setAutoCancel(false)
        .setContentTitle(title)
        .setContentText(text)
        .setDeleteIntent(pendingIntent)
        .setOngoing(true)
        .setPriority(priority)
        .setShowWhen(false)
        .setSmallIcon(R.drawable.stat_notify_timer);
        if (showTicker) {
            builder.setTicker(text);
        }

        Notification notification = builder.build();
        notification.contentIntent = pendingIntent;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }

    private String buildTimeRemaining(Context context, long timeLeft) {
        if (timeLeft < 0) {
            // We should never be here...
            Log.v(TAG, "Will not show notification for timer already expired.");
            return null;
        }

        long hundreds, seconds, minutes, hours;
        seconds = timeLeft / 1000;
        minutes = seconds / 60;
        seconds = seconds - minutes * 60;
        hours = minutes / 60;
        minutes = minutes - hours * 60;
        if (hours > 99) {
            hours = 0;
        }

        String hourSeq = (hours == 0) ? "" :
            ( (hours == 1) ? context.getString(R.string.hour) :
                context.getString(R.string.hours, Long.toString(hours)) );
        String minSeq = (minutes == 0) ? "" :
            ( (minutes == 1) ? context.getString(R.string.minute) :
                context.getString(R.string.minutes, Long.toString(minutes)) );

        boolean dispHour = hours > 0;
        boolean dispMinute = minutes > 0;
        int index = (dispHour ? 1 : 0) | (dispMinute ? 2 : 0);
        String[] formats = context.getResources().getStringArray(R.array.timer_notifications);
        return String.format(formats[index], hourSeq, minSeq);
    }

    private TimerObj getNextRunningTimer(
            ArrayList<TimerObj> timers, boolean requireNextUpdate, long now) {
        long nextTimesup = Long.MAX_VALUE;
        boolean nextTimerFound = false;
        Iterator<TimerObj> i = timers.iterator();
        TimerObj t = null;
        while(i.hasNext()) {
            TimerObj tmp = i.next();
            if (tmp.mState == TimerObj.STATE_RUNNING) {
                long timesupTime = tmp.getTimesupTime();
                long timeLeft = timesupTime - now;
                if (timesupTime < nextTimesup && (!requireNextUpdate || timeLeft > 60) ) {
                    nextTimesup = timesupTime;
                    nextTimerFound = true;
                    t = tmp;
                }
            }
        }
        if (nextTimerFound) {
            return t;
        } else {
            return null;
        }
    }

    private void cancelInUseNotification(final Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(IN_USE_NOTIFICATION_ID);
    }
}
