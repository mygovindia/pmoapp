package com.sanskrit.pmo.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;


public class WebviewFallback implements CustomTabActivityHelper.CustomTabFallback {
    public void openUri(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_URL, uri.toString());
        activity.startActivity(intent);
    }
}
