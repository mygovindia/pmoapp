package com.sanskrit.pmo.network.server.models;

import com.google.gson.annotations.SerializedName;

public class UserSignup {
    private static final String DESCRIPTON = "Description";
    private static final String DOA = "DOA";
    private static final String DOB = "DOB";
    private static final String ERROR_CODE = "error_code";
    private static final String ERROR_RESPONSE = "response";
    private static final String FULL_NAME = "full_name";
    private static final String SUCCESS_CODE = "Success_Code";
    private static final String UUID = "UUID";
    @SerializedName("DOA")
    public String mDOA;
    @SerializedName("DOB")
    public String mDOB;
    @SerializedName("Description")
    public String mDescription;
    @SerializedName("error_code")
    public int mErrorCode;
    @SerializedName("response")
    public String mErrorResponse;
    @SerializedName("full_name")
    public String mFullName;
    @SerializedName("Success_Code")
    public int mSuccessCode;
    @SerializedName("UUID")
    public String uuid;
}
