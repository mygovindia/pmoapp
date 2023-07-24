package com.sanskrit.pmo.network.server.models;

import com.google.gson.annotations.SerializedName;

public class RequestToken {
    private static final String ERROR_CODE = "error_code";
    private static final String ERROR_RESPONSE = "response";
    private static final String REQUEST_TOKEN = "requestToken";
    @SerializedName("error_code")
    public int mErrorCode;
    @SerializedName("response")
    public String mErrorResponse;
    @SerializedName("requestToken")
    public String mToken;
}
