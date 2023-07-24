package com.sanskrit.pmo.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;


public class TypefaceTextView extends AppCompatTextView {

    public TypefaceTextView(Context context) {
        super(context);
        init();
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "EkMukta-Regular.otf");
            setTypeface(tf);
        }
    }
}
