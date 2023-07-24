package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class NewsFeed {
    private static final String CONTENT = "content";
    private static final String DATE = "date";
    private static final String EXCERPT = "excerpt";
    private static final String ID = "ID";
    private static final String IMAGE = "feature_image";
    private static final String NEWS_MEDIA = "news_media";
    private static final String NEWS_TAG = "tags";
    private static final String NEWS_TWEETS = "news_tweets";
    private static final String NEWS_TWEET_IDS = "news_tweets_ids";
    private static final String TAG_COUNT = "tag_count";
    private static final String TITLE = "title";
    @SerializedName("content")
    public String mCOntent;
    @SerializedName("date")
    public String mDate;
    @SerializedName("excerpt")
    public String mExcerpt;
    @SerializedName("ID")
    public String mId;
    @SerializedName("feature_image")
    public String mImage;
    @SerializedName("news_media")
    public String mNewsMedia;
    @SerializedName("tags")
    public List<NewsTag> mNewsTags;
    @SerializedName("news_tweets")
    public String mNewsTweets;
    @SerializedName("tag_count")
    public int mTagCount;
    @SerializedName("title")
    public String mTitle;
    @SerializedName("news_tweets_ids")
    public List<String> mTweetIds;

    public String getmCOntent() {
        return mCOntent;
    }

    public void setmCOntent(String mCOntent) {
        this.mCOntent = mCOntent;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmExcerpt() {
        return mExcerpt;
    }

    public void setmExcerpt(String mExcerpt) {
        this.mExcerpt = mExcerpt;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmNewsMedia() {
        return mNewsMedia;
    }

    public void setmNewsMedia(String mNewsMedia) {
        this.mNewsMedia = mNewsMedia;
    }

    public List<NewsTag> getmNewsTags() {
        return mNewsTags;
    }

    public void setmNewsTags(List<NewsTag> mNewsTags) {
        this.mNewsTags = mNewsTags;
    }

    public String getmNewsTweets() {
        return mNewsTweets;
    }

    public void setmNewsTweets(String mNewsTweets) {
        this.mNewsTweets = mNewsTweets;
    }

    public int getmTagCount() {
        return mTagCount;
    }

    public void setmTagCount(int mTagCount) {
        this.mTagCount = mTagCount;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public List<String> getmTweetIds() {
        return mTweetIds;
    }

    public void setmTweetIds(List<String> mTweetIds) {
        this.mTweetIds = mTweetIds;
    }
}
