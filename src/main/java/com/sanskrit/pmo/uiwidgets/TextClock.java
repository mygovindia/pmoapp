package com.sanskrit.pmo.uiwidgets;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.utils.DateUtil;

import java.util.Calendar;
import java.util.TimeZone;

import static android.view.ViewDebug.ExportedProperty;
import static android.widget.RemoteViews.RemoteView;


@RemoteView
public class TextClock extends TextView {

    public static final CharSequence DEFAULT_FORMAT_12_HOUR = "h:mm a";


    public static final CharSequence DEFAULT_FORMAT_24_HOUR = "H:mm";

    private CharSequence mFormat12;
    private CharSequence mFormat24;

    @ExportedProperty
    private CharSequence mFormat;
    @ExportedProperty
    private boolean mHasSeconds;

    private boolean mAttached;

    private Calendar mTime;
    private String mTimeZone;

    private final ContentObserver mFormatChangeObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            chooseFormat();
            onTimeChanged();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            chooseFormat();
            onTimeChanged();
        }
    };

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mTimeZone == null && Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
                final String timeZone = intent.getStringExtra("time-zone");
                createTime(timeZone);
            }
            onTimeChanged();
        }
    };

    private final Runnable mTicker = new Runnable() {
        public void run() {
            onTimeChanged();

            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);

            getHandler().postAtTime(mTicker, next);
        }
    };


    @SuppressWarnings("UnusedDeclaration")
    public TextClock(Context context) {
        super(context);
        init();
    }


    @SuppressWarnings("UnusedDeclaration")
    public TextClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public TextClock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextClock, defStyle, 0);
        try {
            mFormat12 = a.getText(R.styleable.TextClock_format12Hour);
            mFormat24 = a.getText(R.styleable.TextClock_format24Hour);
            mTimeZone = a.getString(R.styleable.TextClock_timeZone);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        if (mFormat12 == null || mFormat24 == null) {
            //TODO add bestDateFormat
            //LocaleData ld = LocaleData.get(getContext().getResources().getConfiguration().locale);
            if (mFormat12 == null) {
                mFormat12 = "h:mm";
            }
            if (mFormat24 == null) {
                mFormat24 = "HH:mm";
            }
        }

        createTime(mTimeZone);
        // Wait until onAttachedToWindow() to handle the ticker
        chooseFormat(false);
    }

    private void createTime(String timeZone) {
        if (timeZone != null) {
            mTime = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        } else {
            mTime = Calendar.getInstance();
        }
    }


    @ExportedProperty
    public CharSequence getFormat12Hour() {
        return mFormat12;
    }


    public void setFormat12Hour(CharSequence format) {
        mFormat12 = format;

        chooseFormat();
        onTimeChanged();
    }


    @ExportedProperty
    public CharSequence getFormat24Hour() {
        return mFormat24;
    }


    public void setFormat24Hour(CharSequence format) {
        mFormat24 = format;

        chooseFormat();
        onTimeChanged();
    }


    public boolean is24HourModeEnabled() {
        return DateFormat.is24HourFormat(getContext());
    }


    public String getTimeZone() {
        return mTimeZone;
    }


    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;

        createTime(timeZone);
        onTimeChanged();
    }

    private void chooseFormat() {
        chooseFormat(true);
    }


    public CharSequence getFormat() {
        return mFormat;
    }


    private void chooseFormat(boolean handleTicker) {
        final boolean format24Requested = is24HourModeEnabled();

        //TODO add bestDateFormat
        // LocaleData ld = LocaleData.get(getContext().getResources().getConfiguration().locale);

        if (format24Requested) {
            mFormat = abc(mFormat24, mFormat12, "HH:mm");
        } else {
            mFormat = abc(mFormat12, mFormat24, "h:mm");
        }

        boolean hadSeconds = mHasSeconds;
        //TODO update seconds
        mHasSeconds = DateUtil.hasSeconds(mFormat);

        if (handleTicker && mAttached && hadSeconds != mHasSeconds) {
            if (hadSeconds) getHandler().removeCallbacks(mTicker);
            else mTicker.run();
        }
    }

    /**
     * Returns a if not null, else return b if not null, else return c.
     */
    private static CharSequence abc(CharSequence a, CharSequence b, CharSequence c) {
        return a == null ? (b == null ? c : b) : a;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;

            registerReceiver();
            registerObserver();

            createTime(mTimeZone);

            if (mHasSeconds) {
                mTicker.run();
            } else {
                onTimeChanged();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mAttached) {
            unregisterReceiver();
            unregisterObserver();

            getHandler().removeCallbacks(mTicker);

            mAttached = false;
        }
    }

    private void registerReceiver() {
        final IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

        getContext().registerReceiver(mIntentReceiver, filter, "com.sanskrit.pmo:textclock:RECIEVER", getHandler());
    }

    private void registerObserver() {
        final ContentResolver resolver = getContext().getContentResolver();
        resolver.registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);
    }

    private void unregisterReceiver() {
        getContext().unregisterReceiver(mIntentReceiver);
    }

    private void unregisterObserver() {
        final ContentResolver resolver = getContext().getContentResolver();
        resolver.unregisterContentObserver(mFormatChangeObserver);
    }

    private void onTimeChanged() {
        mTime.setTimeInMillis(System.currentTimeMillis());
        setText(DateFormat.format(mFormat, mTime));
    }


}