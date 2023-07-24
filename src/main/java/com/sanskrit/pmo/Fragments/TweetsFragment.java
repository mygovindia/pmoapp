package com.sanskrit.pmo.Fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.TViewUtil;
import com.sanskrit.pmo.fragments.AuthenticatedTweetsFragment;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.server.SanskritClient;
import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.Result;
import com.sanskrit.pmo.twitter.core.TwitterAuthException;
import com.sanskrit.pmo.twitter.core.TwitterException;
import com.sanskrit.pmo.twitter.core.models.Tweet;
import com.sanskrit.pmo.twitter.tweetui.CompactTweetView;
import com.sanskrit.pmo.uiwidgets.DividerItemDecoration;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.EndlessRecyclerOnScrollListener;
import com.sanskrit.pmo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class TweetsFragment extends Fragment {

//    TweetsAdapter adapter;
//    int gPage = 0;
//    boolean isDarkTheme;
//    boolean isLightTheme;
//    RecyclerView recyclerView;
    WebView tweeterwebview;
    ProgressBar progressBar;
//    Tweet tweet;
//    private EndlessRecyclerOnScrollListener mScrollListener = null;

//    private class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
//        private Context context;
//        private List<Tweet> tweets;
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            CompactTweetView tweetView;
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//                this.tweetView = (CompactTweetView) itemView;
//            }
//        }
//
//        public TweetsAdapter(Context context, List<Tweet> statuse) {
//            this.context = context;
//            this.tweets = statuse;
//        }
//
//        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
//            CompactTweetView compactTweetView;
//            if (TweetsFragment.this.isLightTheme) {
//                compactTweetView = new CompactTweetView(this.context, (int) R.style.CustomTweetStyleLight);
//            } else if (TweetsFragment.this.isDarkTheme) {
//                compactTweetView = new CompactTweetView(this.context, (int) R.style.CustomTweetStyleDark);
//            } else {
//                compactTweetView = new CompactTweetView(this.context, (int) R.style.tw__TweetDarkWithActionsStyle);
//            }
//            return new ViewHolder(compactTweetView);
//        }
//
//        public void onBindViewHolder(ViewHolder viewHolder, int i) {
//            viewHolder.tweetView.setTweet(this.tweets.get(i));
//            viewHolder.tweetView.setOnActionCallback(TweetsFragment.this.actionCallback);
//        }
//
//        public int getItemCount() {
//            return this.tweets.size();
//        }
//
//        public void updateDataSet(List<Tweet> tweets) {
//            this.tweets = tweets;
//            notifyDataSetChanged();
//        }
//
//        public void addDataSet(List<Tweet> tweets) {
//            this.tweets.addAll(tweets);
//            notifyDataSetChanged();
//        }
//    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tweets, container, false);
         tweeterwebview = rootView.findViewById(R.id.aboutuswebview);
        Tweeter_Timeline();
//        this.isLightTheme = PreferencesUtility.getTheme(getActivity()).equals("light");
//        this.isDarkTheme = PreferencesUtility.getTheme(getActivity()).equals("dark");
//        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
//        this.adapter = new TweetsAdapter(getActivity(), new ArrayList());
//        if (getActivity() != null) {
//            this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//            this.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
//        }
//        this.recyclerView.setAdapter(this.adapter);
         progressBar = rootView.findViewById(R.id.progressbar);
//        this.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
//        showSwipeRefresh();
//        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
//        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (mScrollListener != null) {
//                    mScrollListener.resetAllData();
//                }
//                TweetsFragment.this.fetchTweets(1);
//            }
//        });
//
//        /*this.recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(this.recyclerView.getLayoutManager()) {
//            public void onLoadMore(int current_page) {
//                TweetsFragment.this.fetchTweets(current_page);
//            }
//        });*/
//
//
//        mScrollListener = new EndlessRecyclerOnScrollListener(this.recyclerView.getLayoutManager()) {
//            @Override
//            public void onLoadMore(int current_page) {
//                TweetsFragment.this.fetchTweets(current_page);
//            }
//        };
//
//        recyclerView.addOnScrollListener(mScrollListener);
//
//
//        try {
//            if (Reservoir.contains(Constants.CACHE_TWITTER_FEED)) {
//                try {
//                    this.adapter.updateDataSet((List) Reservoir.get(Constants.CACHE_TWITTER_FEED, new Token().getType()));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception e2) {
//            e2.printStackTrace();
//        }
//        if (Utils.isOnline(getActivity())) {
//            fetchTweets(1);
//        }
        return rootView;
    }

