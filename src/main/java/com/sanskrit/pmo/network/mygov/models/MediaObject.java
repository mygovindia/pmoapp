package com.sanskrit.pmo.network.mygov.models;

public class MediaObject {
    public String imageId;
    public String imageurl;
    public String title;
    public String type;
    public String videoId;

    public MediaObject(String type, String imageUrl, String videoId, String imageId, String title) {
        this.type = type;
        this.imageurl = imageUrl;
        this.videoId = videoId;
        this.imageId = imageId;
        this.title = title;
    }
}
