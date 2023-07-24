package com.sanskrit.pmo.utils;

import com.google.gson.annotations.SerializedName;

public class UserValue {
    @SerializedName("id_str")
    public final String idStr;

    public UserValue(String idStr) {
        this.idStr = idStr;
    }
}
