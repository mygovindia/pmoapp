package com.sanskrit.pmo.network.server;

import android.content.Context;
import android.util.Log;

import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter.Builder;
import retrofit.RestAdapter.LogLevel;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;

public class RestServiceFactory {
    private static final long CACHE_SIZE = 1048576;
    private static final String TAG_OK_HTTP = "OkHttp";

    public static <T> T create(Context context, String baseUrl, Class<T> clazz, boolean cache) {
        return create(context, baseUrl, clazz, cache, null);
    }

    public static <T> T create(final Context context, String baseUrl, Class<T> clazz, final boolean cache, GenericCallback listener) {
        System.setProperty("http.keepAlive", "false");


        Log.e("test", "create: " + baseUrl);
        OkHttpClient okHttpClient = new OkHttpClient();
        if (cache) {
            try {
                okHttpClient.setCache(new Cache(context.getApplicationContext().getCacheDir(), CACHE_SIZE));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(40, TimeUnit.SECONDS);
        Builder builder = new Builder().setEndpoint(baseUrl).setLogLevel(LogLevel.NONE).setLog(new AndroidLog("server")).setRequestInterceptor(new RequestInterceptor() {
            public void intercept(RequestFacade request) {
                if (cache && PreferencesUtility.getBackgroundCacheEnabled(context)) {
                    request.addHeader("Cache-Control", String.format(Locale.ENGLISH, "max-age=%d,max-stale=%d", new Object[]{Integer.valueOf(604800), Integer.valueOf(31536000)}));
                }
            }
        }).setClient(new OkClient(okHttpClient));
        if (listener != null) {
            builder.setConverter(new RawResponseConverter(listener));
        }
        return builder.build().create(clazz);
    }

    public static Cache getCache(Context context) throws IOException {
        return new Cache(context.getApplicationContext().getCacheDir(), CACHE_SIZE);
    }
}