package com.sanskrit.pmo.network.mygov.models.gallery;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class ImageGalleryModel {
    private static final String CONTENT = "imageInfo";
    private static final String DATE = "date";
    private static final String ID = "ID";
    private static final String IMAGE = "feature_image";
    private static final String TITLE = "title";
    @SerializedName("imageInfo")
    public List<ImageGallerySubModel> mContent;
    @SerializedName("date")
    public String mDate;
    @SerializedName("feature_image")
    public String mFeatureImage;
    @SerializedName("ID")
    public String mID;
    @SerializedName("title")
    public String mTitle;
}
