package com.sanskrit.pmo.Utils;

public class Content {
    private String publishedAt;

    private String id;

    private String title;

    private String thumbnail;

    private String description;

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ClassPojo [publishedAt = " + publishedAt + ", id = " + id + ", title = " + title + ", thumbnail = " + thumbnail + ", description = " + description + "]";
    }
}
		