package com.sanskrit.pmo.Models;

import com.sanskrit.pmo.network.mygov.models.NewsTag;

import java.io.Serializable;
import java.util.List;

public class NotificationInfo implements Serializable {
    private String id;
    private String notificationType;
    private String lang;
    private int parentID;
    private String title;
    private String content;
    private String excerpt;
    private String date;
    private String featureImage;
    private String newsMedia;
    private String newsTweets;
    private String soundCloudLink;
    private String link;
    private List<String> newsTweetsIDsList;
    private String modifiedDate;
    private int tagCount;

    // Youtube Live Stream
    private String objectID;
    private String videoID;

    private String createdAt;
    private String updatedAt;

    //Birthday Anniversary
    private String dob;
    private String name;

    private List<NewsTag> tagsList;
    private List<GetImageInfo> imageInfoList;

    public NotificationInfo() {
    }

    public NotificationInfo(String id, String notificationType, String lang, int parentID, String title, String content, String excerpt, String date, String featureImage, String newsMedia, String newsTweets, String soundCloudLink, String link, List<String> newsTweetsIDsList, String modifiedDate, int tagCount, String objectID, String videoID, String createdAt, String updatedAt, String dob, String name, List<NewsTag> tagsList, List<GetImageInfo> imageInfoList) {
        this.id = id;
        this.notificationType = notificationType;
        this.lang = lang;
        this.parentID = parentID;
        this.title = title;
        this.content = content;
        this.excerpt = excerpt;
        this.date = date;
        this.featureImage = featureImage;
        this.newsMedia = newsMedia;
        this.newsTweets = newsTweets;
        this.soundCloudLink = soundCloudLink;
        this.link = link;
        this.newsTweetsIDsList = newsTweetsIDsList;
        this.modifiedDate = modifiedDate;
        this.tagCount = tagCount;
        this.objectID = objectID;
        this.videoID = videoID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.dob = dob;
        this.name = name;
        this.tagsList = tagsList;
        this.imageInfoList = imageInfoList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFeatureImage() {
        return featureImage;
    }

    public void setFeatureImage(String featureImage) {
        this.featureImage = featureImage;
    }

    public String getNewsMedia() {
        return newsMedia;
    }

    public void setNewsMedia(String newsMedia) {
        this.newsMedia = newsMedia;
    }

    public String getNewsTweets() {
        return newsTweets;
    }

    public void setNewsTweets(String newsTweets) {
        this.newsTweets = newsTweets;
    }

    public String getSoundCloudLink() {
        return soundCloudLink;
    }

    public void setSoundCloudLink(String soundCloudLink) {
        this.soundCloudLink = soundCloudLink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<String> getNewsTweetsIDsList() {
        return newsTweetsIDsList;
    }

    public void setNewsTweetsIDsList(List<String> newsTweetsIDsList) {
        this.newsTweetsIDsList = newsTweetsIDsList;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getTagCount() {
        return tagCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDob() {
        return dob;
    }


    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NewsTag> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<NewsTag> tagsList) {
        this.tagsList = tagsList;
    }

    public List<GetImageInfo> getImageInfoList() {
        return imageInfoList;
    }

    public void setImageInfoList(List<GetImageInfo> imageInfoList) {
        this.imageInfoList = imageInfoList;
    }

    public static class Tags implements Serializable {
        String slug;
        String tagName;
        String termTaxonomyID;

        public Tags() {
        }

        public Tags(String slug, String tagName, String termTaxonomyID) {
            this.slug = slug;
            this.tagName = tagName;
            this.termTaxonomyID = termTaxonomyID;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getTagName() {
            return tagName;
        }

        public void setTagName(String tagName) {
            this.tagName = tagName;
        }

        public String getTermTaxonomyID() {
            return termTaxonomyID;
        }

        public void setTermTaxonomyID(String termTaxonomyID) {
            this.termTaxonomyID = termTaxonomyID;
        }

        @Override
        public String toString() {
            return "Tags{" +
                    "slug='" + slug + '\'' +
                    ", tagName='" + tagName + '\'' +
                    ", termTaxonomyID='" + termTaxonomyID + '\'' +
                    '}';
        }
    }

    public static class GetImageInfo implements Serializable {
        String imageCaption;
        String imageURL;

        public GetImageInfo() {
        }

        public GetImageInfo(String imageCaption, String imageURL) {
            this.imageCaption = imageCaption;
            this.imageURL = imageURL;
        }

        public String getImageCaption() {
            return imageCaption;
        }

        public void setImageCaption(String imageCaption) {
            this.imageCaption = imageCaption;
        }

        public String getImageURL() {
            return imageURL;
        }

        public void setImageURL(String imageURL) {
            this.imageURL = imageURL;
        }

        @Override
        public String toString() {
            return "GetImageInfo{" +
                    "imageCaption='" + imageCaption + '\'' +
                    ", imageURL='" + imageURL + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NotificationInfo{" +
                "id='" + id + '\'' +
                ", notificationType='" + notificationType + '\'' +
                ", lang='" + lang + '\'' +
                ", parentID=" + parentID +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", excerpt='" + excerpt + '\'' +
                ", date='" + date + '\'' +
                ", featureImage='" + featureImage + '\'' +
                ", newsMedia='" + newsMedia + '\'' +
                ", newsTweets='" + newsTweets + '\'' +
                ", soundCloudLink='" + soundCloudLink + '\'' +
                ", link='" + link + '\'' +
                ", newsTweetsIDsList=" + newsTweetsIDsList +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", tagCount=" + tagCount +
                ", objectID='" + objectID + '\'' +
                ", videoID='" + videoID + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", dob='" + dob + '\'' +
                ", name='" + name + '\'' +
                ", tagsList=" + tagsList +
                ", imageInfoList=" + imageInfoList +
                '}';
    }
}
