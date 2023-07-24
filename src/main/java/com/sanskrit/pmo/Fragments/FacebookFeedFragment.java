package com.sanskrit.pmo.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.sanskrit.pmo.Adapters.FacebookFeedAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.server.SanskritClient;
import com.sanskrit.pmo.network.server.models.FacebookFeed;
import com.sanskrit.pmo.uiwidgets.DividerItemDecoration;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

public class FacebookFeedFragment extends Fragment {
    FacebookFeedAdapter adapter;
    int gPage = 0;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    private EndlessRecyclerOnScrollListener mScrollListener = null;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_facebook_feed, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        this.adapter = new FacebookFeedAdapter((AppCompatActivity) getActivity(), new ArrayList());
        this.swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        this.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (mScrollListener != null) {
                    mScrollListener.resetAllData();
                }
                FacebookFeedFragment.this.fetchContent(1);
            }
        });
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        try {
            if (Reservoir.contains(Constants.CACHE_FACEBOOK_FEED)) {
                try {
                    this.adapter.updateDataSet((List) Reservoir.get(Constants.CACHE_FACEBOOK_FEED, new Token().getType()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        fetchContent(1);
      /*  this.recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(this.recyclerView.getLayoutManager()) {
            public void onLoadMore(int current_page) {
                FacebookFeedFragment.this.fetchContent(current_page);
            }
        });*/

        mScrollListener = new EndlessRecyclerOnScrollListener(this.recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                FacebookFeedFragment.this.fetchContent(current_page);
            }
        };

        recyclerView.addOnScrollListener(mScrollListener);


        return rootView;
    }

    private void fetchContent(int page) {
        this.gPage = page;
        showSwipeRefresh();
        SanskritClient.getInstance(getActivity()).getFacebookFeed("en", String.valueOf(page), new GenericCallback() {
            @Override
            public void failure() {
                FacebookFeedFragment.this.hideSwipeRefresh();
            }

            @Override
            public void success(Object response) {
                List<FacebookFeed> feeds = (List) response;
                if (FacebookFeedFragment.this.gPage == 1) {
                    try {
                        FacebookFeedFragment.this.adapter.updateDataSet(feeds);
                        try {
                            Reservoir.put(Constants.CACHE_FACEBOOK_FEED, response);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return;
                    }
                } else
                    FacebookFeedFragment.this.adapter.addDataSet(feeds);
                FacebookFeedFragment.this.hideSwipeRefresh();
            }
        });
    }

    private void showSwipeRefresh() {
        this.swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                FacebookFeedFragment.this.swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void hideSwipeRefresh() {
        this.swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                FacebookFeedFragment.this.swipeRefresh.setRefreshing(false);
            }
        });
    }


    class Token extends TypeToken<List<FacebookFeed>> {
        Token() {
        }
    }

}
