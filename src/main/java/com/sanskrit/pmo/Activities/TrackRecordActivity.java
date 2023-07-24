package com.sanskrit.pmo.Activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.Adapters.TrackRecordAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.TViewUtil;
import com.sanskrit.pmo.network.mygov.MyGovCacheClient;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.models.TrackRecord;
import com.sanskrit.pmo.uiwidgets.DividerItemDecoration;
import com.sanskrit.pmo.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;

public class TrackRecordActivity extends BaseActivity {
    TrackRecordAdapter adapter;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    Toolbar toolbar;
    public EndlessRecyclerOnScrollListener mScrollListener = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_trackrecord);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((int) R.string.track_record);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        this.adapter = new TrackRecordAdapter(this, new ArrayList());
        this.recyclerView.setAdapter(this.adapter);
       /* this.recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(this.recyclerView.getLayoutManager()) {
            public void onLoadMore(int current_page) {
                TrackRecordActivity.this.fetchTrackRecord(current_page);
            }
        });*/

        mScrollListener = new EndlessRecyclerOnScrollListener(this.recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                TrackRecordActivity.this.fetchTrackRecord(current_page);

            }
        };

        recyclerView.addOnScrollListener(mScrollListener);

        fetchTrackRecord(1);

    }

    private void fetchTrackRecord(final int page) {
        MyGovCacheClient.getInstance(this).getTrackRecord(PreferencesUtility.getLanguagePrefernce(this), String.valueOf(page), new GenericCallback() {
            public void success(Object response) {
                TrackRecord record = (TrackRecord) response;
                if (!(record.mContent == null || record.mContent.size() == 0)) {
                    if (page == 1) {
                        TrackRecordActivity.this.adapter.updateDataSet(record.mContent);
                    } else {
                        TrackRecordActivity.this.adapter.addDataSet(record.mContent);
                    }
                }
                TrackRecordActivity.this.progressBar.setVisibility(View.GONE);
            }

            public void failure() {
                TrackRecordActivity.this.progressBar.setVisibility(View.GONE);
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
                //  Toast.makeText(TrackRecordActivity.this, "call", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SearchTrackRecordActivity.class);
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
}
