package com.sanskrit.pmo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sanskrit.pmo.Fragments.TwitterLoginFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.Result;
import com.sanskrit.pmo.twitter.core.TwitterAuthException;
import com.sanskrit.pmo.twitter.core.TwitterException;
import com.sanskrit.pmo.twitter.core.models.Tweet;
import com.sanskrit.pmo.twitter.tweetui.TimelineResult;
import com.sanskrit.pmo.twitter.tweetui.TweetTimelineListAdapter;

public class AuthenticatedTweetsFragment extends ListFragment {

    SwipeRefreshLayout swipeRefresh;
    TweetTimelineListAdapter adapter;
    boolean isLightTheme, isDarkTheme;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_authenticated_tweets, container, false);

        swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        isLightTheme = PreferencesUtility.getTheme(getActivity()).equals("light");
        isDarkTheme = PreferencesUtility.getTheme(getActivity()).equals("dark");

        final com.sanskrit.pmo.twitter.tweetui.UserTimeline userTimeline = new com.sanskrit.pmo.twitter.tweetui.UserTimeline.Builder()
                .screenName("PMOIndia")
                .includeRetweets(true)
                .build();
        final TweetTimelineListAdapter.Builder builder = new TweetTimelineListAdapter.Builder(getActivity())
                .setOnActionCallback(actionCallback)
                .setTimeline(userTimeline);


        if (PreferencesUtility.getTheme(getActivity()).equals("dark")) {
            builder.setViewStyle(R.style.CustomTweetStyleDark);
        } else if (PreferencesUtility.getTheme(getActivity()).equals("black")) {
            builder.setViewStyle(R.style.tw__TweetDarkWithActionsStyle);
        } else builder.setViewStyle(R.style.tw__TweetLightWithActionsStyle);

        adapter = builder.build();
        setListAdapter(adapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        swipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });


        return rootView;
    }

    final Callback<Tweet> actionCallback = new Callback<Tweet>() {
        @Override
        public void success(Result<Tweet> result) {
            //TODO temporary fix
            adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                @Override
                public void success(Result<TimelineResult<Tweet>> result) {
                }

                @Override
                public void failure(TwitterException exception) {
                }
            });
        }

        @Override
        public void failure(TwitterException exception) {
            if (exception instanceof TwitterAuthException) {
                TwitterLoginFragment fragment = new TwitterLoginFragment();
                fragment.show(getChildFragmentManager(), "TwitterLoginFragment");
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Fragment fragment = getChildFragmentManager().findFragmentByTag("TwitterLoginFragment");
            if (fragment != null) {
                if (fragment instanceof TwitterLoginFragment) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}