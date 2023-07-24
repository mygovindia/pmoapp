package com.sanskrit.pmo.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by manoj on 27/12/17.
 */

public class ImageInfo implements Parcelable {

    private String image_url;

    private String image_caption;

    protected ImageInfo(Parcel in) {
        image_url = in.readString();
        image_caption = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image_url);
        dest.writeString(image_caption);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageInfo> CREATOR = new Creator<ImageInfo>() {
        @Override
        public ImageInfo createFromParcel(Parcel in) {
            return new ImageInfo(in);
        }

        @Override
        public ImageInfo[] newArray(int size) {
            return new ImageInfo[size];
        }
    };

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_caption() {
        return image_caption;
    }

    public void setImage_caption(String image_caption) {
        this.image_caption = image_caption;
    }

    @Override
    public String toString() {
        return "ClassPojo [image_url = " + image_url + ", image_caption = " + image_caption + "]";
    }
}
