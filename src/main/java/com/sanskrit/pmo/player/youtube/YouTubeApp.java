package com.sanskrit.pmo.player.youtube;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.NonNull;


public class YouTubeApp {
    public static void startVideo(@NonNull Context context, @NonNull String videoId) {
        Uri video_uri = Uri.parse(YouTubeUrlParser.getVideoUrl(videoId));
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("vnd.youtube:" + videoId));
        if (context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() == 0) {
            intent = new Intent("android.intent.action.VIEW", video_uri);
        }
        context.startActivity(intent);
    }
}
