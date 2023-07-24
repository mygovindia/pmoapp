package com.sanskrit.pmo.network.server.models;

import com.google.gson.annotations.SerializedName;

public class UserLogin {
    private static final String DOA = "DOA";
    private static final String DOB = "DOB";
    private static final String ERROR_CODE = "error_code";
    private static final String ERROR_RESPONSE = "response";
    private static final String FULL_NAME = "full_name";
    private static final String LANG_PREF = "langPref";
    private static final String UUID = "UUID";
    @SerializedName("DOA")
    public String mDOA;
    @SerializedName("DOB")
    public String mDOB;
    @SerializedName("error_code")
    public int mErrorCode;
    @SerializedName("response")
    public String mErrorResponse;
    @SerializedName("full_name")
    public String mFullName;
    @SerializedName("langPref")
    public String mLangPref;
    @SerializedName("UUID")
    public String mUUID;
}
