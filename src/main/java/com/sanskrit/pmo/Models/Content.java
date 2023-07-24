package com.sanskrit.pmo.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by manoj on 27/12/17.
 */

public class Content implements Parcelable {
    private ImageInfo[] imageInfo;

    private String title;

    private String ID;

    private String feature_image;

    private String date;

    protected Content(Parcel in) {
        imageInfo = in.createTypedArray(ImageInfo.CREATOR);
        title = in.readString();
        ID = in.readString();
        feature_image = in.readString();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(imageInfo, flags);
        dest.writeString(title);
        dest.writeString(ID);
        dest.writeString(feature_image);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };

    public ImageInfo[] getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ImageInfo[] imageInfo) {
        this.imageInfo = imageInfo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFeature_image() {
        return feature_image;
    }

    public void setFeature_image(String feature_image) {
        this.feature_image = feature_image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ClassPojo [imageInfo = " + imageInfo + ", title = " + title + ", ID = " + ID + ", feature_image = " + feature_image + ", date = " + date + "]";
    }
}
