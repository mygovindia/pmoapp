
package com.sanskrit.pmo.Fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.sanskrit.pmo.Activities.MKBActivity;
import com.sanskrit.pmo.Adapters.MKBVideoAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.server.SanskritClient;
import com.sanskrit.pmo.network.server.models.MkbVideo;
import com.sanskrit.pmo.network.server.models.MkbVideoResponse;
import com.sanskrit.pmo.uiwidgets.DividerItemDecoration;
import com.sanskrit.pmo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MKBVideoFragment extends Fragment {
    MKBVideoAdapter adapter;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mankibaat_video, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        this.progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        this.swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        this.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MKBVideoFragment.this.getTracks();
            }
        });
        if (((MKBActivity) getActivity()).getSupportActionBar() != null) {
            ((MKBActivity) getActivity()).getSupportActionBar().setTitle((CharSequence) "Videos");
        }
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
        setUpAdapter();
        try {
            if (Reservoir.contains(Constants.CACHE_MKB_VIDEOS)) {
                try {
                    this.adapter.updateDataSet((List) Reservoir.get(Constants.CACHE_MKB_VIDEOS, new Token().getType()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return rootView;
    }

    private void setUpAdapter() {
        this.adapter = new MKBVideoAdapter(getActivity(), new ArrayList());
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                MKBVideoFragment.this.swipeRefresh.setEnabled(((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0);

            }
        });
        if (getActivity() != null) {
            this.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        }
    }

    private void getTracks() {
        this.progressBar.setVisibility(View.VISIBLE);
        SanskritClient.getInstance(getActivity()).getMKBVideo(new GenericCallback() {
            @Override
            public void failure() {
                MKBVideoFragment.this.progressBar.setVisibility(View.GONE);
                MKBVideoFragment.this.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void success(Object response) {
                MkbVideoResponse videoResponse = (MkbVideoResponse) response;
                try {
                    Reservoir.put(Constants.CACHE_MKB_VIDEOS, videoResponse.getResults());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MKBVideoFragment.this.adapter.updateDataSet(videoResponse.getResults());
                MKBVideoFragment.this.progressBar.setVisibility(View.GONE);
                MKBVideoFragment.this.swipeRefresh.setRefreshing(false);
            }
        });


    }


    class Token extends TypeToken<List<MkbVideo>> {
        Token() {
        }
    }


}

