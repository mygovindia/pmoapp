package com.sanskrit.pmo.Utils;

public class SocialFeed {
    public int sourceType;
    public TweetFeed tweetFeed;
    public YoutubeFeed youtubeFeed;

    public SocialFeed(int sourcetype) {
        this.sourceType = sourcetype;
    }

    public void setYoutubeFeed(YoutubeFeed youtubeFeed) {
        this.youtubeFeed = youtubeFeed;
    }

    public void setTweetFeed(TweetFeed tweetFeed) {
        this.tweetFeed = tweetFeed;
    }

    public YoutubeFeed getYoutubeFeed() {
        return this.youtubeFeed;
    }

    public TweetFeed getTweetFeed() {
        return this.tweetFeed;
    }
}
