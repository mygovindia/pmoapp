package com.sanskrit.pmo.Utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sanskrit.pmo.R;

public class FontTextView extends TextView {
    public FontTextView(Context context) {
        super(context);
        init(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
        if (a.hasValue(0)) {
            TypedArray atp = getContext().obtainStyledAttributes(a.getResourceId(0, 16973886), R.styleable.FontTextAppearance);
            if (atp.hasValue(3)) {
                setFont(atp.getString(3));
            }
            atp.recycle();
        }
        if (a.hasValue(1)) {
            setFont(a.getString(1));
        }
        a.recycle();
    }

    public void setFont(String font) {
        setPaintFlags(getPaintFlags() | 1);
        setTypeface(FontUtil.get(getContext(), font));
    }
}
