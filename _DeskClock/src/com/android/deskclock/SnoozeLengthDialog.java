package com.android.deskclock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * A dialog preference that shows a number picker for selecting snooze length
 */
public class SnoozeLengthDialog extends DialogPreference {
    private NumberPicker mNumberPickerView;
    private TextView mNumberPickerMinutesView;
    private final Context mContext;
    private int mSnoozeMinutes;

    private static final String DEFAULT_SNOOZE_TIME = "10";


    public SnoozeLengthDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setDialogLayoutResource(R.layout.snooze_length_picker);
        setTitle(R.string.snooze_duration_title);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setTitle(getContext().getString(R.string.snooze_duration_title))
                .setCancelable(true);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mNumberPickerMinutesView = (TextView) view.findViewById(R.id.title);
        mNumberPickerView = (NumberPicker) view.findViewById(R.id.minutes_picker);
        mNumberPickerView.setMinValue(1);
        mNumberPickerView.setMaxValue(30);
        mNumberPickerView.setValue(mSnoozeMinutes);
        updateDays();
        mNumberPickerView.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateDays();
            }
        });
    }

    @Override
    protected void onSetInitialValue (boolean restorePersistedValue, Object defaultValue) {
        String val;
        if (restorePersistedValue) {
            val = getPersistedString(DEFAULT_SNOOZE_TIME);
            if (val != null) {
                mSnoozeMinutes = Integer.parseInt(val);
            }
        } else {
            val = (String) defaultValue;
            if (val != null) {
                mSnoozeMinutes = Integer.parseInt(val);
            }
            persistString(val);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    private void updateDays() {
        mNumberPickerMinutesView.setText(String.format(mContext.getResources()
                .getQuantityText(R.plurals.snooze_picker_label, mNumberPickerView.getValue())
                .toString()));
    }
    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            mNumberPickerView.clearFocus();
            mSnoozeMinutes = mNumberPickerView.getValue();
            persistString(Integer.toString(mSnoozeMinutes));
            setSummary();
        }
    }

    public void setSummary() {
        setSummary(String.format(mContext.getResources()
                .getQuantityText(R.plurals.snooze_duration, mSnoozeMinutes).toString(),
                mSnoozeMinutes));
    }

    public int getCurrentValue() {
        return mSnoozeMinutes;
    }
}



