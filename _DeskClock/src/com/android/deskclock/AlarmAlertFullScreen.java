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

import java.util.Calendar;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.deskclock.widget.multiwaveview.GlowPadView;

/**
 * Alarm Clock alarm alert: pops visible indicator and plays alarm
 * tone. This activity is the full screen version which shows over the lock
 * screen with the wallpaper as the background.
 */
public class AlarmAlertFullScreen extends Activity implements GlowPadView.OnTriggerListener {

    private final boolean LOG = true;
    // These defaults must match the values in res/xml/settings.xml
    private static final String DEFAULT_SNOOZE = "10";
    private static final String DEFAULT_VOLUME_BEHAVIOR = "2";
    protected static final String SCREEN_OFF = "screen_off";

    protected Alarm mAlarm;
    private int mVolumeBehavior;
    boolean mFullscreenStyle;
    private GlowPadView mGlowPadView;
    private boolean mIsDocked = false;

    // Parameters for the GlowPadView "ping" animation; see triggerPing().
    private static final int PING_MESSAGE_WHAT = 101;
    private static final boolean ENABLE_PING_AUTO_REPEAT = true;
    private static final long PING_AUTO_REPEAT_DELAY_MSEC = 1200;

    private boolean mPingEnabled = true;

    // Receives the ALARM_KILLED action from the AlarmKlaxon,
    // and also ALARM_SNOOZE_ACTION / ALARM_DISMISS_ACTION from other applications
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (LOG) {
                Log.v("AlarmAlertFullScreen - onReceive " + action);
            }
            if (action.equals(Alarms.ALARM_SNOOZE_ACTION)) {
                snooze();
            } else if (action.equals(Alarms.ALARM_DISMISS_ACTION)) {
                dismiss(false);
            } else {
                Alarm alarm = intent.getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);
                if (alarm != null && mAlarm.id == alarm.id) {
                    dismiss(true);
                }
            }
        }
    };

    private final Handler mPingHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PING_MESSAGE_WHAT:
                    triggerPing();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        mAlarm = getIntent().getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);

        if (LOG) {
            Log.v("AlarmAlertFullScreen - onCreate");
            if (mAlarm != null) {
                Log.v("AlarmAlertFullScreen - Alarm Id " + mAlarm.toString());
            }
        }

        // Get the volume/camera button behavior setting
        final String vol =
                PreferenceManager.getDefaultSharedPreferences(this)
                .getString(SettingsActivity.KEY_VOLUME_BEHAVIOR,
                        DEFAULT_VOLUME_BEHAVIOR);
        mVolumeBehavior = Integer.parseInt(vol);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        // Turn on the screen unless we are being launched from the AlarmAlert
        // subclass as a result of the screen turning off.
        if (!getIntent().getBooleanExtra(SCREEN_OFF, false)) {
            win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        }

        updateLayout();

        // Check the docking status , if the device is docked , do not limit rotation
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_DOCK_EVENT);
        Intent dockStatus = registerReceiver(null, ifilter);
        if (dockStatus != null) {
            mIsDocked = dockStatus.getIntExtra(Intent.EXTRA_DOCK_STATE, -1)
                    != Intent.EXTRA_DOCK_STATE_UNDOCKED;
        }

        // Register to get the alarm killed/snooze/dismiss intent.
        IntentFilter filter = new IntentFilter(Alarms.ALARM_KILLED);
        filter.addAction(Alarms.ALARM_SNOOZE_ACTION);
        filter.addAction(Alarms.ALARM_DISMISS_ACTION);
        registerReceiver(mReceiver, filter);
    }

    private void setTitle() {
        final String titleText = mAlarm.getLabelOrDefault(this);

        TextView tv = (TextView) findViewById(R.id.alertTitle);
        tv.setText(titleText);

        setTitle(titleText);
    }

    protected int getLayoutResId() {
        return R.layout.alarm_alert;
    }

    private void updateLayout() {
        if (LOG) {
            Log.v("AlarmAlertFullScreen - updateLayout");
        }

        final LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(getLayoutResId(), null);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        setContentView(view);

        /* Set the title from the passed in alarm */
        setTitle();

        mGlowPadView = (GlowPadView) findViewById(R.id.glow_pad_view);
        mGlowPadView.setOnTriggerListener(this);
        triggerPing();
    }

    private void triggerPing() {
        if (mPingEnabled) {
            mGlowPadView.ping();

            if (ENABLE_PING_AUTO_REPEAT) {
                mPingHandler.sendEmptyMessageDelayed(PING_MESSAGE_WHAT, PING_AUTO_REPEAT_DELAY_MSEC);
            }
        }
    }

    // Attempt to snooze this alert.
    private void snooze() {
        if (LOG) {
            Log.v("AlarmAlertFullScreen - snooze");
        }

        final String snooze =
                PreferenceManager.getDefaultSharedPreferences(this)
                .getString(SettingsActivity.KEY_ALARM_SNOOZE, DEFAULT_SNOOZE);
        int snoozeMinutes = Integer.parseInt(snooze);

        final long snoozeTime = System.currentTimeMillis()
                + (1000 * 60 * snoozeMinutes);
        Alarms.saveSnoozeAlert(AlarmAlertFullScreen.this, mAlarm.id,
                snoozeTime);

        // Get the display time for the snooze and update the notification.
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(snoozeTime);
        String snoozeTimeStr = Alarms.formatTime(this, c);
        String label = mAlarm.getLabelOrDefault(this);

        // Notify the user that the alarm has been snoozed.
        Intent dismissIntent = new Intent(this, AlarmReceiver.class);
        dismissIntent.setAction(Alarms.CANCEL_SNOOZE);
        dismissIntent.putExtra(Alarms.ALARM_INTENT_EXTRA, mAlarm);

        Intent openAlarm = new Intent(this, DeskClock.class);
        openAlarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        openAlarm.putExtra(Alarms.ALARM_INTENT_EXTRA, mAlarm);
        openAlarm.putExtra(DeskClock.SELECT_TAB_INTENT_EXTRA, DeskClock.CLOCK_TAB_INDEX);

        NotificationManager nm = getNotificationManager();
        Notification notif = new Notification.Builder(getApplicationContext())
        .setContentTitle(label)
        .setContentText(getResources().getString(R.string.alarm_alert_snooze_until, snoozeTimeStr))
        .setSmallIcon(R.drawable.stat_notify_alarm)
        .setOngoing(true)
        .setAutoCancel(false)
        .setPriority(Notification.PRIORITY_MAX)
        .setWhen(0)
        .addAction(android.R.drawable.ic_menu_close_clear_cancel,
                getResources().getString(R.string.alarm_alert_dismiss_text),
                PendingIntent.getBroadcast(this, mAlarm.id, dismissIntent, 0))
        .build();
        notif.contentIntent = PendingIntent.getActivity(this, mAlarm.id, openAlarm, 0);
        nm.notify(mAlarm.id, notif);

        String displayTime = getString(R.string.alarm_alert_snooze_set,
                snoozeMinutes);
        // Intentionally log the snooze time for debugging.
        Log.v(displayTime);

        // Display the snooze minutes in a toast.
        Toast.makeText(AlarmAlertFullScreen.this, displayTime,
                Toast.LENGTH_LONG).show();
        stopService(new Intent(Alarms.ALARM_ALERT_ACTION));
        finish();
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    // Dismiss the alarm.
    private void dismiss(boolean killed) {
        if (LOG) {
            Log.v("AlarmAlertFullScreen - dismiss");
        }

        Log.i(killed ? "Alarm killed" : "Alarm dismissed by user");
        // The service told us that the alarm has been killed, do not modify
        // the notification or stop the service.
        if (!killed) {
            // Cancel the notification and stop playing the alarm
            NotificationManager nm = getNotificationManager();
            nm.cancel(mAlarm.id);
            stopService(new Intent(Alarms.ALARM_ALERT_ACTION));
        }
        finish();
    }

    /**
     * this is called when a second alarm is triggered while a
     * previous alert window is still active.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (LOG) Log.v("AlarmAlert.OnNewIntent()");

        mAlarm = intent.getParcelableExtra(Alarms.ALARM_INTENT_EXTRA);

        setTitle();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (LOG) {
            Log.v("AlarmAlertFullScreen - onConfigChanged");
        }
        updateLayout();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LOG) {
            Log.v("AlarmAlertFullScreen - onResume");
        }
        // If the alarm was deleted at some point, disable snooze.
        if (Alarms.getAlarm(getContentResolver(), mAlarm.id) == null) {
            mGlowPadView.setTargetResources(R.array.dismiss_drawables);
            mGlowPadView.setTargetDescriptionsResourceId(R.array.dismiss_descriptions);
            mGlowPadView.setDirectionDescriptionsResourceId(R.array.dismiss_direction_descriptions);
        }
        // The activity is locked to the default orientation as a default set in the manifest
        // Override this settings if the device is docked or config set it differently
        if (getResources().getBoolean(R.bool.config_rotateAlarmAlert) || mIsDocked) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LOG) Log.v("AlarmAlertFullScreen.onDestroy()");
        // No longer care about the alarm being killed.
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Do this on key down to handle a few of the system keys.
        boolean up = event.getAction() == KeyEvent.ACTION_UP;
        if (LOG) {
            Log.v("AlarmAlertFullScreen - dispatchKeyEvent " + event.getKeyCode());
        }
        switch (event.getKeyCode()) {
            // Volume keys and camera keys dismiss the alarm
            case KeyEvent.KEYCODE_POWER:
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            case KeyEvent.KEYCODE_VOLUME_MUTE:
            case KeyEvent.KEYCODE_CAMERA:
            case KeyEvent.KEYCODE_FOCUS:
                if (up) {
                    switch (mVolumeBehavior) {
                        case 1:
                            snooze();
                            break;

                        case 2:
                            dismiss(false);
                            break;

                        default:
                            break;
                    }
                }
                return true;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        // Don't allow back to dismiss. This method is overriden by AlarmAlert
        // so that the dialog is dismissed.
        if (LOG) {
            Log.v("AlarmAlertFullScreen - onBackPressed");
        }
        return;
    }


    @Override
    public void onGrabbed(View v, int handle) {
        mPingEnabled = false;
    }

    @Override
    public void onReleased(View v, int handle) {
        mPingEnabled = true;
        triggerPing();
    }

    @Override
    public void onTrigger(View v, int target) {
        final int resId = mGlowPadView.getResourceIdForTarget(target);
        switch (resId) {
            case R.drawable.ic_alarm_alert_snooze:
                snooze();
                break;

            case R.drawable.ic_alarm_alert_dismiss:
                dismiss(false);
                break;
            default:
                // Code should never reach here.
                Log.e("Trigger detected on unhandled resource. Skipping.");
        }
    }

    @Override
    public void onGrabbedStateChange(View v, int handle) {
    }

    @Override
    public void onFinishFinalAnimation() {
    }
}
