package com.sanskrit.pmo.network.mygov.models.polls;

import org.parceler.Parcel;

@Parcel
public class PollTimeUnd {
    public String date_type;
    public String timezone;
    public String timezone_db;
    public String value;

    public String getValue() {
        return this.value;
    }

    public String getTimezone() {
        return this.timezone;
    }

    public String getTimezone_db() {
        return this.timezone_db;
    }

    public String getDate_type() {
        return this.date_type;
    }
}
