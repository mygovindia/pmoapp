package com.sanskrit.pmo.webview;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.webkit.WebView;

import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;

import java.util.List;

public class CustomTabActivityHelper {
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private ConnectionCallback mConnectionCallback;
    private CustomTabsSession mCustomTabsSession;

    public interface ConnectionCallback {
        void onCustomTabsConnected();

        void onCustomTabsDisconnected();
    }

    public interface CustomTabFallbackAuthWebView {
        void oepnAuthWebview(Activity activity, String str, WebView webView);
    }

    class C13691 extends CustomTabsServiceConnection {
        C13691() {
        }

        public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
            CustomTabActivityHelper.this.mClient = client;
            CustomTabActivityHelper.this.mClient.warmup(0);
            if (CustomTabActivityHelper.this.mConnectionCallback != null) {
                CustomTabActivityHelper.this.mConnectionCallback.onCustomTabsConnected();
            }
            CustomTabActivityHelper.this.getSession();
        }

        public void onServiceDisconnected(ComponentName name) {
            CustomTabActivityHelper.this.mClient = null;
            if (CustomTabActivityHelper.this.mConnectionCallback != null) {
                CustomTabActivityHelper.this.mConnectionCallback.onCustomTabsDisconnected();
            }
        }
    }

    public interface CustomTabFallback {
        void openUri(Activity activity, Uri uri);
    }

    public static void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri, CustomTabFallback fallback, boolean fallbackToBrowser) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, uri);
        } else if (fallback == null || fallbackToBrowser) {
            activity.startActivity(new Intent("android.intent.action.VIEW").setData(uri));
        } else {
            fallback.openUri(activity, uri);
        }
    }

    public static void openCustomTabWithBrowserFallback(Activity activity, CustomTabsIntent customTabsIntent, Uri uri, CustomTabFallback fallback) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, uri);
        } else if (fallback != null) {
            fallback.openUri(activity, uri);
        }
    }

    public static void openCustomTabAuthWebview(Activity activity, CustomTabsIntent customTabsIntent, String uri, WebView webView, CustomTabFallbackAuthWebView fallback) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (packageName != null) {
            customTabsIntent.intent.setPackage(packageName);
            customTabsIntent.launchUrl(activity, Uri.parse(uri));
        } else if (fallback != null) {
            fallback.oepnAuthWebview(activity, uri, webView);
        }
    }

    public void unbindCustomTabsService(Activity activity) {
        if (this.mConnection != null) {
            activity.unbindService(this.mConnection);
            this.mClient = null;
            this.mCustomTabsSession = null;
        }
    }

    public CustomTabsSession getSession() {
        if (this.mClient == null) {
            this.mCustomTabsSession = null;
        } else if (this.mCustomTabsSession == null) {
            this.mCustomTabsSession = this.mClient.newSession(null);
        }
        return this.mCustomTabsSession;
    }

    public void setConnectionCallback(ConnectionCallback connectionCallback) {
        this.mConnectionCallback = connectionCallback;
    }

    public void bindCustomTabsService(Activity activity) {
        if (this.mClient == null) {
            String packageName = CustomTabsHelper.getPackageNameToUse(activity);
            if (packageName != null) {
                this.mConnection = new C13691();
                CustomTabsClient.bindCustomTabsService(activity, packageName, this.mConnection);
            }
        }
    }

    public boolean mayLaunchUrl(Uri uri, Bundle extras, List<Bundle> otherLikelyBundles) {
        if (this.mClient == null) {
            return false;
        }
        CustomTabsSession session = getSession();
        if (session != null) {
            return session.mayLaunchUrl(uri, extras, otherLikelyBundles);
        }
        return false;
    }
}