//    private void fetchTweets(int page) {
//        this.gPage = page;
//        showSwipeRefresh();
//        Log.v("TWITTER_FEED", "PAGE: " + page);
//        SanskritClient.getInstance(getActivity()).getTwitterFeed("en", String.valueOf(page), new GenericCallback() {
//            @Override
//            public void failure() {
//                TweetsFragment.this.hideSwipeRefresh();
//                TViewUtil.EmptyViewBuilder.getInstance(getActivity()).setEmptyText("NO DATA").setShowText(true).setIconSrc(R.drawable.ic_error_outline_black_24dp).setShowIcon(false).bindView(recyclerView);
//
//            }
//
//            @Override
//            public void success(Object response) {
//                try {
//
//                    List<Tweet> tweets = (List) response;
//                    Log.v("TWEET_RESPONSE", tweets.toString());
//                    Log.v("TWEET_RESPONSE", tweets.toArray().toString());
//                    if (!(tweets == null || tweets.size() == 0)) {
//                        if (TweetsFragment.this.gPage == 1) {
//                            try {
//                                TweetsFragment.this.adapter.updateDataSet(tweets);
//                                try {
//                                    Reservoir.put(Constants.CACHE_TWITTER_FEED, tweets);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            } catch (Exception e2) {
//                                e2.printStackTrace();
//                                return;
//                            }
//                        } else
//                            TweetsFragment.this.adapter.addDataSet(tweets);
//                    }
//                } catch (Exception e22) {
//                    e22.printStackTrace();
//                }
//                TweetsFragment.this.hideSwipeRefresh();
//            }
//        });
//
//    }
//
    public void transactFragment(AuthenticatedTweetsFragment fragment) {
        Fragment parent = getParentFragment();
        if (parent != null && (parent instanceof SocialFeedFragment)) {
            ((SocialFeedFragment) parent).transactFragment(fragment);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Fragment fragment = getChildFragmentManager().findFragmentByTag("TwitterLoginFragment");
            if (fragment != null && (fragment instanceof TwitterLoginFragment)) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void showSwipeRefresh() {
//        this.swipeRefresh.post(new Runnable() {
//            @Override
//            public void run() {
//                TweetsFragment.this.swipeRefresh.setRefreshing(true);
//            }
//        });
//    }
////
//    private void hideSwipeRefresh() {
//        this.swipeRefresh.post(new Runnable() {
//            @Override
//            public void run() {
//                TweetsFragment.this.swipeRefresh.setRefreshing(false);
//            }
//        });
//    }


//    class Token extends TypeToken<List<Tweet>> {
//        Token() {
//        }
//    }
//
//    final Callback<Tweet> actionCallback = new Callback<Tweet>() {
//        @Override
//        public void success(Result<Tweet> result) {
//
//        }
//
//        @Override
//        public void failure(TwitterException exception) {
//            if (exception instanceof TwitterAuthException) {
//                new TwitterLoginFragment().show(TweetsFragment.this.getChildFragmentManager(), "TwitterLoginFragment");
//            }
//
//        }
//    };
//private class HelloWebViewClient extends WebViewClient {
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        // final Uri uri = Uri.parse(url);
//        return urldataupdate(url);
//    }
//
//    @TargetApi(Build.VERSION_CODES.N)
//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//        final Uri uri = request.getUrl();
//
//        return urldataupdate(request.getUrl().toString());
//    }
//
//
//    @Override
//    public void onPageFinished(WebView view, final String url1) {
//        try {
////                if(camp_url.equals(url1)) {
//            view.loadUrl("javascript:(function() {" +
//                    "document.getElementsByClassName('section-top-wrapper')[0].style.display='none'; " +
//                    "document.getElementsByClassName('section-header-wrapper')[0].style.display='none'; " +
//                    "document.getElementsByClassName('footer-wrapper')[0].style.display='none'; " +
//                    "})()");
//            cappaindetails.setVisibility(View.VISIBLE);
//
////                }
//
//            try {
//                myDialog.dismiss();
//            }
//            catch (IllegalArgumentException e)
//            {
//
//            }
//            catch (Exception e)
//            {
//
//            }
//        } catch (Exception e) {
//            // e.printStackTrace();
//        }
//    }
//}
//    private inner class HelloWebViewClient() : WebViewClient() {
//        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
//            // final Uri uri = Uri.parse(url);
//            return true //urldataupdate(url);
//        }
//
//        @TargetApi(Build.VERSION_CODES.N)
//        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
//            //  final Uri uri = request.getUrl();
//            return true //urldataupdate(request.getUrl().toString());
//        }
//
//        override fun onPageFinished(view: WebView, url1: String) {
//            try {
////                if(camp_url.equals(url1)) {
//                view.loadUrl(
//                        "javascript:(function() {" +
//                                "document.getElementsByClassName('section-top-wrapper')[0].style.display='none'; " +
//                                "document.getElementsByClassName('section-header-wrapper')[0].style.display='none'; " +
//                                "document.getElementsByClassName('sidebar')[0].style.display='none'; " +
//                                "document.getElementsByClassName('footer-wrapper')[0].style.display='none'; " +
//                                "})()"
//                )
//                aboutuswebview.setVisibility(View.VISIBLE)
//
////                }
//                try {
//                    myDialog.dismiss()
//                } catch (e: IllegalArgumentException) {
//                } catch (e: Exception) {
//                }
//            } catch (e: Exception) {
//                if (myDialog != null && myDialog.isShowing()) myDialog.dismiss()
//            }
//        }
//    }

    private void Tweeter_Timeline()
    {
        String url = "https://twitter.com/PMOIndia";
        tweeterwebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        tweeterwebview.clearCache(true);
        WebSettings webSettings = tweeterwebview.getSettings();
        webSettings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.0; en-us; Droid Build/ESD20) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
      //  myDialog = CommonFunctions.showDialog(this)
       // myDialog.show()
        //myDialog.setCancelable(true)
       // myDialog.setCanceledOnTouchOutside(false)
        tweeterwebview.setVisibility(View.INVISIBLE);
        tweeterwebview.setBackgroundColor(Color.TRANSPARENT);
        //  aboutuswebview.loadDataWithBaseURL("https://twitter.com", CommonFunctions.widgetInfo, "text/html", "utf-8", null)
        tweeterwebview.loadUrl(url);
        tweeterwebview.setWebViewClient(new HelloWebViewClient());

    }


    private class HelloWebViewClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return true;
        }

        @Override
        public void onPageFinished(WebView view, final String url1) {
            try {
//                if(camp_url.equals(url1)) {
                view.loadUrl("javascript:(function() {" +
                        "document.getElementsByClassName('section-top-wrapper')[0].style.display='none'; " +
                        "document.getElementsByClassName('section-header-wrapper')[0].style.display='none'; " +
                        "document.getElementsByClassName('footer-wrapper')[0].style.display='none'; " +
                        "})()");
                tweeterwebview.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
//                }

//                try {
//                    myDialog.dismiss();
//                }
//                catch (IllegalArgumentException e)
//                {
//
//                }
//                catch (Exception e)
//                {
//
//                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }
}
