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

import android.view.View;
import android.widget.TextView;

import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.Result;
import com.sanskrit.pmo.twitter.core.TwitterApiException;
import com.sanskrit.pmo.twitter.core.TwitterException;
import com.sanskrit.pmo.twitter.core.internal.TwitterApiConstants;
import com.sanskrit.pmo.twitter.core.models.Tweet;
import com.sanskrit.pmo.twitter.core.models.TweetBuilder;

/*
 * LikeTweetAction is a click listener for ToggleImageButtons which performs Twitter API
 * favorite/unfavorite actions onClick, updates button state, and calls through to the given
 * callback.
 */
class LikeTweetAction extends BaseTweetAction implements View.OnClickListener {
    final Tweet tweet;
    final TweetRepository tweetRepository;
    final TweetUi tweetUi;
    final TweetScribeClient tweetScribeClient;

    TextView countText;


    LikeTweetAction(Tweet tweet, TweetUi tweetUi, Callback<Tweet> cb, TextView countText) {
        this(tweet, tweetUi, cb, new TweetScribeClientImpl(tweetUi));
        this.countText = countText;
    }

    // For testing only
    LikeTweetAction(Tweet tweet, TweetUi tweetUi, Callback<Tweet> cb,
                    TweetScribeClient tweetScribeClient) {
        super(cb);
        this.tweet = tweet;
        this.tweetUi = tweetUi;
        this.tweetScribeClient = tweetScribeClient;
        this.tweetRepository = tweetUi.getTweetRepository();
    }

    @Override
    public void onClick(View view) {
        if (view instanceof ToggleImageButton) {
            final ToggleImageButton toggleImageButton = (ToggleImageButton) view;
            if (tweet.favorited) {
                scribeUnFavoriteAction();
                tweetRepository.unfavorite(tweet.id,
                        new LikeCallback(toggleImageButton, tweet, getActionCallback()));
            } else {
                scribeFavoriteAction();
                tweetRepository.favorite(tweet.id,
                        new LikeCallback(toggleImageButton, tweet, getActionCallback()));
            }
        }
    }

    void scribeFavoriteAction() {
        tweetScribeClient.favorite(tweet);
    }

    void scribeUnFavoriteAction() {
        tweetScribeClient.unfavorite(tweet);
    }

    /*
     * Toggles like button state to handle favorite/unfavorite API exceptions. It calls through to
     * the given action callback.
     */
     class LikeCallback extends Callback<Tweet> {
        ToggleImageButton button;
        Tweet tweet;
        Callback<Tweet> cb;

        /*
         * Constructs a new FavoriteCallback.
         * @param button Favorite ToggleImageButton which should reflect Tweet favorited state
         * @param wasFavorited whether the Tweet was favorited or not before the click
         * @param cb the Callback.
         */
        LikeCallback(ToggleImageButton button, Tweet tweet, Callback<Tweet> cb) {
            this.button = button;
            this.tweet = tweet;
            this.cb = cb;
        }

        @Override
        public void success(Result<Tweet> result) {
            cb.success(result);
        }

        @Override
        public void failure(TwitterException exception) {
            if (exception instanceof TwitterApiException) {
                final TwitterApiException apiException = (TwitterApiException) exception;
                final int errorCode = apiException.getErrorCode();

                switch (errorCode) {
                    case TwitterApiConstants.Errors.ALREADY_FAVORITED:
                        final Tweet favorited = new TweetBuilder().copy(tweet).setFavorited(true)
                                .build();
                        cb.success(new Result<>(favorited, null));
                        return;
                    case TwitterApiConstants.Errors.ALREADY_UNFAVORITED:
                        final Tweet unfavorited = new TweetBuilder().copy(tweet).setFavorited(false)
                                .build();
                        cb.success(new Result<>(unfavorited, null));
                        return;
                    default:
                        // reset the toggle state back to match the Tweet
                        button.setToggledOn(tweet.favorited);
                        countText.setText(String.valueOf(tweet.favoriteCount));
                        cb.failure(exception);
                        return;
                }
            }
            // reset the toggle state back to match the Tweet
            button.setToggledOn(tweet.favorited);
            countText.setText(String.valueOf(tweet.favoriteCount));
            cb.failure(exception);
        }
    }
}
