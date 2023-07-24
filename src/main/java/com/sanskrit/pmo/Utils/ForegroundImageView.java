package com.sanskrit.pmo.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.utils.Utils;


public class ForegroundImageView extends ImageView {
    private Drawable foreground;

    public ForegroundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ForegroundView);
        Drawable d = a.getDrawable(0);
        if (d != null) {
            setForeground(d);
        }
        a.recycle();
        if (Utils.isLollipop()) {
            setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.foreground != null) {
            this.foreground.setBounds(0, 0, w, h);
        }
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.foreground;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.foreground != null) {
            this.foreground.jumpToCurrentState();
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.foreground != null && this.foreground.isStateful()) {
            this.foreground.setState(getDrawableState());
        }
    }

    public Drawable getForeground() {
        return this.foreground;
    }

    public void setForeground(Drawable drawable) {
        if (this.foreground != drawable) {
            if (this.foreground != null) {
                this.foreground.setCallback(null);
                unscheduleDrawable(this.foreground);
            }
            this.foreground = drawable;
            if (this.foreground != null) {
                this.foreground.setBounds(0, 0, getWidth(), getHeight());
                setWillNotDraw(false);
                this.foreground.setCallback(this);
                if (this.foreground.isStateful()) {
                    this.foreground.setState(getDrawableState());
                }
            } else {
                setWillNotDraw(true);
            }
            invalidate();
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.foreground != null) {
            this.foreground.draw(canvas);
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (Utils.isLollipop() && this.foreground != null) {
            this.foreground.setHotspot(x, y);
        }
    }
}