package com.sanskrit.pmo.utils;

import com.google.gson.annotations.SerializedName;

public class ImageValue {
    @SerializedName("alt")
    public final String alt;
    @SerializedName("height")
    public final int height;
    @SerializedName("url")
    public final String url;
    @SerializedName("width")
    public final int width;

    public ImageValue(int height, int width, String url, String alt) {
        this.height = height;
        this.width = width;
        this.url = url;
        this.alt = alt;
    }
}
