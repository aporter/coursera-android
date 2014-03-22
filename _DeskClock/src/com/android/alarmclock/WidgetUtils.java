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

package com.android.alarmclock;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.RemoteViews;

import com.android.deskclock.R;

public class WidgetUtils {
    static final String TAG = "WidgetUtils";

    public static void setClockSize(Context context, RemoteViews clock, float scale) {
        float fontSize = context.getResources().getDimension(R.dimen.widget_big_font_size);
        clock.setTextViewTextSize(
                R.id.the_clock1, TypedValue.COMPLEX_UNIT_PX, fontSize * scale);
        clock.setTextViewTextSize(
                R.id.the_clock2, TypedValue.COMPLEX_UNIT_PX, fontSize * scale);
    }

    // Calculate the scale factor of the fonts in the widget
    public static float getScaleRatio(Context context, Bundle options, int id) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        if (options == null) {
            options = widgetManager.getAppWidgetOptions(id);
        }
        if (options != null) {
            int minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            if (minWidth == 0) {
                // No data , do no scaling
                return 1f;
            }
            Resources res = context.getResources();
            float ratio = minWidth / res.getDimension(R.dimen.def_digital_widget_width);
            return (ratio > 1) ? 1 : ratio;
        }
        return 1;
    }

    // Decide if to show the list of world clock.
    // Check to see if the widget size is big enough, if it is return true.
    public static boolean showList(Context context, int id, float scale) {
        Bundle options = AppWidgetManager.getInstance(context).getAppWidgetOptions(id);
        if (options == null) {
            // no data to make the calculation, show the list anyway
            return true;
        }
        int minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        int neededSize = (int) context.getResources().
            getDimension(R.dimen.def_digital_widget_height);
        return (minHeight > neededSize);
    }
}

