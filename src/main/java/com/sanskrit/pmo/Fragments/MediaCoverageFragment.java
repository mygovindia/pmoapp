package com.sanskrit.pmo.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anupcowkur.reservoir.Reservoir;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.reflect.TypeToken;
import com.sanskrit.pmo.Adapters.MediaCoverageFeedAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.MygovClient;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.models.MediaCoverageDatailContent;
import com.sanskrit.pmo.network.mygov.models.MediaCoverageDetails;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.EndlessRecyclerOnScrollListener;
import com.sanskrit.pmo.utils.TintHelper;
import com.sanskrit.pmo.utils.Utils;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaCoverageFragment extends Fragment {


    MediaCoverageFeedAdapter adapter;
    FloatingActionButton fabFilter;
    List<String> filters;
    int position = 0;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    long time1;
    long time2;
    private EndlessRecyclerOnScrollListener mScrollListener = null;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        if (getArguments() != null) {
            this.position = getArguments().getInt(Constants.POSITION);
        }
        this.swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        this.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        this.fabFilter = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fabFilter.setVisibility(View.GONE);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mScrollListener != null) {
                    mScrollListener.resetAllData();
                }
                MediaCoverageFragment.this.fetchFeed(1);

            }
        });
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        this.adapter = new MediaCoverageFeedAdapter((AppCompatActivity) getActivity(), new ArrayList());
        this.recyclerView.setAdapter(this.adapter);

        if (PreferencesUtility.getTheme(getActivity()).equals("black")) {
            this.fabFilter.setImageDrawable(TintHelper.tintDrawable(this.fabFilter.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));
        }
        return rootView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.time2 = System.currentTimeMillis();
        try {
            if (Reservoir.contains(Constants.CACHE_MEDIA_FEED)) {
                try {
                    this.adapter.updateDataSet((List) Reservoir.get(Constants.CACHE_MEDIA_FEED, new MediaCoverageFragment.Token().getType()));
                    Log.e("news-time-cache", String.valueOf(System.currentTimeMillis() - this.time2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }

        mScrollListener = new EndlessRecyclerOnScrollListener(this.recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int i) {
                MediaCoverageFragment.this.fetchFeed(i);
            }
        };

        recyclerView.addOnScrollListener(mScrollListener);
        if (Utils.isOnline(getActivity())) {
            fetchFeed(1);
        }
    }

    private void fetchFeed(int page) {

        fetchNewsFeed(page);
    }

    private void fetchNewsFeed(final int page) {
        showSwipeRefresh();
        this.time1 = System.currentTimeMillis();
        MygovClient.getInstance(getActivity()).getLatestMediaCoverage(PreferencesUtility.getLanguagePrefernce(getActivity()), String.valueOf(page), new GenericCallback() {
            @Override
            public void failure() {
                MediaCoverageFragment.this.hideSwipeRefresh();
            }

            @Override
            public void success(Object obj) {
                ArrayList<MediaCoverageDatailContent> response = new ArrayList<>();

                response.addAll(Arrays.asList(((MediaCoverageDetails) obj).getContent()));
                if (page == 1) {
                    try {
                        if (response != null)
                            MediaCoverageFragment.this.adapter.updateDataSet(response);
                        Log.d("response", "success: " + response);
                        try {
                            if (response != null)
                                Reservoir.put(Constants.CACHE_MEDIA_FEED, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                } else {
                    if (response != null)
                        MediaCoverageFragment.this.adapter.addDataSet(response);
                }

                MediaCoverageFragment.this.hideSwipeRefresh();
                Log.e("news-time-network", String.valueOf(System.currentTimeMillis() - MediaCoverageFragment.this.time1));

            }
        });
    }


    private void showSwipeRefresh() {
        this.swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                MediaCoverageFragment.this.swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void hideSwipeRefresh() {
        this.swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                MediaCoverageFragment.this.swipeRefresh.setRefreshing(false);
            }
        });
    }

    class Token extends TypeToken<List<MediaCoverageDatailContent>> {
        Token() {
        }
    }

}
