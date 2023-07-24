package com.sanskrit.pmo.Utils;

import java.util.Date;


public class YoutubeFeed {
    public Date date;
    public String description;
    public String thumbnail;
    public String title;
    public String videoId;

    public YoutubeFeed(String id, String title, String thumbnail, String desc, Date date) {
        this.title = title;
        this.videoId = id;
        this.thumbnail = thumbnail;
        this.description = desc;
        this.date = date;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public Date getDate() {
        return this.date;
    }
}
