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

package com.sanskrit.pmo.twitter.core;

import com.sanskrit.pmo.twitter.core.*;
import com.sanskrit.pmo.twitter.core.TwitterCore;
import com.sanskrit.pmo.twitter.core.internal.TwitterApi;
import com.sanskrit.pmo.twitter.core.models.BindingValues;
import com.sanskrit.pmo.twitter.core.models.BindingValuesAdapter;
import com.sanskrit.pmo.twitter.core.models.SafeListAdapter;
import com.sanskrit.pmo.twitter.core.models.SafeMapAdapter;
import com.sanskrit.pmo.twitter.core.services.AccountService;
import com.sanskrit.pmo.twitter.core.services.CollectionService;
import com.sanskrit.pmo.twitter.core.services.ConfigurationService;
import com.sanskrit.pmo.twitter.core.services.FavoriteService;
import com.sanskrit.pmo.twitter.core.services.ListService;
import com.sanskrit.pmo.twitter.core.services.MediaService;
import com.sanskrit.pmo.twitter.core.services.SearchService;
import com.sanskrit.pmo.twitter.core.services.StatusesService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import javax.net.ssl.SSLSocketFactory;

import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;
import retrofit.converter.GsonConverter;

/**
 * A class to allow authenticated access to Twitter API endpoints.
 * Can be extended to provided additional endpoints by extending and providing Retrofit API
 * interfaces to {@link TwitterApiClient#getService(Class)}
 */
public class TwitterApiClient {
    private static final String UPLOAD_ENDPOINT = "https://upload.twitter.com";
    final ConcurrentHashMap<Class, Object> services;
    final RestAdapter apiAdapter;
    final RestAdapter uploadAdapter;

    TwitterApiClient(TwitterAuthConfig authConfig,
                     Session session,
                     TwitterApi twitterApi,
                     SSLSocketFactory sslSocketFactory, ExecutorService executorService) {

        if (session == null) {
            throw new IllegalArgumentException("Session must not be null.");
        }

        this.services = new ConcurrentHashMap<>();

        final Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new SafeListAdapter())
                .registerTypeAdapterFactory(new SafeMapAdapter())
                .registerTypeAdapter(BindingValues.class, new BindingValuesAdapter())
                .create();

        apiAdapter = new RestAdapter.Builder()
                .setClient(new AuthenticatedClient(authConfig, session, sslSocketFactory))
                .setEndpoint(twitterApi.getBaseHostUrl())
                .setConverter(new GsonConverter(gson))
                .setExecutors(executorService, new MainThreadExecutor())
                .build();

        uploadAdapter = new RestAdapter.Builder()
                .setClient(new AuthenticatedClient(authConfig, session, sslSocketFactory))
                .setEndpoint(UPLOAD_ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .setExecutors(executorService, new MainThreadExecutor())
                .build();
    }

    /**
     * Must be instantiated after {@link TwitterCore} has been
     * initialized via {@link io.fabric.sdk.android.Fabric#with(android.content.Context, io.fabric.sdk.android.Kit[])}.
     *
     * @param session Session to be used to create the API calls.
     *
     * @throws IllegalArgumentException if TwitterSession argument is null
     */
    public TwitterApiClient(Session session) {
        this(TwitterCore.getInstance().getAuthConfig(), session, new TwitterApi(),
                TwitterCore.getInstance().getSSLSocketFactory(),
                TwitterCore.getInstance().getFabric().getExecutorService());
    }

    /**
     * @return {@link AccountService} to access TwitterApi
     */
    public AccountService getAccountService() {
        return getService(AccountService.class);
    }

    /**
     * @return {@link FavoriteService} to access TwitterApi
     */
    public FavoriteService getFavoriteService() {
        return getService(FavoriteService.class);
    }

    /**
     * @return {@link StatusesService} to access TwitterApi
     */
    public StatusesService getStatusesService() {
        return getService(StatusesService.class);
    }

    /**
     * @return {@link SearchService} to access TwitterApi
     */
    public SearchService getSearchService() {
        return getService(SearchService.class);
    }

    /**
     * @return {@link ListService} to access TwitterApi
     */
    public ListService getListService() {
        return getService(ListService.class);
    }

    /**
     * Use CollectionTimeline directly, CollectionService is expected to change.
     * @return {@link CollectionService} to access TwitterApi
     */
    public CollectionService getCollectionService() {
        return getService(CollectionService.class);
    }

    /**
     * @return {@link ConfigurationService} to access TwitterApi
     */
    public ConfigurationService getConfigurationService() {
        return getService(ConfigurationService.class);
    }

    /**
     * @return {@link MediaService} to access Twitter API
     * upload endpoints.
     */
    public MediaService getMediaService() {
        return getAdapterService(uploadAdapter, MediaService.class);
    }

    /**
     * Converts Retrofit style interface into instance for API access
     *
     * @param cls Retrofit style interface
     * @return instance of cls
     */
    @SuppressWarnings("unchecked")
    protected <T> T getService(Class<T> cls) {
        return getAdapterService(apiAdapter, cls);
    }

    /**
     * Converts a Retrofit style interfaces into an instance using the given RestAdapter.
     * @param adapter the retrofit RestAdapter to use to generate a service instance
     * @param cls Retrofit style service interface
     * @return instance of cls
     */
    @SuppressWarnings("unchecked")
    protected <T> T getAdapterService(RestAdapter adapter, Class<T> cls) {
        if (!services.contains(cls)) {
            services.putIfAbsent(cls, adapter.create(cls));
        }
        return (T) services.get(cls);
    }
}
