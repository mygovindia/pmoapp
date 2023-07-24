package com.sanskrit.pmo.uiwidgets.parallaxheader;


import android.widget.ScrollView;

import androidx.fragment.app.Fragment;


public abstract class ScrollTabHolderFragment extends Fragment implements ScrollTabHolder {

    protected ScrollTabHolder mScrollTabHolder;

    public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
        mScrollTabHolder = scrollTabHolder;
    }

    @Override
    public void onScroll(ScrollView view, int x, int y, int oldX, int oldY, int pagePosition) {
        // nothing
    }


}