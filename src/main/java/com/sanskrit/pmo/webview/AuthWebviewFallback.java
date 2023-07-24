package com.sanskrit.pmo.webview;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class AuthWebviewFallback implements CustomTabActivityHelper.CustomTabFallbackAuthWebView {
    public void oepnAuthWebview(final Activity activity, String uri, WebView webView) {
        webView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //  ((LoginActivity) activity).onWebviewPageFinished();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(uri);
    }
}
