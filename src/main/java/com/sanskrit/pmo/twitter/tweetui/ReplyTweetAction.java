package com.sanskrit.pmo.twitter.tweetui;

/**
 * Created by naman on 23/07/16.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.sanskrit.pmo.twitter.core.models.Tweet;

class ReplyTweetAction implements View.OnClickListener {
    final Tweet tweet;
    final TweetUi tweetUi;
    final TweetScribeClient tweetScribeClient;

    ReplyTweetAction(Tweet tweet, TweetUi tweetUi) {
        this(tweet, tweetUi, new TweetScribeClientImpl(tweetUi));
    }

    // For testing only
    ReplyTweetAction(Tweet tweet, TweetUi tweetUi, TweetScribeClient tweetScribeClient) {
        super();
        this.tweet = tweet;
        this.tweetUi = tweetUi;
        this.tweetScribeClient = tweetScribeClient;
    }

    @Override
    public void onClick(View v) {
        onClick(v.getContext(), v);
    }


    void onClick(Context context, View v) {
        if (tweet == null || tweet.user == null) return;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getShareContent()));
        context.startActivity(intent);
    }

    String getShareContent() {
        if (tweet.retweetedStatus == null)
            return "https://twitter.com/intent/tweet?in_reply_to=" + tweet.id;
        else return "https://twitter.com/intent/tweet?in_reply_to=" + tweet.retweetedStatus.id;
    }
}

