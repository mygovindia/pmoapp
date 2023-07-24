package com.sanskrit.pmo.Utils;

import android.annotation.TargetApi;
import android.content.Context;

import android.util.AttributeSet;
import android.widget.Button;

import androidx.core.view.ViewCompat;

import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.utils.TintHelper;


public class ThemeTintedButton extends Button {
    public ThemeTintedButton(Context context) {
        super(context);
        tint();
    }

    public ThemeTintedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        tint();
    }

    public ThemeTintedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tint();
    }

    @TargetApi(21)
    public ThemeTintedButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        tint();
    }

    private void tint() {
        if (PreferencesUtility.getTheme(getContext()).equals("light")) {
            setCompoundDrawablesWithIntrinsicBounds(null, TintHelper.tintDrawable(getCompoundDrawables()[1], (int) ViewCompat.MEASURED_STATE_MASK), null, null);
            setTextColor(ViewCompat.MEASURED_STATE_MASK);
            return;
        }
        setCompoundDrawablesWithIntrinsicBounds(null, TintHelper.tintDrawable(getCompoundDrawables()[1], -1), null, null);
        setTextColor(-1);
    }
}
