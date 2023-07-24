package com.sanskrit.pmo.network.mygov.models.polls;

import org.parceler.Parcel;

@Parcel
public class PollModel {
    public TimeUnd field_deadline;
    public QuestionsUnd field_questions;
    public TimeUnd field_start_date;
    public ImagesUnd field_theme_image;
    public String nid;
    public String title;

    public String getTitle() {
        return this.title;
    }

    public String getNid() {
        return this.nid;
    }

    public TimeUnd getField_deadline() {
        return this.field_deadline;
    }

    public TimeUnd getField_start_date() {
        return this.field_start_date;
    }

    public QuestionsUnd getField_questions() {
        return this.field_questions;
    }

    public ImagesUnd getField_theme_image() {
        return this.field_theme_image;
    }
}
