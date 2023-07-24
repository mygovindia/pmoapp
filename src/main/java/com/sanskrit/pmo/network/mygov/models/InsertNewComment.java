package com.sanskrit.pmo.network.mygov.models;

import com.google.gson.annotations.SerializedName;

public class InsertNewComment {
    private static final String AUTHOR_EMAIL = "comment_author_email";
    private static final String AUTHOR_NAME = "author_name";
    private static final String COMMENT = "author_comment";
    private static final String DATE = "date";
    private static final String IP = "comment_author_IP";
    private static final String NEWS_ID = "news_post_id";
    private static final String PARENT_COMMENT_ID = "parent_comment_id";
    @SerializedName("author_comment")
    public String comment;
    @SerializedName("parent_comment_id")
    public String commentId;
    @SerializedName("date")
    public String date;
    @SerializedName("comment_author_email")
    public String email;
    @SerializedName("news_post_id")
    public String id;
    @SerializedName("comment_author_IP")
    public String ip;
    @SerializedName("author_name")
    public String name;

    public void setId(String id) {
        this.id = id;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
