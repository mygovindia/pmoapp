package com.sanskrit.pmo.Activities;

import android.os.Bundle;

import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.sanskrit.pmo.Adapters.NotificationAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.server.SanskritClient;
import com.sanskrit.pmo.network.server.models.Notification;
import com.sanskrit.pmo.network.server.models.NotificationResponse;
import com.sanskrit.pmo.uiwidgets.DividerItemDecoration;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.EndlessRecyclerOnScrollListener;
import com.sanskrit.pmo.utils.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends BaseActivity {

    RecyclerView recyclerView;
    NotificationAdapter adapter;
    SwipeRefreshLayout swipeRefresh;
    public EndlessRecyclerOnScrollListener mScrollListener = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.notifications);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchMenuVisible = false;

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);


        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mScrollListener != null) {
                    mScrollListener.resetAllData();
                }
                getAllNotifications(1);


            }
        });

        adapter = new NotificationAdapter(this, new ArrayList<Notification>());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mScrollListener = new EndlessRecyclerOnScrollListener(this.recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                getAllNotifications(current_page);
            }
        };

        recyclerView.addOnScrollListener(mScrollListener);

        if (Utils.isOnline(this))
            loadNotifications();

        notifMenuVisible = false;
        invalidateOptionsMenu();


    }

    private void loadNotifications() {
        adapter.clearDataSet();

        try {
            boolean cacheExists = Reservoir.contains(Constants.CACHE_MY_NOTIFICATIONS);
            if (cacheExists) {
                List<Notification> feeds;
                try {
                    Type resultType = new TypeToken<List<Notification>>() {
                    }.getType();
                    feeds = Reservoir.get(Constants.CACHE_MY_NOTIFICATIONS, resultType);
                    if (feeds != null && feeds.size() != 0) {
                        adapter.updateDataSet(feeds);
                        swipeRefresh.setRefreshing(false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                swipeRefresh.setRefreshing(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getAllNotifications(1);
    }

    private void getAllNotifications(final int page) {
        showSwipeRefresh(swipeRefresh);
        SanskritClient.getInstance(this).getAllNotifications(PreferencesUtility.getServerUuid(this), String.valueOf(page), new GenericCallback() {
            @Override
            public void success(Object response) {
                NotificationResponse notificationResponse = (NotificationResponse) response;

                if (notificationResponse != null && notificationResponse.getResults() != null && notificationResponse.getResults().size() != 0) {
                    if (page == 1) {
                        adapter.updateDataSet(notificationResponse.getResults());
                        try {
                            Reservoir.put(Constants.CACHE_MY_NOTIFICATIONS, notificationResponse.getResults());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (notificationResponse.getResults().size() > 0) {
                            adapter.addDataSet(notificationResponse.getResults());
                        }
                    }

                } else {

                    if (page == 1) {
                        try {
                            Reservoir.delete(Constants.CACHE_MY_NOTIFICATIONS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        adapter.clearDataSet();

                        setEmptyView(recyclerView);
                    }
                }
                hideSwipeRefresh(swipeRefresh);
            }

            @Override
            public void failure() {
                hideSwipeRefresh(swipeRefresh);
                setEmptyView(recyclerView);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
