package com.sanskrit.pmo.network.mygov.models;

public class MediaCoverageDetails {
    private String ItemCount;

    private String Items;

    private MediaCoverageDatailContent[] content;

    public String getItemCount() {
        return ItemCount;
    }

    public void setItemCount(String ItemCount) {
        this.ItemCount = ItemCount;
    }

    public String getItems() {
        return Items;
    }

    public void setItems(String Items) {
        this.Items = Items;
    }

    public MediaCoverageDatailContent[] getContent() {
        return content;
    }

    public void setContent(MediaCoverageDatailContent[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ClassPojo [ItemCount = " + ItemCount + ", Items = " + Items + ", content = " + content + "]";
    }
}