package com.sanskrit.pmo.Fragments;

import android.os.Bundle;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nineoldandroids.view.ViewHelper;
import com.sanskrit.pmo.Activities.FormerPMActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.FormerPM;
import com.sanskrit.pmo.parallaxheader.NotifyingScrollView;
import com.sanskrit.pmo.parallaxheader.ScrollTabHolderFragment;

public class FormerPMFragment extends ScrollTabHolderFragment implements NotifyingScrollView.OnScrollChangedListener {
    private static final String ARG_POSITION = "position";
    TextView description;
    TextView date;
    private int mPosition;
    private NotifyingScrollView mScrollView;

    public static Fragment newInstance(int position) {
        FormerPMFragment f = new FormerPMFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPosition = getArguments().getInt("position");
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_former_pm, container, false);
        this.mScrollView = (NotifyingScrollView) rootView.findViewById(R.id.scrollview);
        this.mScrollView.setOnScrollChangedListener(this);
        this.description = (TextView) rootView.findViewById(R.id.description);
        this.date = (TextView) rootView.findViewById(R.id.date);
        return rootView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            this.description.setText(Html.fromHtml(((FormerPM) ((FormerPMActivity) getActivity()).formerPms.get(this.mPosition)).mCOntent));
            date.setText(((FormerPMActivity) getActivity()).formerPms.get(this.mPosition).mTenure);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void adjustScroll(int scrollHeight, int headerTranslationY) {
        ViewHelper.setScrollY(this.mScrollView, headerTranslationY - scrollHeight);
    }

    public void onScrollChanged(ScrollView view, int l, int t, int oldl, int oldt) {
        if (this.mScrollTabHolder != null) {
            this.mScrollTabHolder.onScroll(view, l, t, oldl, oldt, this.mPosition);
        }
    }
}
