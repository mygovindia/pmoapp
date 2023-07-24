package com.sanskrit.pmo.Models;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

@Parcel
public class ImageGallerySubModel {
    private static final String IMAGE_CAPTION = "image_caption";
    private static final String IMAGE_URL = "image_url";
    @SerializedName("image_caption")
    public String mCaption;
    @SerializedName("image_url")
    public String mUrl;
}
