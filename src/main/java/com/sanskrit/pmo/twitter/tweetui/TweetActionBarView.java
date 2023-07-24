/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.sanskrit.pmo.twitter.tweetui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.models.Tweet;

public class TweetActionBarView extends LinearLayout {
    final DependencyProvider dependencyProvider;
    ToggleImageButton likeButton;
    ToggleImageButton retweetButton;
    ImageButton shareButton, replyButton;
    TextView retweetCount, favouriteCount;
    Callback<Tweet> actionCallback;

    public TweetActionBarView(Context context) {
        this(context, null, new DependencyProvider());
    }

    public TweetActionBarView(Context context, AttributeSet attrs) {
        this(context, attrs, new DependencyProvider());
    }

    TweetActionBarView(Context context, AttributeSet attrs, DependencyProvider dependencyProvider) {
        super(context, attrs);
        this.dependencyProvider = dependencyProvider;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findSubviews();
    }

    /*
     * Sets the callback to call when a Tweet Action (favorite, unfavorite) is performed.
     */
    void setOnActionCallback(Callback<Tweet> actionCallback) {
        this.actionCallback = actionCallback;
    }

    void findSubviews() {
        likeButton = (ToggleImageButton) findViewById(R.id.tw__tweet_like_button);
        shareButton = (ImageButton) findViewById(R.id.tw__tweet_share_button);
        retweetButton = (ToggleImageButton) findViewById(R.id.tw__tweet_retweet_button);
        replyButton = (ImageButton) findViewById(R.id.tw__tweet_reply_button);
        retweetCount = (TextView) findViewById(R.id.tw_tweet_retweet_count);
        favouriteCount = (TextView) findViewById(R.id.tw_tweet_like_count);
    }

    /*
     * Setup action bar buttons with Tweet and action performer.
     * @param tweet Tweet source for whether an action has been performed (e.g. isFavorited?)
     */
    void setTweet(Tweet tweet) {
        setRetweet(tweet);
        setLike(tweet);
        setShare(tweet);
        setReply(tweet);
    }

    void setLike(Tweet tweet) {
        final TweetUi tweetUi = dependencyProvider.getTweetUi();
        if (tweet != null) {
            likeButton.setToggledOn(tweet.favorited);
            if (tweet.retweetedStatus == null)
                favouriteCount.setText(String.valueOf(tweet.favoriteCount));
            else favouriteCount.setText(String.valueOf(tweet.retweetedStatus.favoriteCount));
            final LikeTweetAction likeTweetAction = new LikeTweetAction(tweet,
                    tweetUi, actionCallback, favouriteCount);
            likeButton.setOnClickListener(likeTweetAction);
        }
    }

    void setShare(Tweet tweet) {
        final TweetUi tweetUi = dependencyProvider.getTweetUi();
        if (tweet != null) {
            shareButton.setOnClickListener(new ShareTweetAction(tweet, tweetUi));
        }
    }

    void setReply(Tweet tweet) {
        final TweetUi tweetUi = dependencyProvider.getTweetUi();
        if (tweet != null) {
            replyButton.setOnClickListener(new ReplyTweetAction(tweet, tweetUi));
        }
    }

    void setRetweet(Tweet tweet) {
        final TweetUi tweetUi = dependencyProvider.getTweetUi();
        if (tweet != null) {
            retweetButton.setToggledOn(tweet.retweeted);
            retweetCount.setText(String.valueOf(tweet.retweetCount));
            final RetweetAction retweetAction = new RetweetAction(tweet,
                    tweetUi, actionCallback, retweetCount);
            retweetButton.setOnClickListener(retweetAction);
        }
    }

    /**
     * x
     * This is a mockable class that extracts our tight coupling with the TweetUi singleton.
     */
    static class DependencyProvider {
        /**
         * Return TweetRepository
         */
        TweetUi getTweetUi() {
            return TweetUi.getInstance();
        }
    }
}
