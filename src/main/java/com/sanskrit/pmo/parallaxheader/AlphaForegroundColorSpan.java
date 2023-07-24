package com.sanskrit.pmo.parallaxheader;

import android.graphics.Color;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

public class AlphaForegroundColorSpan extends ForegroundColorSpan {
    private float mAlpha;

    public AlphaForegroundColorSpan(int color) {
        super(color);
    }

    public AlphaForegroundColorSpan(Parcel src) {
        super(src);
        this.mAlpha = src.readFloat();
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeFloat(this.mAlpha);
    }

    public void updateDrawState(TextPaint ds) {
        ds.setColor(getAlphaColor());
    }

    public void setAlpha(float alpha) {
        this.mAlpha = alpha;
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    private int getAlphaColor() {
        int foregroundColor = getForegroundColor();
        return Color.argb((int) (this.mAlpha * 255.0f), Color.red(foregroundColor), Color.green(foregroundColor), Color.blue(foregroundColor));
    }
}
