package com.sanskrit.pmo.Activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.sanskrit.pmo.Adapters.InfographicsAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.TViewUtil;
import com.sanskrit.pmo.network.mygov.MyGovBlogsClient;
import com.sanskrit.pmo.network.mygov.callbacks.RecentPostsListener;
import com.sanskrit.pmo.network.mygov.models.Post;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.EndlessRecyclerOnScrollListener;
import com.sanskrit.pmo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class InfographicsActivity extends BaseActivity {
    InfographicsAdapter adapter;
    List<Post> posts;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    public EndlessRecyclerOnScrollListener mScrollListener = null;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_blogs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.infographics));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(llm);
        this.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mScrollListener != null) {
                    mScrollListener.resetAllData();
                }
                InfographicsActivity.this.fetchInfographics(1);

            }
        });
        this.posts = new ArrayList();
        this.adapter = new InfographicsAdapter(this, this.posts);
        this.recyclerView.setAdapter(this.adapter);
        try {
            if (Reservoir.contains(Constants.CACHE_INFOGRAPHICS)) {
                try {
                    this.adapter.updateDateSet((List) Reservoir.get(Constants.CACHE_INFOGRAPHICS, new Token().getType()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
//        this.recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
//            public void onLoadMore(int current_page) {
//                InfographicsActivity.this.fetchInfographics(current_page);
//            }
//        });
        mScrollListener = new EndlessRecyclerOnScrollListener(this.recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                InfographicsActivity.this.fetchInfographics(current_page);

            }
        };

        recyclerView.addOnScrollListener(mScrollListener);

        if (Utils.isOnline(this)) {
            fetchInfographics(1);
        }


    }

    private void fetchInfographics(final int page) {
        showSwipeRefresh(this.swipeRefresh);
        InfographicsActivity.this.swipeRefresh.setEnabled(true);

        MyGovBlogsClient.getInstance(this).getInfographics(PreferencesUtility.getLanguagePrefernce(this), String.valueOf(page), new RecentPostsListener() {
            public void onPostsFetched(Object response) {
                List<Post> infographicList = (List) response;

                if (!(infographicList == null || infographicList.size() == 0)) {
                    if (page == 1) {
                        InfographicsActivity.this.adapter.updateDateSet(infographicList);
                        try {
                            Reservoir.put(Constants.CACHE_INFOGRAPHICS, infographicList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        InfographicsActivity.this.adapter.addDataSet(infographicList);
                    }
                }
                InfographicsActivity.this.hideSwipeRefresh(InfographicsActivity.this.swipeRefresh);
            }

            public void onError() {
                InfographicsActivity.this.hideSwipeRefresh(InfographicsActivity.this.swipeRefresh);
                setEmptyView(recyclerView);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            case R.id.action_search:
                // Toast.makeText(InfographicsActivity.this, "call", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SearchInfographicsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setEmptyView(RecyclerView recyclerView) {
        TViewUtil.EmptyViewBuilder.getInstance(this).setEmptyText("NO DATA").setShowText(true).setShowIcon(false).setIconSrc(R.drawable.ic_error_outline_black_24dp).bindView(recyclerView);
    }

    public void showSwipeRefresh(final SwipeRefreshLayout swipeRefresh) {
        swipeRefresh.post(new Runnable() {
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    public void hideSwipeRefresh(final SwipeRefreshLayout swipeRefresh) {
        swipeRefresh.post(new Runnable() {
            public void run() {
                swipeRefresh.setRefreshing(false);
            }
        });
    }


    class Token extends TypeToken<List<Post>> {
        Token() {
        }
    }
}
