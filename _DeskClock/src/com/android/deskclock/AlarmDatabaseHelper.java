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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Helper class for opening the database from multiple providers.  Also provides
 * some common functionality.
 */
class AlarmDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarms.db";
    private static final int DATABASE_VERSION = 5;

    public AlarmDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE alarms (" +
                   "_id INTEGER PRIMARY KEY," +
                   "hour INTEGER, " +
                   "minutes INTEGER, " +
                   "daysofweek INTEGER, " +
                   "alarmtime INTEGER, " +
                   "enabled INTEGER, " +
                   "vibrate INTEGER, " +
                   "message TEXT, " +
                   "alert TEXT);");

        // insert default alarms
        String insertMe = "INSERT INTO alarms " +
                "(hour, minutes, daysofweek, alarmtime, enabled, vibrate, " +
                " message, alert) VALUES ";
        db.execSQL(insertMe + "(8, 30, 31, 0, 0, 1, '', '');");
        db.execSQL(insertMe + "(9, 00, 96, 0, 0, 1, '', '');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
            int currentVersion) {
        if (Log.LOGV) Log.v(
                "Upgrading alarms database from version " +
                oldVersion + " to " + currentVersion +
                ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS alarms");
        onCreate(db);
    }

    Uri commonInsert(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long rowId = -1;
        try {
            // Check if we are trying to re-use an existing id.
            Object value = values.get(Alarm.Columns._ID);
            if (value != null) {
                int id = (Integer) value;
                if (id > -1) {
                    final Cursor cursor = db
                            .query("alarms", new String[]{Alarm.Columns._ID}, "_id = ?",
                                    new String[]{id + ""}, null, null, null);
                    if (cursor.moveToFirst()) {
                        // Record exists. Remove the id so sqlite can generate a new one.
                        values.putNull(Alarm.Columns._ID);
                    }
                }
            }

            rowId = db.insert("alarms", Alarm.Columns.MESSAGE, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        if (rowId < 0) {
            throw new SQLException("Failed to insert row");
        }
        if (Log.LOGV) Log.v("Added alarm rowId = " + rowId);

        return ContentUris.withAppendedId(Alarm.Columns.CONTENT_URI, rowId);
    }
}
