package com.sanskrit.pmo.network.server.models;

public class MyGovToken {
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;

    public String getAccess_token() {
        return this.access_token;
    }

    public String getExpires_in() {
        return this.expires_in;
    }

    public String getToken_type() {
        return this.token_type;
    }

    public String getScope() {
        return this.scope;
    }

    public String getRefresh_token() {
        return this.refresh_token;
    }
}
