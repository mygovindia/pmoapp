package com.sanskrit.pmo.network.mygov.models;

import org.parceler.Parcel;

@Parcel
public class SearchModel {
    public String ID;
    public String content;
    public String date;
    public String link;
    public String title;
    public String type;
    public String feature_image;
    public String tenure;

    public String getYoutubeId() {
        return youtube_id;
    }

    public String youtube_id;


    public String getFeature_image() {
        return feature_image;
    }


    public String getTenure() {
        return tenure;
    }


    public String getId() {
        return this.ID;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public String getDate() {
        return this.date;
    }

    public String getType() {
        return this.type;
    }

    public String getLink() {
        return this.link;
    }
}
