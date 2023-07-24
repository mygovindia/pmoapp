package com.sanskrit.pmo.network.mygov.models.gallery;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class ImageGallery {
    private static final String CONTENT = "content";
    private static final String ITEMS = "Items";
    @SerializedName("content")
    public List<ImageGalleryModel> mContent;
    @SerializedName("Items")
    public String mItems;
}
