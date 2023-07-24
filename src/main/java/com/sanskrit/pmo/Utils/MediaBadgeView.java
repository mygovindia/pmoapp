package com.sanskrit.pmo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanskrit.pmo.R;


public class MediaBadgeView extends FrameLayout {
    ImageView badge;
    TextView videoDuration;

    public MediaBadgeView(Context context) {
        this(context, null);
    }

    public MediaBadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MediaBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSubViews(context);
    }

    void initSubViews(Context context) {
        View view = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.tw__media_badge, this, true);
        this.videoDuration = (TextView) view.findViewById(R.id.tw__video_duration);
        this.badge = (ImageView) view.findViewById(R.id.tw__gif_badge);
    }

    public void setMediaEntity(MediaEntity entity) {
        if ("animated_gif".equals(entity.type)) {
            setBadge(getResources().getDrawable(R.drawable.tw__gif_badge));
        } else if ("video".equals(entity.type)) {
            setText(entity.videoInfo == null ? 0 : entity.videoInfo.durationMillis);
        } else {
            setEmpty();
        }
    }

    public void setCard(Card card) {
        if (VineCardUtils.isVine(card)) {
            setBadge(getResources().getDrawable(R.drawable.tw__vine_badge));
        } else {
            setEmpty();
        }
    }

    void setText(long duration) {
        this.videoDuration.setVisibility(0);
        this.badge.setVisibility(8);
        this.videoDuration.setText(MediaTimeUtils.getPlaybackTime(duration));
    }

    void setBadge(Drawable drawable) {
        this.badge.setVisibility(0);
        this.videoDuration.setVisibility(8);
        this.badge.setImageDrawable(drawable);
    }

    void setEmpty() {
        this.videoDuration.setVisibility(8);
        this.badge.setVisibility(8);
    }
}
