package com.sanskrit.pmo.Models.YotubeFeedModels;

public class Results {
    private String timestamp;

    private Content content;

    private String language;

    private String type;

    private String identifier;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "ClassPojo [timestamp = " + timestamp + ", content = " + content + ", language = " + language + ", type = " + type + ", identifier = " + identifier + "]";
    }
}