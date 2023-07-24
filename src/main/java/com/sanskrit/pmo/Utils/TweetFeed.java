package com.sanskrit.pmo.Utils;

import java.util.Date;


public class TweetFeed {
    public Date date;
    public String id;
    public String tweet;

    public TweetFeed(String tweet, String id, Date date) {
        this.tweet = tweet;
        this.id = id;
        this.date = date;
    }

    public String getTweet() {
        return this.tweet;
    }

    public Date getDate() {
        return this.date;
    }

    public String getId() {
        return this.id;
    }
}
