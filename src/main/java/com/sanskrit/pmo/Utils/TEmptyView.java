package com.sanskrit.pmo.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanskrit.pmo.R;


public class TEmptyView extends FrameLayout {
    private static TViewUtil.EmptyViewBuilder mConfig = null;
    private String actionText;
    private Button mButton;
    private String mEmptyText;
    private int mIconSrc;
    private ImageView mImageView;
    private OnClickListener mOnClickListener;
    private boolean mShowButton;
    private boolean mShowIcon;
    private boolean mShowText;
    private int mTextColor;
    private float mTextSize;
    private TextView mTextView;

    public static void init(TViewUtil.EmptyViewBuilder defaultConfig) {
        mConfig = defaultConfig;
    }

    public static boolean hasDefaultConfig() {
        return mConfig != null;
    }

    public static TViewUtil.EmptyViewBuilder getConfig() {
        return mConfig;
    }

    public TEmptyView(Context context) {
        this(context, null);
    }

    public TEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mShowIcon = true;
        this.mShowText = true;
        this.mShowButton = false;
        inflate(context, R.layout.simple_empty_view, this);
        this.mTextView = (TextView) findViewById(R.id.t_emptyTextView);
        this.mImageView = (ImageView) findViewById(R.id.t_emptyImageIcon);
        this.mButton = (Button) findViewById(R.id.t_emptyButton);
    }

    public void setShowIcon(boolean mShowIcon) {
        this.mShowIcon = mShowIcon;
        this.mImageView.setVisibility(mShowIcon ? VISIBLE : GONE);
    }

    public void setShowText(boolean showText) {
        this.mShowText = showText;
        this.mTextView.setVisibility(showText ? VISIBLE : GONE);
    }

    public void setShowButton(boolean showButton) {
        this.mShowButton = showButton;
        this.mButton.setVisibility(showButton ? VISIBLE : GONE);
    }

    public float getTextSize() {
        return this.mTextSize;
    }

    public void setTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
        this.mTextView.setTextSize(mTextSize);
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        this.mTextView.setTextColor(mTextColor);
    }

    public String getEmptyText() {
        return this.mEmptyText;
    }

    public void setEmptyText(String mEmptyText) {
        this.mEmptyText = mEmptyText;
        this.mTextView.setText(mEmptyText);
    }

    public void setIcon(int mIconSrc) {
        this.mIconSrc = mIconSrc;
        this.mImageView.setImageResource(mIconSrc);
    }

    public void setIcon(Drawable drawable) {
        this.mIconSrc = 0;
        this.mImageView.setImageDrawable(drawable);
    }

    public void setAction(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        this.mButton.setOnClickListener(onClickListener);
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
        this.mButton.setText(actionText);
    }
}
