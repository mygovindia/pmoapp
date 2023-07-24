package com.sanskrit.pmo.network.mygov.models;

import org.parceler.Parcel;

@Parcel
public class ImageObject {
    public String id;
    public String title;
    public String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
