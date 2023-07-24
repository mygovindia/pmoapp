package com.sanskrit.pmo.Models;

import android.os.Parcel;
import android.os.Parcelable;


public class NewsFeed implements Parcelable {
    private String content;
    private String date;
    private String excerpt;
    private String ID;
    private String feature_image;
    private String news_media;
    private String tags;
    private String news_tweets;
    private String news_tweets_ids;
    private String tag_count;
    private String parent_id;
    private String modified_date;
    private String title;


    public static String CONTENT_KEY = "content";
    public static String DATE_KEY = "date";
    public static String EXCERPT_KEY = "excerpt";
    public static String ID_KEY = "ID";
    public static String FEATURE_IMAGE_KEY = "feature_image";
    public static String NEWS_MEDIA_KEY = "news_media";
    public static String TAGS_KEY = "tags";
    public static String NEWS_TWEETS_KEY = "news_tweets";
    public static String NEWS_TWEETS_IDS = "news_tweets_ids";
    public static String TAG_COUNT_KEY = "tag_count";
    public static String PARENT_ID_KEY = "parent_id";
    public static String MODIFIED_DATE_KEY = "modified_date";
    public static String TITLE_KEY = "title";


    public NewsFeed(Parcel in) {
        content = in.readString();
        date = in.readString();
        excerpt = in.readString();
        ID = in.readString();
        feature_image = in.readString();
        news_media = in.readString();
        tags = in.readString();
        news_tweets = in.readString();
        news_tweets_ids = in.readString();
        tag_count = in.readString();
        parent_id = in.readString();
        modified_date = in.readString();
        title = in.readString();
    }

    public static final Creator<NewsFeed> CREATOR = new Creator<NewsFeed>() {
        @Override
        public NewsFeed createFromParcel(Parcel in) {
            return new NewsFeed(in);
        }

        @Override
        public NewsFeed[] newArray(int size) {
            return new NewsFeed[size];
        }
    };

    public NewsFeed() {

    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFeature_image() {
        return feature_image;
    }

    public void setFeature_image(String feature_image) {
        this.feature_image = feature_image;
    }

    public String getNews_media() {
        return news_media;
    }

    public void setNews_media(String news_media) {
        this.news_media = news_media;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getNews_tweets() {
        return news_tweets;
    }

    public void setNews_tweets(String news_tweets) {
        this.news_tweets = news_tweets;
    }

    public String getNews_tweets_ids() {
        return news_tweets_ids;
    }

    public void setNews_tweets_ids(String news_tweets_ids) {
        this.news_tweets_ids = news_tweets_ids;
    }

    public String getTag_count() {
        return tag_count;
    }

    public void setTag_count(String tag_count) {
        this.tag_count = tag_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(date);
        dest.writeString(excerpt);
        dest.writeString(ID);
        dest.writeString(feature_image);
        dest.writeString(news_media);
        dest.writeString(tags);
        dest.writeString(news_tweets);
        dest.writeString(news_tweets_ids);
        dest.writeString(tag_count);
        dest.writeString(parent_id);
        dest.writeString(modified_date);
        dest.writeString(title);
    }
}
