package com.sanskrit.pmo.Models;

import android.os.Parcelable;


public class ImageGalleryModel implements Parcelable {
    private Content[] content;

    private String Items;

    protected ImageGalleryModel(android.os.Parcel in) {
        Items = in.readString();
    }

    public static final Creator<ImageGalleryModel> CREATOR = new Creator<ImageGalleryModel>() {
        @Override
        public ImageGalleryModel createFromParcel(android.os.Parcel in) {
            return new ImageGalleryModel(in);
        }

        @Override
        public ImageGalleryModel[] newArray(int size) {
            return new ImageGalleryModel[size];
        }
    };

    public Content[] getContent() {
        return content;
    }

    public void setContent(Content[] content) {
        this.content = content;
    }

    public String getItems() {
        return Items;
    }

    public void setItems(String Items) {
        this.Items = Items;
    }

    @Override
    public String toString() {
        return "ClassPojo [content = " + content + ", Items = " + Items + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(Items);
    }
}
