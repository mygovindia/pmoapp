package com.sanskrit.pmo.utils;

import android.net.Uri;

import org.parceler.Parcel;

@Parcel
public class ShareObject {
    public String content;
    public Uri imageUri = null;
    public String imageUrl;
    public int shareType;
    public String title;
    public String url;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getShareType() {
        return this.shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public Uri getImageUri() {
        return this.imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
