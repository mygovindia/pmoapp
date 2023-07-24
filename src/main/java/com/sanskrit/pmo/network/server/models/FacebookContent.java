package com.sanskrit.pmo.network.server.models;

public class FacebookContent {
    private String created_time;
    private String fb_post_image;
    private String fb_type;
    private String id;
    private String link;
    private String message;

    public String getId() {
        return this.id;
    }

    public String getCreated_time() {
        return this.created_time;
    }

    public String getMessage() {
        return this.message;
    }

    public String getFb_type() {
        return this.fb_type;
    }

    public String getFb_post_image() {
        return this.fb_post_image;
    }

    public String getLink() {
        return this.link;
    }
}
