package com.sanskrit.pmo.Fragments;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.sanskrit.pmo.Adapters.FeedAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.TViewUtil;
import com.sanskrit.pmo.network.mygov.MygovClient;
import com.sanskrit.pmo.network.mygov.callbacks.NewsFeedListener;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.EndlessRecyclerOnScrollListener;
import com.sanskrit.pmo.utils.IOUtils;
import com.sanskrit.pmo.utils.TintHelper;
import com.sanskrit.pmo.utils.Utils;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeedFragment extends Fragment {
    FeedAdapter adapter;
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
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mScrollListener != null) {
                    mScrollListener.resetAllData();
                }
                IOUtils.saveArrayToPreferences(getActivity(), new ArrayList<String>());

                fabFilter.setImageResource(R.drawable.filter);
                FeedFragment.this.fetchFeed(1);

            }
        });
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        this.adapter = new FeedAdapter((AppCompatActivity) getActivity(), new ArrayList());
        this.recyclerView.setAdapter(this.adapter);
        this.fabFilter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                new NewsFilterFragment().show(FeedFragment.this.getChildFragmentManager(), "Filter Fragment");

            }
        });
        if (PreferencesUtility.getTheme(getActivity()).equals("black")) {
            this.fabFilter.setImageDrawable(TintHelper.tintDrawable(this.fabFilter.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));
        }
        return rootView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.time2 = System.currentTimeMillis();
        try {
            if (Reservoir.contains(Constants.CACHE_HOME_FEED)) {
                try {
                    this.adapter.updateDataSet((List) Reservoir.get(Constants.CACHE_HOME_FEED, new Token().getType()));
                    Log.e("news-time-cache", String.valueOf(System.currentTimeMillis() - this.time2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.filters = IOUtils.getArrayFromPref(getActivity());

        mScrollListener = new EndlessRecyclerOnScrollListener(this.recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int i) {
                FeedFragment.this.fetchFeed(i);
            }
        };

        recyclerView.addOnScrollListener(mScrollListener);
        if (Utils.isOnline(getActivity())) {
            fetchFeed(1);
        }
    }

    private void fetchFeed(int page) {

        this.filters = IOUtils.getArrayFromPref(getActivity());

        if (this.filters.size() == 0) {
            fetchNewsFeed(page);
        } else if (((String) this.filters.get(0)).equals("")) {

            fetchNewsFeed(page);
        } else {

          /*  if (mScrollListener != null) {
                mScrollListener.resetAllData();
            }*/
            fetchFilteredNews(page, StringUtil.join(this.filters, ","));
        }
    }

    private void fetchNewsFeed(final int page) {
        showSwipeRefresh();
        this.time1 = System.currentTimeMillis();
        MygovClient.getInstance(getActivity()).getLatestNews(PreferencesUtility.getLanguagePrefernce(getActivity()), String.valueOf(page), new NewsFeedListener() {
            public void success(List<NewsFeed> response) {
                if (page == 1) {
                    try {
                        FeedFragment.this.adapter.updateDataSet(response);
                        try {
                            Reservoir.put(Constants.CACHE_HOME_FEED, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                } else
                    FeedFragment.this.adapter.addDataSet(response);
                FeedFragment.this.hideSwipeRefresh();
                Log.e("news-time-network", String.valueOf(System.currentTimeMillis() - FeedFragment.this.time1));
            }

            public void failure() {
                FeedFragment.this.hideSwipeRefresh();
                TViewUtil.EmptyViewBuilder.getInstance(getActivity()).setEmptyText("NO DATA").setShowText(true).setIconSrc(R.drawable.ic_error_outline_black_24dp).setShowIcon(false).bindView(recyclerView);

            }
        });
    }

    private void fetchFilteredNews(final int page, String filters) {
        showSwipeRefresh();
        MygovClient.getInstance(getActivity()).getTagWiseNews(PreferencesUtility.getLanguagePrefernce(getActivity()), String.valueOf(page), filters, new NewsFeedListener() {
            public void success(List<NewsFeed> response) {
                if (page == 1) {
                    try {
                        FeedFragment.this.adapter.updateDataSet(response);
                        try {
                            Reservoir.put(Constants.CACHE_HOME_FEED, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                } else
                    FeedFragment.this.adapter.addDataSet(response);
                FeedFragment.this.hideSwipeRefresh();
            }

            public void failure() {
                FeedFragment.this.hideSwipeRefresh();
                TViewUtil.EmptyViewBuilder.getInstance(getActivity()).setEmptyText("NO DATA").setShowText(true).setIconSrc(R.drawable.ic_error_outline_black_24dp).setShowIcon(false).bindView(recyclerView);

            }
        });
    }

    public void filtersUpdated(List<String> enabledFilters) {
        if (enabledFilters != null) {
            this.adapter.clearDataSet();

            if (mScrollListener != null) {
                mScrollListener.resetAllData();
            }
            try {
                Reservoir.delete(Constants.CACHE_HOME_FEED);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (enabledFilters.size() == 0) {
                fabFilter.setImageResource(R.drawable.filter);

                fetchNewsFeed(1);
            } else if (((String) enabledFilters.get(0)).equals("")) {
                fabFilter.setImageResource(R.drawable.filter);
                fetchNewsFeed(1);
            } else {
                fabFilter.setImageResource(R.drawable.selected_filter);

                fetchFilteredNews(1, StringUtil.join((Collection) enabledFilters, ","));
            }
        }
    }

    private void showSwipeRefresh() {
        this.swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                FeedFragment.this.swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void hideSwipeRefresh() {
        this.swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                FeedFragment.this.swipeRefresh.setRefreshing(false);


            }
        });
    }


    class Token extends TypeToken<List<NewsFeed>> {
        Token() {
        }
    }

}
