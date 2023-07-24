package com.sanskrit.pmo.network.server.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class MkbAudio {
    private static final String CONTENT = "content";
    private static final String DATE = "date";
    private static final String EXCERPT = "excerpt";
    private static final String ID = "ID";
    private static final String IMAGE = "feature_image";
    private static final String NEWS_MEDIA = "news_media";
    private static final String NEWS_TWEETS = "news_tweets";
    private static final String SOUNCLOUD_LINK = "soundCloudLink";
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
    @SerializedName("news_tweets")
    public String mNewsTweets;
    @SerializedName("soundCloudLink")
    public String mSoundCloudLink;
    @SerializedName("title")
    public String mTitle;
}
