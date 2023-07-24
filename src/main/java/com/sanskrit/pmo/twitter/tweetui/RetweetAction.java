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

/**
 * Created by naman on 23/07/16.
 */
public class RetweetAction  extends BaseTweetAction implements View.OnClickListener {

    final Tweet tweet;
    final TweetRepository tweetRepository;
    final TweetUi tweetUi;
    final TweetScribeClient tweetScribeClient;

    TextView retweetCount;

    RetweetAction(Tweet tweet, TweetUi tweetUi, Callback<Tweet> cb, TextView count) {
        this(tweet, tweetUi, cb, new TweetScribeClientImpl(tweetUi));
        this.retweetCount = count;
    }

    // For testing only
    RetweetAction(Tweet tweet, TweetUi tweetUi, Callback<Tweet> cb,
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
            if (tweet.retweeted) {
                scribeUnRetweetAction();
                tweetRepository.unretweet(tweet.id,
                        new RetweetCallback(toggleImageButton, tweet, getActionCallback()));
            } else {
                scribeRetTweetAction();
                tweetRepository.retweet(tweet.id,
                        new RetweetCallback(toggleImageButton, tweet, getActionCallback()));
            }
        }
    }

    void scribeRetTweetAction() {
        tweetScribeClient.retweet(tweet);
    }

    void scribeUnRetweetAction() {
        tweetScribeClient.unretweet(tweet);
    }

    /*
     * Toggles like button state to handle favorite/unfavorite API exceptions. It calls through to
     * the given action callback.
     */
    class RetweetCallback extends Callback<Tweet> {
        ToggleImageButton button;
        Tweet tweet;
        Callback<Tweet> cb;

        /*
         * Constructs a new FavoriteCallback.
         * @param button Favorite ToggleImageButton which should reflect Tweet favorited state
         * @param wasFavorited whether the Tweet was favorited or not before the click
         * @param cb the Callback.
         */
        RetweetCallback(ToggleImageButton button, Tweet tweet, Callback<Tweet> cb) {
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
            exception.printStackTrace();
            if (exception instanceof TwitterApiException) {
                final TwitterApiException apiException = (TwitterApiException) exception;
                final int errorCode = apiException.getErrorCode();

                switch (errorCode) {
                    case TwitterApiConstants.Errors.ALREADY_FAVORITED:
                        final Tweet favorited = new TweetBuilder().copy(tweet).setRetweeted(true)
                                .build();
                        cb.success(new Result<>(favorited, null));
                        return;
                    case TwitterApiConstants.Errors.ALREADY_UNFAVORITED:
                        final Tweet unfavorited = new TweetBuilder().copy(tweet).setRetweeted(false)
                                .build();
                        cb.success(new Result<>(unfavorited, null));
                        return;
                    default:
                        // reset the toggle state back to match the Tweet
                        button.setToggledOn(tweet.retweeted);
                        retweetCount.setText(String.valueOf(tweet.retweetCount));
                        cb.failure(exception);
                        return;
                }
            }
            // reset the toggle state back to match the Tweet
            button.setToggledOn(tweet.retweeted);
            retweetCount.setText(String.valueOf(tweet.retweetCount));
            cb.failure(exception);
        }
    }

}
