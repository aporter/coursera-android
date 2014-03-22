/*
 * Copyright (C) 2010 The Android Open Source Project
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

import static android.provider.AlarmClock.ACTION_SET_ALARM;
import static android.provider.AlarmClock.EXTRA_HOUR;
import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static android.provider.AlarmClock.EXTRA_MINUTES;
import static android.provider.AlarmClock.EXTRA_SKIP_UI;

import java.util.Calendar;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

public class HandleSetAlarm extends Activity {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Intent intent = getIntent();
        if (intent == null || !ACTION_SET_ALARM.equals(intent.getAction())) {
            finish();
            return;
        } else if (!intent.hasExtra(EXTRA_HOUR)) {
            startActivity(new Intent(this, AlarmClock.class));
            finish();
            return;
        }

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        final int hour = intent.getIntExtra(EXTRA_HOUR,
                calendar.get(Calendar.HOUR_OF_DAY));
        final int minutes = intent.getIntExtra(EXTRA_MINUTES,
                calendar.get(Calendar.MINUTE));
        final boolean skipUi = intent.getBooleanExtra(EXTRA_SKIP_UI, false);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        if (message == null) {
            message = "";
        }

        Cursor c = null;
        long timeInMillis = Alarms.calculateAlarm(hour, minutes,
                new Alarm.DaysOfWeek(0)).getTimeInMillis();
        try {
            c = getContentResolver().query(
                    Alarm.Columns.CONTENT_URI,
                    Alarm.Columns.ALARM_QUERY_COLUMNS,
                    Alarm.Columns.HOUR + "=" + hour + " AND " +
                    Alarm.Columns.MINUTES + "=" + minutes + " AND " +
                    Alarm.Columns.DAYS_OF_WEEK + "=0 AND " +
                    Alarm.Columns.MESSAGE + "=?",
                    new String[] { message }, null);
            if (handleCursorResult(c, timeInMillis, true, skipUi)) {
                finish();
                return;
            }
        } finally {
            if (c != null) c.close();
            // Reset for use below.
            c = null;
        }

        ContentValues values = new ContentValues();
        values.put(Alarm.Columns.HOUR, hour);
        values.put(Alarm.Columns.MINUTES, minutes);
        values.put(Alarm.Columns.MESSAGE, message);
        values.put(Alarm.Columns.ENABLED, 1);
        values.put(Alarm.Columns.VIBRATE, 1);
        values.put(Alarm.Columns.DAYS_OF_WEEK, 0);
        values.put(Alarm.Columns.ALARM_TIME, timeInMillis);

        ContentResolver cr = getContentResolver();
        Uri result = cr.insert(Alarm.Columns.CONTENT_URI, values);
        if (result != null) {
            try {
                c = cr.query(result, Alarm.Columns.ALARM_QUERY_COLUMNS, null,
                        null, null);
                handleCursorResult(c, timeInMillis, false, skipUi);
            } finally {
                if (c != null) c.close();
            }
        }

        finish();
    }

    private boolean handleCursorResult(Cursor c, long timeInMillis,
            boolean enable, boolean skipUi) {
        if (c != null && c.moveToFirst()) {
            Alarm alarm = new Alarm(c);
            if (enable) {
                Alarms.enableAlarm(this, alarm.id, true);
                alarm.enabled = true;
            }
            AlarmUtils.popAlarmSetToast(this, timeInMillis);
            if (skipUi) {
                Alarms.setAlarm(this, alarm);
            } else {
                Intent i = new Intent(this, AlarmClock.class);
                i.putExtra(Alarms.ALARM_INTENT_EXTRA, alarm);
                startActivity(i);
            }
            return true;
        }
        return false;
    }
}
