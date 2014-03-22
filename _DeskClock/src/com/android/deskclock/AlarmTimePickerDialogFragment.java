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

package com.android.deskclock;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Dialog to set alarm time.
 */
public class AlarmTimePickerDialogFragment extends DialogFragment {

    private static final String KEY_ALARM = "alarm";

    private Button mSet, mCancel;
    private TimePicker mPicker;

    public static AlarmTimePickerDialogFragment newInstance(Alarm alarm) {
        final AlarmTimePickerDialogFragment frag = new AlarmTimePickerDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_ALARM, alarm);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final Alarm alarm = getArguments().getParcelable(KEY_ALARM);

        View v = inflater.inflate(R.layout.time_picker_dialog, null);
        mSet = (Button) v.findViewById(R.id.set_button);
        mCancel = (Button) v.findViewById(R.id.cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mPicker = (TimePicker) v.findViewById(R.id.time_picker);
        mPicker.setSetButton(mSet);
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity activity = getActivity();
                if (activity instanceof AlarmTimePickerDialogHandler) {
                    final AlarmTimePickerDialogHandler act =
                            (AlarmTimePickerDialogHandler) activity;
                    act.onDialogTimeSet(alarm, mPicker.getHours(), mPicker.getMinutes());
                } else {
                    Log.e("Error! Activities that use AlarmTimePickerDialogFragment must implement "
                            + "AlarmTimePickerDialogHandler");
                }
                dismiss();
            }
        });

        return v;
    }

    interface AlarmTimePickerDialogHandler {
        void onDialogTimeSet(Alarm alarm, int hourOfDay, int minute);
    }
}
