/*
 * Copyright (C) 2008 The Android Open Source Project
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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;

import com.android.deskclock.R;
import com.android.deskclock.Utils;


public class CountingTimerView extends View {
    private static final String TWO_DIGITS = "%02d";
    private static final String ONE_DIGIT = "%01d";
    private static final String NEG_TWO_DIGITS = "-%02d";
    private static final String NEG_ONE_DIGIT = "-%01d";
    private static final float TEXT_SIZE_TO_WIDTH_RATIO = 0.75f;
    // This is the ratio of the font typeface we need to offset the font by vertically to align it
    // vertically center.
    private static final float FONT_VERTICAL_OFFSET = 0.14f;

    private String mHours, mMinutes, mSeconds, mHunderdths;
    private final String mHoursLabel, mMinutesLabel, mSecondsLabel;
    private float mHoursWidth, mMinutesWidth, mSecondsWidth, mHundredthsWidth;
    private float mHoursLabelWidth, mMinutesLabelWidth, mSecondsLabelWidth, mHundredthsSepWidth;

    private boolean mShowTimeStr = true;
    private final Typeface mAndroidClockMonoThin, mAndroidClockMonoBold, mRobotoLabel, mAndroidClockMonoLight;
    private final Paint mPaintBig = new Paint();
    private final Paint mPaintBigThin = new Paint();
    private final Paint mPaintMed = new Paint();
    private final Paint mPaintLabel = new Paint();
    private float mTextHeight = 0;
    private float mTotalTextWidth;
    private static final String HUNDREDTH_SEPERATOR = ".";
    private boolean mRemeasureText = true;

    private int mDefaultColor;
    private final int mPressedColor;
    private final int mWhiteColor;
    private final int mRedColor;
    private TextView mStopStartTextView;
    private final AccessibilityManager mAccessibilityManager;

    // Fields for the text serving as a virtual button.
    private boolean mVirtualButtonEnabled = false;
    private boolean mVirtualButtonPressedOn = false;

    Runnable mBlinkThread = new Runnable() {
        private boolean mVisible = true;
        @Override
        public void run() {
            mVisible = !mVisible;
            CountingTimerView.this.showTime(mVisible);
            postDelayed(mBlinkThread, 500);
        }

    };


    public CountingTimerView(Context context) {
        this(context, null);
    }

    public CountingTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAndroidClockMonoThin = Typeface.createFromAsset(context.getAssets(),"fonts/AndroidClockMono-Thin.ttf");
        mAndroidClockMonoBold = Typeface.createFromAsset(context.getAssets(),"fonts/AndroidClockMono-Bold.ttf");
        mAndroidClockMonoLight = Typeface.createFromAsset(context.getAssets(),"fonts/AndroidClockMono-Light.ttf");
        mAccessibilityManager =
                (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        mRobotoLabel= Typeface.create("sans-serif-condensed", Typeface.BOLD);
        Resources r = context.getResources();
        mHoursLabel = r.getString(R.string.hours_label).toUpperCase();
        mMinutesLabel = r.getString(R.string.minutes_label).toUpperCase();
        mSecondsLabel = r.getString(R.string.seconds_label).toUpperCase();
        mWhiteColor = r.getColor(R.color.clock_white);
        mDefaultColor = mWhiteColor;
        mPressedColor = r.getColor(Utils.getPressedColorId());
        mRedColor = r.getColor(R.color.clock_red);

        mPaintBig.setAntiAlias(true);
        mPaintBig.setStyle(Paint.Style.STROKE);
        mPaintBig.setTextAlign(Paint.Align.LEFT);
        mPaintBig.setTypeface(mAndroidClockMonoBold);
        float bigFontSize = r.getDimension(R.dimen.big_font_size);
        mPaintBig.setTextSize(bigFontSize);
        mTextHeight = bigFontSize;

        mPaintBigThin.setAntiAlias(true);
        mPaintBigThin.setStyle(Paint.Style.STROKE);
        mPaintBigThin.setTextAlign(Paint.Align.LEFT);
        mPaintBigThin.setTypeface(mAndroidClockMonoThin);
        mPaintBigThin.setTextSize(r.getDimension(R.dimen.big_font_size));

        mPaintMed.setAntiAlias(true);
        mPaintMed.setStyle(Paint.Style.STROKE);
        mPaintMed.setTextAlign(Paint.Align.LEFT);
        mPaintMed.setTypeface(mAndroidClockMonoLight);
        mPaintMed.setTextSize(r.getDimension(R.dimen.small_font_size));

        mPaintLabel.setAntiAlias(true);
        mPaintLabel.setStyle(Paint.Style.STROKE);
        mPaintLabel.setTextAlign(Paint.Align.LEFT);
        mPaintLabel.setTypeface(mRobotoLabel);
        mPaintLabel.setTextSize(r.getDimension(R.dimen.label_font_size));

        setTextColor(mDefaultColor);
    }

    protected void setTextColor(int textColor) {
        mPaintBig.setColor(textColor);
        mPaintBigThin.setColor(textColor);
        mPaintMed.setColor(textColor);
        mPaintLabel.setColor(textColor);
    }

    public void setTime(long time, boolean showHundredths, boolean update) {
        boolean neg = false, showNeg = false;
        String format = null;
        if (time < 0) {
            time = -time;
            neg = showNeg = true;
        }
        long hundreds, seconds, minutes, hours;
        seconds = time / 1000;
        hundreds = (time - seconds * 1000) / 10;
        minutes = seconds / 60;
        seconds = seconds - minutes * 60;
        hours = minutes / 60;
        minutes = minutes - hours * 60;
        if (hours > 99) {
            hours = 0;
        }
        // time may less than a second below zero, since we do not show fractions of seconds
        // when counting down, do not show the minus sign.
        if (hours ==0 && minutes == 0 && seconds == 0) {
            showNeg = false;
        }
        // TODO: must build to account for localization
        if (!showHundredths) {
            if (!neg && hundreds != 0) {
                seconds++;
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                    if (minutes == 60) {
                        minutes = 0;
                        hours++;
                    }
                }
            }
            if (hundreds < 10 || hundreds > 90) {
                update = true;
            }
        }

        if (hours >= 10) {
            format = showNeg ? NEG_TWO_DIGITS : TWO_DIGITS;
            mHours = String.format(format, hours);
        } else if (hours > 0) {
            format = showNeg ? NEG_ONE_DIGIT : ONE_DIGIT;
            mHours = String.format(format, hours);
        } else {
            mHours = null;
        }

        if (minutes >= 10 || hours > 0) {
            format = (showNeg && hours == 0) ? NEG_TWO_DIGITS : TWO_DIGITS;
            mMinutes = String.format(format, minutes);
        } else {
            format = (showNeg && hours == 0) ? NEG_ONE_DIGIT : ONE_DIGIT;
            mMinutes = String.format(format, minutes);
        }

        mSeconds = String.format(TWO_DIGITS, seconds);
        if (showHundredths) {
            mHunderdths = String.format(TWO_DIGITS, hundreds);
        } else {
            mHunderdths = null;
        }
        mRemeasureText = true;

        if (update) {
            setContentDescription(getTimeStringForAccessibility((int) hours, (int) minutes,
                    (int) seconds, showNeg, getResources()));
            invalidate();
        }
    }
    private void setTotalTextWidth() {
        mTotalTextWidth = 0;
        if (mHours != null) {
            mHoursWidth = mPaintBig.measureText(mHours);
            mTotalTextWidth += mHoursWidth;
            mHoursLabelWidth = mPaintLabel.measureText(mHoursLabel);
            mTotalTextWidth += mHoursLabelWidth;
        }
        if (mMinutes != null) {
            mMinutesWidth =  mPaintBig.measureText(mMinutes);
            mTotalTextWidth += mMinutesWidth;
            mMinutesLabelWidth = mPaintLabel.measureText(mMinutesLabel);
            mTotalTextWidth += mMinutesLabelWidth;
        }
        if (mSeconds != null) {
            mSecondsWidth = mPaintBigThin.measureText(mSeconds);
            mTotalTextWidth += mSecondsWidth;
            mSecondsLabelWidth = mPaintLabel.measureText(mSecondsLabel);
            mTotalTextWidth += mSecondsLabelWidth;
        }
        if (mHunderdths != null) {
            mHundredthsWidth = mPaintMed.measureText(mHunderdths);
            mTotalTextWidth += mHundredthsWidth;
            mHundredthsSepWidth = mPaintLabel.measureText(HUNDREDTH_SEPERATOR);
            mTotalTextWidth += mHundredthsSepWidth;
        }

        // This is a hack: if the text is too wide, reduce all the paint text sizes
        // To determine the maximum width, we find the minimum of the height and width (since the
        // circle we are trying to fit the text into has its radius sized to the smaller of the
        // two.
        int width = Math.min(getWidth(), getHeight());
        if (width != 0) {
            float ratio = mTotalTextWidth / width;
            if (ratio > TEXT_SIZE_TO_WIDTH_RATIO) {
                float sizeRatio = (TEXT_SIZE_TO_WIDTH_RATIO / ratio);
                mPaintBig.setTextSize( mPaintBig.getTextSize() * sizeRatio);
                mPaintBigThin.setTextSize( mPaintBigThin.getTextSize() * sizeRatio);
                mPaintMed.setTextSize( mPaintMed.getTextSize() * sizeRatio);
                mTotalTextWidth *= sizeRatio;
                mMinutesWidth *= sizeRatio;
                mHoursWidth *= sizeRatio;
                mSecondsWidth *= sizeRatio;
                mHundredthsWidth *= sizeRatio;
                mHundredthsSepWidth *= sizeRatio;
                //recalculate the new total text width and half text height
                mTotalTextWidth = mHoursWidth + mMinutesWidth + mSecondsWidth +
                        mHundredthsWidth + mHundredthsSepWidth + mHoursLabelWidth +
                        mMinutesLabelWidth + mSecondsLabelWidth;
                mTextHeight = mPaintBig.getTextSize();
            }
        }
    }

    public void blinkTimeStr(boolean blink) {
        if (blink) {
            removeCallbacks(mBlinkThread);
            postDelayed(mBlinkThread, 1000);
        } else {
            removeCallbacks(mBlinkThread);
            showTime(true);
        }
    }

    public void showTime(boolean visible) {
        mShowTimeStr = visible;
        invalidate();
        mRemeasureText = true;
    }

    public void redTimeStr(boolean red, boolean forceUpdate) {
        mDefaultColor = red ? mRedColor : mWhiteColor;
        setTextColor(mDefaultColor);
        if (forceUpdate) {
            invalidate();
        }
    }

    public String getTimeString() {
        if (mHours == null) {
            return String.format("%s:%s.%s",mMinutes, mSeconds,  mHunderdths);
        }
        return String.format("%s:%s:%s.%s",mHours, mMinutes, mSeconds,  mHunderdths);
    }

    private static String getTimeStringForAccessibility(int hours, int minutes, int seconds,
            boolean showNeg, Resources r) {
        StringBuilder s = new StringBuilder();
        if (showNeg) {
            // This must be followed by a non-zero number or it will be audible as "hyphen"
            // instead of "minus".
            s.append("-");
        }
        if (showNeg && hours == 0 && minutes == 0) {
            // Non-negative time will always have minutes, eg. "0 minutes 7 seconds", but negative
            // time must start with non-zero digit, eg. -0m7s will be audible as just "-7 seconds"
            s.append(String.format(
                    r.getQuantityText(R.plurals.Nseconds_description, seconds).toString(),
                    seconds));
        } else if (hours == 0) {
            s.append(String.format(
                    r.getQuantityText(R.plurals.Nminutes_description, minutes).toString(),
                    minutes));
            s.append(" ");
            s.append(String.format(
                    r.getQuantityText(R.plurals.Nseconds_description, seconds).toString(),
                    seconds));
        } else {
            s.append(String.format(
                    r.getQuantityText(R.plurals.Nhours_description, hours).toString(),
                    hours));
            s.append(" ");
            s.append(String.format(
                    r.getQuantityText(R.plurals.Nminutes_description, minutes).toString(),
                    minutes));
            s.append(" ");
            s.append(String.format(
                    r.getQuantityText(R.plurals.Nseconds_description, seconds).toString(),
                    seconds));
        }
        return s.toString();
    }

    public void setVirtualButtonEnabled(boolean enabled) {
        mVirtualButtonEnabled = enabled;
    }

    private void virtualButtonPressed(boolean pressedOn) {
        mVirtualButtonPressedOn = pressedOn;
        mStopStartTextView.setTextColor(pressedOn ? mPressedColor : mWhiteColor);
        invalidate();
    }

    private boolean withinVirtualButtonBounds(float x, float y) {
        int width = getWidth();
        int height = getHeight();
        float centerX = width / 2;
        float centerY = height / 2;
        float radius = Math.min(width, height) / 2;

        // Within the circle button if distance to the center is less than the radius.
        double distance = Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2));
        return distance < radius;
    }

    public void registerVirtualButtonAction(final Runnable runnable) {
        if (!mAccessibilityManager.isEnabled()) {
            this.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mVirtualButtonEnabled) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                if (withinVirtualButtonBounds(event.getX(), event.getY())) {
                                    virtualButtonPressed(true);
                                    return true;
                                } else {
                                    virtualButtonPressed(false);
                                    return false;
                                }
                            case MotionEvent.ACTION_CANCEL:
                                virtualButtonPressed(false);
                                return true;
                            case MotionEvent.ACTION_OUTSIDE:
                                virtualButtonPressed(false);
                                return false;
                            case MotionEvent.ACTION_UP:
                                virtualButtonPressed(false);
                                if (withinVirtualButtonBounds(event.getX(), event.getY())) {
                                    runnable.run();
                                }
                                return true;
                        }
                    }
                    return false;
                }
            });
        } else {
            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    runnable.run();
                }
            });
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Blink functionality.
        if (!mShowTimeStr && !mVirtualButtonPressedOn) {
            return;
        }

        int width = getWidth();
        if (mRemeasureText && width != 0) {
            setTotalTextWidth();
            width = getWidth();
            mRemeasureText = false;
        }

        int xCenter = width / 2;
        int yCenter = getHeight() / 2;

        float textXstart = xCenter - mTotalTextWidth / 2;
        float textYstart = yCenter + mTextHeight/2 - (mTextHeight * FONT_VERTICAL_OFFSET);
        // align the labels vertically to the top of the rest of the text
        float labelYStart = textYstart - (mTextHeight * (1 - 2 * FONT_VERTICAL_OFFSET))
                + (1 - 2 * FONT_VERTICAL_OFFSET) * mPaintLabel.getTextSize();

        // Text color differs based on pressed state.
        int textColor;
        if (mVirtualButtonPressedOn) {
            textColor = mPressedColor;
            mStopStartTextView.setTextColor(mPressedColor);
        } else {
            textColor = mDefaultColor;
        }
        mPaintBig.setColor(textColor);
        mPaintBigThin.setColor(textColor);
        mPaintLabel.setColor(textColor);
        mPaintMed.setColor(textColor);

        if (mHours != null) {
            canvas.drawText(mHours, textXstart, textYstart, mPaintBig);
            textXstart += mHoursWidth;
            canvas.drawText(mHoursLabel, textXstart, labelYStart, mPaintLabel);
            textXstart += mHoursLabelWidth;
        }
        if (mMinutes != null) {
            canvas.drawText(mMinutes, textXstart, textYstart, mPaintBig);
            textXstart += mMinutesWidth;
            canvas.drawText(mMinutesLabel, textXstart, labelYStart, mPaintLabel);
            textXstart += mMinutesLabelWidth;
        }
        if (mSeconds != null) {
            canvas.drawText(mSeconds, textXstart, textYstart, mPaintBigThin);
            textXstart += mSecondsWidth;
            canvas.drawText(mSecondsLabel, textXstart, labelYStart, mPaintLabel);
            textXstart += mSecondsLabelWidth;
        }
        if (mHunderdths != null) {
            canvas.drawText(HUNDREDTH_SEPERATOR, textXstart, textYstart, mPaintLabel);
            textXstart += mHundredthsSepWidth;
            canvas.drawText(mHunderdths, textXstart, textYstart, mPaintMed);
        }
    }

    public void registerStopTextView(TextView stopStartTextView) {
        mStopStartTextView = stopStartTextView;
    }
}
