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
 * limitations under the License
 */

package com.android.deskclock.widget.swipeablelistview;

import android.view.View;

public class Utils {

    public static final String VIEW_DEBUGGING_TAG = "AlarmClock";

    public static void checkRequestLayout(View v) {
        boolean inLayout = false;
        final View root = v.getRootView();

        if (root == null || v.isLayoutRequested()) {
            return;
        }

        final Error e = new Error();
        for (StackTraceElement ste : e.getStackTrace()) {
            if ("android.view.ViewGroup".equals(ste.getClassName())
                    && "layout".equals(ste.getMethodName())) {
                inLayout = true;
                break;
            }
        }
        if (inLayout && !v.isLayoutRequested()) {
            LogUtils.i(VIEW_DEBUGGING_TAG,
                    e, "WARNING: in requestLayout during layout pass, view=%s", v);
        }
    }
}
