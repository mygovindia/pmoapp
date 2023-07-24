package com.sanskrit.pmo.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anupcowkur.reservoir.Reservoir;
import com.sanskrit.pmo.Adapters.GalleryAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.TViewUtil;
import com.sanskrit.pmo.network.mygov.MygovClient;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.models.gallery.ImageGallery;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.EndlessRecyclerOnScrollListener;
import com.sanskrit.pmo.utils.Utils;

import java.util.ArrayList;

public class GalleryActivity extends BaseActivity {
    ProgressDialog pDialog;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    Toolbar toolbar;
    GalleryAdapter adapter;
    public EndlessRecyclerOnScrollListener mScrollListener = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_gallery);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((int) R.string.gallery);
        GridLayoutManager sgm = new GridLayoutManager(this, 2);
        this.recyclerView.setLayoutManager(sgm);
        this.swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        this.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (mScrollListener != null) {
                    mScrollListener.resetAllData();
                }
                GalleryActivity.this.fetchGallery(1);

            }
        });
        this.adapter = new GalleryAdapter(GalleryActivity.this, new ArrayList());
        this.recyclerView.setAdapter(this.adapter);
        try {
            if (Reservoir.contains(Constants.CACHE_GALLERY)) {
                try {
                    this.adapter.updateDataSet(((ImageGallery) Reservoir.get(Constants.CACHE_GALLERY, ImageGallery.class)).mContent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (Utils.isOnline(this)) {
            fetchGallery(1);
        }
        /*this.recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(sgm) {
            public void onLoadMore(int current_page) {
                GalleryActivity.this.fetchGallery(current_page);
            }
        });*/

        mScrollListener = new EndlessRecyclerOnScrollListener(this.recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                GalleryActivity.this.fetchGallery(current_page);
            }
        };

        recyclerView.addOnScrollListener(mScrollListener);


    }

    private void fetchGallery(final int page) {
        showSwipeRefresh(this.swipeRefresh);
        MygovClient.getInstance(this).getGallery(PreferencesUtility.getLanguagePrefernce(this), String.valueOf(page), new GenericCallback() {
            public void success(Object object) {
                ImageGallery gallery = (ImageGallery) object;
                if (page == 1) {
                    try {
                        Reservoir.put(Constants.CACHE_GALLERY, gallery);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    GalleryActivity.this.adapter.updateDataSet(gallery.mContent);
                } else {
                    GalleryActivity.this.adapter.addDataSet(gallery.mContent);
                }
                GalleryActivity.this.hideSwipeRefresh(GalleryActivity.this.swipeRefresh);
            }

            public void failure() {
                GalleryActivity.this.hideSwipeRefresh(GalleryActivity.this.swipeRefresh);
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
                // Toast.makeText(QuotesActivity.this, "call", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SearchGalleryActivity.class);
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
}
