package com.sanskrit.pmo.pushnotify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import com.google.android.youtube.player.YouTubePlayer;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.player.youtube.Orientation;
import com.sanskrit.pmo.player.youtube.YouTubePlayerActivity;
import com.sanskrit.pmo.player.youtube.YouTubeUrlParser;
import com.sanskrit.pmo.webview.CustomTabActivityHelper;
import com.sanskrit.pmo.webview.WebviewFallback;


public class PushActionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String action = getIntent().getAction();
        String link = getIntent().getStringExtra("link");

        switch (action) {
            case "poll":
                launchCustomTab(link);
                break;
            case "livestream":
                startYoutubeActivity(link);
                break;
            case "location":
                if (link == null) {
                    finish();
                } else launchCustomTab(link);
                break;
            case "birthday":
                if (action.equals("birthday")) {
                    Intent intent = new Intent(this, CustomWishActivity.class);
                    intent.setAction("birthday");
                    startActivity(intent);
                    finish();
                }
                break;
            case "anniversary":
                if (action.equals("anniversary")) {
                    Intent intent = new Intent(this, CustomWishActivity.class);
                    intent.setAction("anniversary");
                    startActivity(intent);
                    finish();
                }
                break;

        }
    }

    private void launchCustomTab(String url) {
        try {
            CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
            int color = getResources().getColor(R.color.colorPrimary);
            intentBuilder.setToolbarColor(color);
            intentBuilder.setShowTitle(true);
            CustomTabActivityHelper.openCustomTab(
                    this, intentBuilder.build(), Uri.parse(url), new WebviewFallback(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    private void startYoutubeActivity(String videoUrl) {
        try {
            Intent intent = new Intent(this, YouTubePlayerActivity.class);

            intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, YouTubeUrlParser.getVideoId(videoUrl));
            intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
            intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO_START_WITH_LANDSCAPE);
            intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
            intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }
}
