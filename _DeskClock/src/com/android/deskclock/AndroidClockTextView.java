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

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Displays text using the special AndroidClock font.
 */
public class AndroidClockTextView extends TextView {

    private static final String SYSTEM = "/system/fonts/";
    private static final String SYSTEM_FONT_TIME_BACKGROUND = SYSTEM + "AndroidClock.ttf";

    private static final String ATTR_USE_CLOCK_TYPEFACE = "useClockTypeface";

    private static Typeface sClockTypeface;
    private static Typeface sStandardTypeface;

    private boolean mUseClockTypeface;

    public AndroidClockTextView(Context context) {
        super(context);
    }

    public AndroidClockTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mUseClockTypeface = attrs.getAttributeBooleanValue(null, ATTR_USE_CLOCK_TYPEFACE, true)
                && !isInEditMode();

        sStandardTypeface = Typeface.DEFAULT;
        if (sClockTypeface == null && mUseClockTypeface) {
            sClockTypeface = Typeface.createFromFile(SYSTEM_FONT_TIME_BACKGROUND);
        }

        Paint paint = getPaint();
        paint.setTypeface(mUseClockTypeface ? sClockTypeface : sStandardTypeface);
    }
}
