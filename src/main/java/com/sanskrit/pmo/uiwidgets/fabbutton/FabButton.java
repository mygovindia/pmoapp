package com.sanskrit.pmo.uiwidgets.fabbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.sanskrit.pmo.R;


public class FabButton extends FrameLayout implements CircleImageView.OnFabViewListener {

    private CircleImageView mCircle;
    private ProgressRingView mRing;
    private float mRingWidthRatio = 0.14f; //of a possible 1f;
    private boolean mIndeterminate;
    private boolean mAutoStartAnim;
    private int mEndBitmapResource;
    private boolean mShowEndBitmap;
    private boolean mHideProgressOnComplete;
    private boolean mWasVisible = false;

    public FabButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public FabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public FabButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    protected void init(Context context, AttributeSet attrs, int defStyle) {
        View v = View.inflate(context, R.layout.widget_fab_button, this);
        mCircle = (CircleImageView) v.findViewById(R.id.fabbutton_circle);
        mRing = (ProgressRingView) v.findViewById(R.id.fabbutton_ring);
        mCircle.setFabViewListener(this);
        mRing.setFabViewListener(this);
        int color = Color.BLACK;
        int progressColor = Color.BLACK;
        int animDuration = 4000;
        int icon = -1;
        float maxProgress = 0;
        float progress = 0;
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageViewFabButton);
            color = a.getColor(R.styleable.CircleImageViewFabButton_android_color, Color.BLACK);
            progressColor = a.getColor(R.styleable.CircleImageViewFabButton_fbb_progressColor, Color.BLACK);
            progress = a.getFloat(R.styleable.CircleImageViewFabButton_android_progress, 0f);
            maxProgress = a.getFloat(R.styleable.CircleImageViewFabButton_android_max, 100f);
            mIndeterminate = a.getBoolean(R.styleable.CircleImageViewFabButton_android_indeterminate, false);
            mAutoStartAnim = a.getBoolean(R.styleable.CircleImageViewFabButton_fbb_autoStart, true);
            animDuration = a.getInteger(R.styleable.CircleImageViewFabButton_android_indeterminateDuration, animDuration);
            icon = a.getResourceId(R.styleable.CircleImageViewFabButton_android_src, icon);
            mRingWidthRatio = a.getFloat(R.styleable.CircleImageViewFabButton_fbb_progressWidthRatio, mRingWidthRatio);
            mEndBitmapResource = a.getResourceId(R.styleable.CircleImageViewFabButton_fbb_endBitmap, R.drawable.ic_fab_complete);
            mShowEndBitmap = a.getBoolean(R.styleable.CircleImageViewFabButton_fbb_showEndBitmap, false);
            mHideProgressOnComplete = a.getBoolean(R.styleable.CircleImageViewFabButton_fbb_hideProgressOnComplete, false);

            a.recycle();
        }

        mCircle.setColor(color);
        mCircle.setShowEndBitmap(mShowEndBitmap);
        mRing.setProgressColor(progressColor);
        mRing.setProgress(progress);
        mRing.setMaxProgress(maxProgress);
        mRing.setAutostartanim(mAutoStartAnim);
        mRing.setAnimDuration(animDuration);
        mCircle.setRingWidthRatio(mRingWidthRatio);
        mRing.setRingWithRatio(mRingWidthRatio);
        mRing.setIndeterminate(mIndeterminate);
        if (icon != -1) {
            mCircle.setIcon(icon, mEndBitmapResource);
        }
    }

    public void setIcon(int resource, int endIconResource) {
        mCircle.setIcon(resource, endIconResource);
    }

    public void setIconDrawable(Drawable drawable) {
        mCircle.setImageDrawable(drawable);
    }

    public void resetIcon() {
        mCircle.resetIcon();
    }

    /**
     * sets the progress to indeterminate or not
     *
     * @param indeterminate the flag
     */
    public void setIndeterminate(boolean indeterminate) {
        mIndeterminate = indeterminate;
        mRing.setIndeterminate(indeterminate);
    }

    public void setOnClickListener(OnClickListener listener) {
        mRing.setOnClickListener(listener);
        mCircle.setOnClickListener(listener);
    }

    /**
     * shows the animation ring
     *
     * @param show shows animation ring when set to true
     */
    public void showProgress(boolean show) {
        mCircle.showRing(show);
    }

    public void hideProgressOnComplete(boolean hide) {
        mHideProgressOnComplete = hide;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mCircle.setEnabled(enabled);
        mRing.setEnabled(enabled);
    }

    /**
     * sets current progress
     *
     * @param progress the current progress to set value too
     */
    public void setProgress(float progress) {
        mRing.setProgress(progress);
    }

    public void setMaxProgress(float max) {
        mRing.setMaxProgress(max);
    }

    public float getMaxProgress() {
        return mRing.getMaxProgress();
    }

    @Override
    public void onProgressVisibilityChanged(boolean visible) {
        if (mWasVisible == visible) {
            return;
        }

        mWasVisible = visible;
        if (visible) {
            mRing.setVisibility(View.VISIBLE);
            mRing.startAnimation();
        } else {
            mRing.stopAnimation(true);
            mRing.setVisibility(View.GONE);
        }
    }

    @Override
    public void onProgressCompleted() {
        mCircle.showCompleted(mShowEndBitmap, mHideProgressOnComplete);
        if (mHideProgressOnComplete) {
            mRing.setVisibility(View.GONE);
        }
    }
}