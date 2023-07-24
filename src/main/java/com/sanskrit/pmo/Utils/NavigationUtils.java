package com.sanskrit.pmo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;


import androidx.browser.customtabs.CustomTabsIntent;

import com.sanskrit.pmo.Activities.NewsDetailActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.MediaCoverageDatailContent;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.webview.CustomTabActivityHelper;
import com.sanskrit.pmo.webview.WebviewFallback;

import org.parceler.Parcels;


public class NavigationUtils {
    public static void navigateToNewsDetail(Context context, NewsFeed feed, boolean needsFetching) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("newsitem", Parcels.wrap(feed));
        intent.putExtra("shouldfetch", needsFetching);
        context.startActivity(intent);
    }

    public static void launchCustomTab(Activity context, String url) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        intentBuilder.setToolbarColor(context.getResources().getColor(R.color.colorPrimary));
        intentBuilder.setShowTitle(true);
        intentBuilder.addDefaultShareMenuItem();
        intentBuilder.enableUrlBarHiding();
        CustomTabActivityHelper.openCustomTab(context, intentBuilder.build(), Uri.parse(url), new WebviewFallback(), false);
    }

    public static void navigateToMediaCoverageDetail(Context context, MediaCoverageDatailContent mediaCoverageModel, boolean needsFetching) {

        NewsFeed newsFeed = new NewsFeed();
        newsFeed.setmId(mediaCoverageModel.getID());
        newsFeed.setmImage(mediaCoverageModel.getFeature_image());
        newsFeed.setmCOntent(mediaCoverageModel.getContent());
        newsFeed.setmDate(mediaCoverageModel.getDate());
        newsFeed.setmExcerpt(mediaCoverageModel.getExcerpt());
        newsFeed.setmNewsMedia(mediaCoverageModel.getMediaCoverURL());
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("newsitem", Parcels.wrap(newsFeed));
        intent.putExtra("shouldfetch", needsFetching);
        context.startActivity(intent);


    }

}
