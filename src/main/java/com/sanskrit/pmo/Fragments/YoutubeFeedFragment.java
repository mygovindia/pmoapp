package com.sanskrit.pmo.Fragments;

import android.os.Bundle;

import android.util.Log;
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
import com.sanskrit.pmo.Adapters.YoutubeFeedAdapter;
import com.sanskrit.pmo.Models.YotubeFeedModels.Content;
import com.sanskrit.pmo.Models.YotubeFeedModels.Results;
import com.sanskrit.pmo.Models.YotubeFeedModels.YotubeItemsFeed;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Utils.YoutubeFeed;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.server.SanskritClient;
import com.sanskrit.pmo.uiwidgets.DividerItemDecoration;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.EndlessYoutubeScrollListner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YoutubeFeedFragment extends Fragment {
    YoutubeFeedAdapter adapter;
    int page_number = 0;
    ArrayList<YoutubeFeed> feedArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    private EndlessYoutubeScrollListner mScrollListener = null;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_youtubefeed, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        this.adapter = new YoutubeFeedAdapter((AppCompatActivity) getActivity(), new ArrayList());
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));

        recyclerView.setAdapter(adapter);
        this.swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        this.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mScrollListener != null) {
                    mScrollListener.resetAllData();
                }
                fetchYoutubeFeed(1);
            }
        });
        try {
            if (Reservoir.contains(Constants.CACHE_YOUTUBE_FEED)) {
                try {
                    adapter.updateDataSet((List) Reservoir.get(Constants.CACHE_YOUTUBE_FEED, new Token().getType()), recyclerView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }


        mScrollListener = new EndlessYoutubeScrollListner(this.recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                fetchYoutubeFeed(current_page);
            }
        };

        recyclerView.addOnScrollListener(mScrollListener);

        fetchYoutubeFeed(1);

        return rootView;
    }


    private void fetchYoutubeFeed(int page) {
        page_number = page;

        showSwipeRefresh();
        SanskritClient.getInstance(getActivity()).getYoutubeFeed("en",
                String.valueOf(page), new GenericCallback() {
                    @Override
                    public void failure() {
                        hideSwipeRefresh();
                    }

                    @Override
                    public void success(Object response) {
                        List<Content> feeds = new ArrayList<>();
                        Results results[] = new Results[0];
                        try {
                            results = ((YotubeItemsFeed) response).getResults();

                        } catch (Exception e) {

                        }
                        for (int i = 0; i < results.length; i++) {

                            Content content = new Content();
                            content.setTitle(results[i].getContent().getTitle());
                            content.setDescription(results[i].getContent().getDescription());
                            content.setId(results[i].getContent().getId());

                            content.setPublishedAt(results[i].getContent().getPublishedAt());
                            content.setThumbnail(results[i].getContent().getThumbnail());
                            //content.set(results[i].getContent().getThumbnail());
                            feeds.add(content);
                        }
                        if (page_number == 1) {
                            try {
                                adapter.updateDataSet(feeds, recyclerView);
                                try {
                                    Reservoir.put(Constants.CACHE_YOUTUBE_FEED, feeds);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e2) {
                                e2.printStackTrace();
                                return;
                            }
                        } else {
                            adapter.addDataSet(feeds, recyclerView);
                        }
                        hideSwipeRefresh();
                    }
                });

       /* try {
            showSwipeRefresh();
            SanskritTempClient.getInstance(getActivity(), new GenericCallback() {
                @Override
                public void failure() {
                    hideSwipeRefresh();
                }

                @Override
                public void success(final Object obj) {

                    final String response = (String) obj;
                    if (response != null) {

                        new AsyncTask<String, String, List<YoutubeFeed>>() {
                            protected List<YoutubeFeed> doInBackground(String... params) {
                                try {
                                    return getFeedsFromResponse(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }

                            protected void onPostExecute(List<YoutubeFeed> feeds) {
                                super.onPostExecute(feeds);
                                if (!(feeds == null || feeds.size() == 0)) {
                                    if (YoutubeFeedFragment.this.page_number == 1) {
                                        feedArrayList.clear();
                                        feedArrayList.addAll(feeds);
                                        adapter.updateDataSet(feedArrayList, recyclerView);
                                        try {
                                            Reservoir.put(Constants.CACHE_YOUTUBE_FEED, feeds);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        if (!(feeds == null || feeds.size() == 0)) {
                                            feedArrayList.addAll(feeds);
                                            adapter.updateDataSet(feedArrayList, recyclerView);

                                           // adapter.addDataSet(feeds, recyclerView);
                                        }
                                    }
                                }
                                hideSwipeRefresh();
                                adapter.notifyDataSetChanged();
                            }
                        }.execute(new String[]{""});

                    }

                    hideSwipeRefresh();

                }

            }).getYoutubeFeed("en", String.valueOf(page));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    /*private void fetchContent1(int page) {
        this.gPage = page;
        try {
            showSwipeRefresh();
            *//*SanskritTempClient.getInstance(getActivity(), new GenericCallback() {
                @Override
                public void failure() {
                    YoutubeFeedFragment.this.hideSwipeRefresh();
                }

                @Override
                public void success(Object obj) {
                    final String response = (String) obj;
                    new AsyncTask<String, String, List<SocialFeed>>() {
                        protected List<SocialFeed> doInBackground(String... params) {
                            if (YoutubeFeedFragment.this.getActivity() != null) {
                                try {
                                    return YoutubeFeedFragment.this.getFeedsFromResponse(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            return null;
                        }

                        protected void onPostExecute(List<SocialFeed> feeds) {
                            super.onPostExecute(feeds);
                            if (!(feeds == null || feeds.size() == 0)) {
                                if (YoutubeFeedFragment.this.gPage == 1) {
                                    YoutubeFeedFragment.this.adapter.updateDataSet(feeds);
                                    try {
                                        Reservoir.put(Constants.CACHE_YOUTUBE_FEED, feeds);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    YoutubeFeedFragment.this.adapter.addDataSet(feeds);
                                }
                            }
                            YoutubeFeedFragment.this.hideSwipeRefresh();
                        }
                    }.execute(new String[]{""});
                }





            }).getSocialFeed("youtube", String.valueOf(page));
*//*
            SanskritClient.getInstance(getActivity()).getYoutubeFeed("en", String.valueOf(page), new GenericCallback() {
                @Override
                public void failure() {
                    YoutubeFeedFragment.this.hideSwipeRefresh();
                }

                @Override
                public void success(Object obj) {
                    final String response = (String) obj;
                    //List<SocialFeed> feeds = (List) obj;
                    if (YoutubeFeedFragment.this.gPage == 1) {
                        try {
                            YoutubeFeedFragment.this.adapter.updateDataSet(feeds);
                            try {
                                Reservoir.put(Constants.CACHE_YOUTUBE_FEED, response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            return;
                        }
                    } else
                        YoutubeFeedFragment.this.adapter.addDataSet(feeds);
                    YoutubeFeedFragment.this.hideSwipeRefresh();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private List<YoutubeFeed> getFeedsFromResponse(String json) throws JSONException {
        List<YoutubeFeed> feeds = new ArrayList();
        JSONArray results = new JSONObject(json).getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject item = results.getJSONObject(i);
            JSONObject content = item.getJSONObject("content");
            if (item.getString("type").equals("youtube")) {
                Date yDate;
                try {
                    yDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'").parse(content.getString("publishedAt"));
                } catch (ParseException e) {
                    yDate = null;
                    e.printStackTrace();
                }
                try {
                    YoutubeFeed youtubeFeed = new YoutubeFeed(content.getString("id"), content.getString("title"), content.getString("thumbnail"), content.getString("description"), yDate);

                    feeds.add(youtubeFeed);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        Log.v("YOUTUBE_FEED", "DONE");
        return feeds;
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


    class Token extends TypeToken<List<Content>> {
        Token() {
        }
    }
}
