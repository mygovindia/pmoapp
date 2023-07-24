package com.sanskrit.pmo.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sanskrit.pmo.R;

public class WebViewActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "WebViewActivity.EXTRA_URL";
    WebView mWebView;
    ProgressBar progressBar;
    Toolbar toolbar;

    class C13701 extends WebViewClient {
        C13701() {
        }

        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('header-wrapper')[0].style.display='none';" +
                    "document.getElementsByClassName('footer-wrapper')[0].style.display='none';" +
                    "})()");
            WebViewActivity.this.progressBar.setVisibility(View.GONE);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!url.startsWith("sanskritpmo://")) {
                return false;
            }
            WebViewActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            WebViewActivity.this.progressBar.setVisibility(View.VISIBLE);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.mWebView = (WebView) findViewById(R.id.web_view);
        String url = getIntent().getStringExtra(EXTRA_URL);
        this.mWebView.setWebViewClient(new C13701());
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.loadUrl(url);
        setupActionBar(url);
    }

    private void setupActionBar(String url) {
        setSupportActionBar(this.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle((CharSequence) "");
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
