package com.sanskrit.pmo.uiwidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ThreeTwoImageView extends ImageView {
    public ThreeTwoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, MeasureSpec.makeMeasureSpec((MeasureSpec.getSize(widthSpec) * 2) / 3, 1073741824));
    }
}
