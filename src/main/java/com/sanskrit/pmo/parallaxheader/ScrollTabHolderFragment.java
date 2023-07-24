package com.sanskrit.pmo.parallaxheader;


import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

public abstract class ScrollTabHolderFragment extends Fragment implements ScrollTabHolder {
    protected ScrollTabHolder mScrollTabHolder;

    public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
        this.mScrollTabHolder = scrollTabHolder;
    }

    public void onScroll(ScrollView view, int x, int y, int oldX, int oldY, int pagePosition) {
    }
}
