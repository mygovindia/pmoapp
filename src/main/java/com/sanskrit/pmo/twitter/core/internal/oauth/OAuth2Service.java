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

import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.Result;
import com.sanskrit.pmo.twitter.core.TwitterAuthConfig;
import com.sanskrit.pmo.twitter.core.TwitterCore;
import com.sanskrit.pmo.twitter.core.TwitterException;
import com.sanskrit.pmo.twitter.core.internal.TwitterApi;

import javax.net.ssl.SSLSocketFactory;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.UrlUtils;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * OAuth2.0 service. Provides methods for requesting guest and application-only auth tokens.
 */
public class OAuth2Service extends OAuthService {
    OAuth2Api api;

    interface OAuth2Api {
        @POST("/1.1/guest/activate.json")
        void getGuestToken(@Header(AuthHeaders.HEADER_AUTHORIZATION) String auth,
                           @Body String dummy, Callback<GuestTokenResponse> cb);

        @Headers("Content-Type: application/x-www-form-urlencoded;charset=UTF-8")
        @FormUrlEncoded
        @POST("/oauth2/token")
        void getAppAuthToken(@Header(AuthHeaders.HEADER_AUTHORIZATION) String auth,
                             @Field(OAuthConstants.PARAM_GRANT_TYPE) String grantType,
                             Callback<AppAuthToken> cb);
    }

    public OAuth2Service(TwitterCore twitterCore, SSLSocketFactory sslSocketFactory,
                         TwitterApi api) {
        super(twitterCore, sslSocketFactory, api);
        this.api = getApiAdapter().create(OAuth2Api.class);
    }

    /**
     * Requests a guest auth token.
     *
     * @param callback The callback interface to invoke when when the request completes.
     */
    public void requestGuestAuthToken(final Callback<OAuth2Token> callback) {
        final Callback<AppAuthToken> appAuthCallback = new Callback<AppAuthToken>() {
            @Override
            public void success(Result<AppAuthToken> result) {
                final OAuth2Token appAuthToken = result.data;
                // Got back an app auth token, now request a guest auth token.
                final Callback<GuestTokenResponse> guestTokenCallback
                        = new Callback<GuestTokenResponse>() {
                    @Override
                    public void success(Result<GuestTokenResponse> result) {
                        // Return a GuestAuthToken that includes the guestToken.
                        final GuestAuthToken guestAuthToken = new GuestAuthToken(
                                appAuthToken.getTokenType(), appAuthToken.getAccessToken(),
                                result.data.guestToken);
                        callback.success(new Result<OAuth2Token>(guestAuthToken, null));
                    }

                    @Override
                    public void failure(TwitterException error) {
                        Fabric.getLogger().e(TwitterCore.TAG,
                                "Your app may not allow guest auth. Please talk to us "
                                        + "regarding upgrading your consumer key.", error);
                        callback.failure(error);
                    }
                };
                requestGuestToken(guestTokenCallback, appAuthToken);
            }

            @Override
            public void failure(TwitterException error) {
                Fabric.getLogger().e(TwitterCore.TAG, "Failed to get app auth token", error);
                if (callback != null) {
                    callback.failure(error);
                }
            }
        };
        requestAppAuthToken(appAuthCallback);
    }

    /**
     * Requests an application-only auth token.
     *
     * @param callback The callback interface to invoke when when the request completes.
     */
    public void requestAppAuthToken(final Callback<AppAuthToken> callback) {
        api.getAppAuthToken(getAuthHeader(), OAuthConstants.GRANT_TYPE_CLIENT_CREDENTIALS,
                callback);
    }

    /**
     * Requests a guest token.
     *
     * @param callback     The callback interface to invoke when when the request completes.
     * @param appAuthToken The application-only auth token.
     */
    public void requestGuestToken(final Callback<GuestTokenResponse> callback,
                                  OAuth2Token appAuthToken) {
        api.getGuestToken(getAuthorizationHeader(appAuthToken), "", callback);
    }

    /**
     * Gets authorization header for inclusion in HTTP request headers.
     */
    public static String getAuthorizationHeader(OAuth2Token token) {
        return OAuthConstants.AUTHORIZATION_BEARER + " " + token.getAccessToken();
    }

    private String getAuthHeader() {
        final TwitterAuthConfig authConfig = getTwitterCore().getAuthConfig();
        return OAuthConstants.AUTHORIZATION_BASIC + " " + HttpRequest.Base64.encode(
                UrlUtils.percentEncode(authConfig.getConsumerKey())
                        + ":"
                        + UrlUtils.percentEncode(authConfig.getConsumerSecret()));

    }
}
