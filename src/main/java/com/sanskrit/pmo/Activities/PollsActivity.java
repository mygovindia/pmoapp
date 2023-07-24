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
import com.sanskrit.pmo.Adapters.PollsAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.MyGovBlogsClient;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.models.polls.PollModel;
import com.sanskrit.pmo.uiwidgets.DividerItemDecoration;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PollsActivity extends BaseActivity {
    PollsAdapter adapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_poll);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.polls));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        this.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PollsActivity.this.fetchPolls();
            }
        });
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        this.adapter = new PollsAdapter(this, new ArrayList());
        this.recyclerView.setAdapter(this.adapter);
        try {
            if (Reservoir.contains(Constants.CACHE_POLLS)) {
                try {
                    this.adapter.updateDataSet((List) Reservoir.get(Constants.CACHE_POLLS, new Token().getType()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (Utils.isOnline(this)) {
            fetchPolls();
        }
        setEmptyView(this.recyclerView);
    }

    private void fetchPolls() {
        showSwipeRefresh(this.swipeRefresh);
        MyGovBlogsClient.getInstance(this).getAllPolls(1, new GenericCallback() {
            @Override
            public void failure() {
                PollsActivity.this.hideSwipeRefresh(PollsActivity.this.swipeRefresh);

            }

            @Override
            public void success(Object response) {
                PollsActivity.this.adapter.updateDataSet((List) response);
                PollsActivity.this.hideSwipeRefresh(PollsActivity.this.swipeRefresh);
                try {
                    Reservoir.put(Constants.CACHE_POLLS, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    class Token extends TypeToken<List<PollModel>> {
        Token() {
        }
    }
}
