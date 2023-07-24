package com.sanskrit.pmo.network.mygov.models;

public class MediaCoverageDatailContent {
    private String date;

    private String feature_image;

    private String notification_type;

    private String title;

    private String modified_date;

    private String mediaCoverText1;

    private String content;

    private String mediaCoverText2;

    private String mediaCoverText3;

    private String mediaCoverURL;

    private String ID;

    private String excerpt;

    private String lang;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFeature_image() {
        return feature_image;
    }

    public void setFeature_image(String feature_image) {
        this.feature_image = feature_image;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public String getMediaCoverText1() {
        return mediaCoverText1;
    }

    public void setMediaCoverText1(String mediaCoverText1) {
        this.mediaCoverText1 = mediaCoverText1;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaCoverText2() {
        return mediaCoverText2;
    }

    public void setMediaCoverText2(String mediaCoverText2) {
        this.mediaCoverText2 = mediaCoverText2;
    }

    public String getMediaCoverText3() {
        return mediaCoverText3;
    }

    public void setMediaCoverText3(String mediaCoverText3) {
        this.mediaCoverText3 = mediaCoverText3;
    }

    public String getMediaCoverURL() {
        return mediaCoverURL;
    }

    public void setMediaCoverURL(String mediaCoverURL) {
        this.mediaCoverURL = mediaCoverURL;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return "ClassPojo [date = " + date + ", feature_image = " + feature_image + ", notification_type = " + notification_type + ", title = " + title + ", modified_date = " + modified_date + ", mediaCoverText1 = " + mediaCoverText1 + ", content = " + content + ", mediaCoverText2 = " + mediaCoverText2 + ", mediaCoverText3 = " + mediaCoverText3 + ", mediaCoverURL = " + mediaCoverURL + ", ID = " + ID + ", excerpt = " + excerpt + ", lang = " + lang + "]";
    }
}