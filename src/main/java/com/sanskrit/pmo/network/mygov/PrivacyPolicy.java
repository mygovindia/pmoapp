package com.sanskrit.pmo.network.mygov;

public class PrivacyPolicy {
    private String post_title;

    private String post_content;

    private String post_date;

    private String ID;

    private String post_status;

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPost_status() {
        return post_status;
    }

    public void setPost_status(String post_status) {
        this.post_status = post_status;
    }

    @Override
    public String toString() {
        return "ClassPojo [post_title = " + post_title + ", post_content = " + post_content + ", post_date = " + post_date + ", ID = " + ID + ", post_status = " + post_status + "]";
    }
}
			
			