package com.sanskrit.pmo.Utils;

import android.annotation.TargetApi;
import android.content.Context;

import android.util.AttributeSet;
import android.widget.ImageButton;

import androidx.core.view.ViewCompat;

import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.utils.TintHelper;


public class ThemeTintedImageButton extends ImageButton {
    public ThemeTintedImageButton(Context context) {
        super(context);
        tint();
    }

    public ThemeTintedImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        tint();
    }

    public ThemeTintedImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        tint();
    }

    @TargetApi(21)
    public ThemeTintedImageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        tint();
    }

    private void tint() {
        if (PreferencesUtility.getTheme(getContext()).equals("light")) {
            setImageDrawable(TintHelper.tintDrawable(getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));
        } else {
            setImageDrawable(TintHelper.tintDrawable(getDrawable(), -1));
        }
    }
}
