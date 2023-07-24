package com.sanskrit.pmo.network.server.models;

import com.google.gson.annotations.SerializedName;

public class UserProfileMygov {
    private static final String EMAIL = "mail";
    private static final String ERROR_CODE = "error_code";
    private static final String ERROR_RESPONSE = "response";
    private static final String FULL_NAME = "name";
    private static final String LANG_PREF = "langPref";
    private static final String UUID = "UUID";
    @SerializedName("mail")
    public String mEmail;
    @SerializedName("error_code")
    public int mErrorCode;
    @SerializedName("response")
    public String mErrorResponse;
    @SerializedName("name")
    public String mFullName;
    @SerializedName("langPref")
    public String mLangPref;
    @SerializedName("UUID")
    public String mUUID;
}
