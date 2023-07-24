package com.sanskrit.pmo.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sanskrit.pmo.Adapters.SearchInfographicsAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.MygovClient;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.callbacks.SearchResponse;
import com.sanskrit.pmo.network.mygov.models.SearchModel;
import com.sanskrit.pmo.uiwidgets.DividerItemDecoration;
import com.sanskrit.pmo.utils.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manoj on 27/3/18.
 */

public class SearchInfographicsActivity extends BaseActivity implements SearchView.OnQueryTextListener, View.OnTouchListener {


    public static final int REQUEST_VOICE = 9999;
    View emptyLayout;
    TextView emptyText;
    private InputMethodManager mImm;
    private SearchView mSearchView;
    private String queryString = "";
    private List<SearchModel> results = new ArrayList<>();
    private RecyclerView rvSearch;
    private SearchInfographicsAdapter searchAdapter;
    private Toolbar toolbar;
    SwipeRefreshLayout swipeRefresh;
    FrameLayout searchRecyclerviewContainer;
    boolean isClose;
    private EndlessRecyclerOnScrollListener mScrollListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView((int) R.layout.activity_search_new);
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        isClose = false;


        this.swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        this.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (emptyText.getText().toString().equalsIgnoreCase(getString(R.string.search_no_results))) {
                    queryString = "All";
                }
                results.clear();
                searchAdapter.notifyDataSetChanged();
                if (mScrollListener != null) {
                    mScrollListener.resetAllData();
                }
                doSearch(1);
            }
        });
        this.emptyText = (TextView) findViewById(R.id.empty_text);
        this.emptyLayout = findViewById(R.id.empty_layout);
        this.rvSearch = (RecyclerView) findViewById(R.id.rvsearchResults);
        this.searchRecyclerviewContainer = (FrameLayout) findViewById(R.id.recyclerview_container);
        this.results = new ArrayList();
        this.rvSearch.setLayoutManager(new LinearLayoutManager(this));
        this.searchAdapter = new SearchInfographicsAdapter(this, this.results);
        this.rvSearch.setAdapter(this.searchAdapter);
        this.rvSearch.addItemDecoration(new DividerItemDecoration(this, 1));

        this.searchMenuVisible = false;
        this.notifMenuVisible = false;
       /* this.rvSearch.addOnScrollListener(new EndlessRecyclerOnScrollListener(this.rvSearch.getLayoutManager()) {
            public void onLoadMore(int current_page) {
                doSearch(current_page);
            }
        });*/

        mScrollListener = new EndlessRecyclerOnScrollListener(this.rvSearch.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                doSearch(current_page);

            }
        };

        rvSearch.addOnScrollListener(mScrollListener);
        invalidateOptionsMenu();
    }

    private void doSearch(final int page) {
        //  searchAdapter.clearAdapter();
        searchRecyclerviewContainer.setVisibility(View.VISIBLE);

        showSwipeRefresh();
        this.emptyLayout.setVisibility(View.GONE);

        MygovClient.getInstance(this).searchInFoGraphics(this.queryString, PreferencesUtility.getLanguagePrefernce(this), String.valueOf(page), new GenericCallback() {
            public void success(Object res) {
                parseResponse((SearchResponse) res, page);


            }

            public void failure() {
                if (page == 1) {

                }
                hideSwipeRefresh();
            }
        });
    }


    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void parseResponse(SearchResponse response, int page) {

        List<SearchModel> models = response.getContent();

        if (models != null) {
            //this.results.clear();
            results.addAll(models);

            searchRecyclerviewContainer.setVisibility(View.VISIBLE);
            if (results.size() > 0) {

                //this.progressBar.dismiss();
                hideSwipeRefresh();

                if (page == 1) {
                    this.searchAdapter.updateDateSet(results);
                } else {
                    this.searchAdapter.notifyItemInserted(results.size());
                }
            } else if (page == 1) {

                // this.progressBar.dismiss();
                hideSwipeRefresh();
                //searchAdapter.clearAdapter();
                //  searchAdapter.updateAdapter(new ArrayList<SearchModel>());
                this.emptyLayout.setVisibility(View.VISIBLE);
                //  searchRecyclerviewContainer.setVisibility(View.GONE);
                this.emptyText.setText(R.string.search_no_results);
            }
        } else {
            if (page == 1) {

                this.emptyLayout.setVisibility(View.VISIBLE);
                // hideSwipeRefresh();
                //  searchRecyclerviewContainer.setVisibility(View.GONE);
                this.emptyText.setText(R.string.search_no_results);
            }
            hideSwipeRefresh();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        this.mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
        this.mSearchView.setOnQueryTextListener(this);
        this.mSearchView.setQueryHint(getString(R.string.hint_search));
        this.mSearchView.setIconifiedByDefault(false);
        this.mSearchView.setIconified(false);
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.menu_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                onBackPressed();

                return false;
            }
        });

        MenuItemCompat.expandActionView(menu.findItem(R.id.menu_search));
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onQueryTextSubmit(String query) {
        this.queryString = query;
        results.clear();
        searchAdapter.notifyDataSetChanged();
        if (mScrollListener != null) {
            mScrollListener.resetAllData();
        }
        doSearch(1);
        hideInputManager();
        return true;
    }

    public boolean onQueryTextChange(String newText) {
       /* if (!newText.equals(this.queryString) && newText.length() > 0) {
            this.queryString = newText;
            manipulateResults(this.queryString);
        }*/
        return true;
    }

    public boolean onTouch(View v, MotionEvent event) {
        hideInputManager();
        return false;
    }

    private void onVoiceClicked() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        intent.putExtra("android.speech.extra.MAX_RESULTS", 1);
        startActivityForResult(intent, REQUEST_VOICE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VOICE && resultCode == -1) {
            ArrayList<String> matches = data.getStringArrayListExtra("android.speech.extra.RESULTS");
            if (matches != null && matches.size() > 0) {
                String searchWrd = (String) matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    this.mSearchView.setQuery(searchWrd, false);
                    return;
                }
                return;
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void hideInputManager() {
        if (this.mSearchView != null) {
            if (this.mImm != null) {
                this.mImm.hideSoftInputFromWindow(this.mSearchView.getWindowToken(), 0);
            }
            this.mSearchView.clearFocus();
        }
    }

    private void manipulateResults(String query) {
        searchForAppResults(query);
    }

    private void searchForAppResults(String query) {
        if (query.trim().equals("")) {
            this.emptyLayout.setVisibility(View.VISIBLE);
            return;
        }
        //this.rvAppResults.getLayoutParams().height = getHeightOFRecyclerView(searchResults.size(), getResources().getDimensionPixelSize(R.dimen.item_search_appresults_height));
    }

    private int getHeightOFRecyclerView(int size, int itemHeight) {
        switch (size) {
            case 0:
                return 0;
            case 1:
            case 2:
                return itemHeight;
            case 3:
            case 4:
                return itemHeight * 2;
            default:
                return 0;
        }
    }

    private void showSwipeRefresh() {
        this.swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void hideSwipeRefresh() {
        this.swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);


            }
        });
    }

    @Override
    public void onBackPressed() {

        if (mSearchView != null && mSearchView.hasFocus()) {
            hideSoftKeyboard(this);
            mSearchView.clearFocus();
            isClose = true;
        } else {
            isClose = false;
            super.onBackPressed();
        }


        //  super.onBackPressed();

    }


}
