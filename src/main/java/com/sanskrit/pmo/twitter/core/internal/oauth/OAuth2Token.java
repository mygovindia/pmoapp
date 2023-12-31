/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.sanskrit.pmo.twitter.core.internal.oauth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.sanskrit.pmo.twitter.core.AuthToken;
import com.sanskrit.pmo.twitter.core.TwitterAuthConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * OAuth2.0 token.
 */
public class OAuth2Token extends AuthToken implements Parcelable {
    public static final String TOKEN_TYPE_BEARER = "bearer";

    public static final Creator<OAuth2Token> CREATOR
            = new Creator<OAuth2Token>() {
        public OAuth2Token createFromParcel(Parcel in) {
            return new OAuth2Token(in);
        }

        public OAuth2Token[] newArray(int size) {
            return new OAuth2Token[size];
        }
    };

    @SerializedName("token_type")
    private final String tokenType;

    @SerializedName("access_token")
    private final String accessToken;

    public OAuth2Token(String tokenType, String accessToken) {
        super();
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    public OAuth2Token(String tokenType, String accessToken, long createdAt) {
        super(createdAt);
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }

    private OAuth2Token(Parcel in) {
        super();
        tokenType = in.readString();
        accessToken = in.readString();
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public boolean isExpired() {
        // Oauth 2.0 tokens do not have a common expiration policy. Returning false indicates
        // the token is not known to have expired. App auth tokens only expire when manually
        // invalidated, while guest auth tokens are known to have expired after 3 hours.
        return false;
    }

    @Override
    public Map<String, String> getAuthHeaders(TwitterAuthConfig authConfig, String method,
                                              String url, Map<String, String> postParams) {
        final Map<String, String> headers = new HashMap<>();
        final String authorizationHeader = OAuth2Service.getAuthorizationHeader(this);
        headers.put(HEADER_AUTHORIZATION, authorizationHeader);
        return headers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(tokenType);
        out.writeString(accessToken);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final OAuth2Token that = (OAuth2Token) o;

        if (accessToken != null ? !accessToken.equals(that.accessToken) : that.accessToken != null)
            return false;
        if (tokenType != null ? !tokenType.equals(that.tokenType) : that.tokenType != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = tokenType != null ? tokenType.hashCode() : 0;
        result = 31 * result + (accessToken != null ? accessToken.hashCode() : 0);
        return result;
    }
}
