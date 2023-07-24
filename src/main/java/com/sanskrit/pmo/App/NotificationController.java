package com.sanskrit.pmo.App;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.youtube.player.YouTubePlayer;
import com.sanskrit.pmo.Activities.CustomWishActivity;
import com.sanskrit.pmo.Activities.GalleryActivity;
import com.sanskrit.pmo.Activities.InfographicsActivity;
import com.sanskrit.pmo.Activities.MKBActivity;
import com.sanskrit.pmo.Activities.NewsDetailActivity;
import com.sanskrit.pmo.Activities.QuotesActivity;
import com.sanskrit.pmo.Activities.TrackRecordActivity;
import com.sanskrit.pmo.Models.NotificationInfo;
import com.sanskrit.pmo.Utils.NotificationUtils;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.player.youtube.Orientation;
import com.sanskrit.pmo.player.youtube.YouTubePlayerActivity;
import com.sanskrit.pmo.utils.Constants;

import org.parceler.Parcels;

public class NotificationController {
    public static final String TAG = NotificationController.class.getSimpleName();
    private Context context;
    private NotificationInfo info;
    private NotificationUtils notificationUtils;
    private Intent intent = null;

    public NotificationController(Context context, NotificationInfo info) {
        this.context = context;
        this.info = info;
    }

    public Intent getResultIntent() {
        if (info == null) return null;
        if (info.getNotificationType() == null) return null;


        if (info.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_TYPE_LATEST_NEWS)) {
            NewsFeed feed = getNewsFeed();
            intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("newsitem", Parcels.wrap(feed));
            intent.putExtra("shouldfetch", true);
        } else if (info.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_TYPE_INFOGRAPHICS)) {
            intent = new Intent(context, InfographicsActivity.class);
        } else if (info.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_TYPE_GALLERY)) {
            intent = new Intent(context, GalleryActivity.class);
        } else if (info.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_TYPE_TRACK_RECORD)) {
            intent = new Intent(context, TrackRecordActivity.class);
        } else if (info.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_TYPE_PM_QUOTE)) {
            intent = new Intent(context, QuotesActivity.class);
        } else if (info.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_TYPE_MANN_KI_BAAT)) {
            intent = new Intent(context, MKBActivity.class);
            intent.setAction("Audio");
        } else if (info.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_TYPE_LIVESTREAM)) {
            intent = new Intent(context, YouTubePlayerActivity.class);
            //Log.v("VIDEO_ID",info.getVideoID());
            //Log.v("VIDEO_ID","ID: "+YouTubeUrlParser.getVideoId(info.getVideoID()));
            //Log.v("VIDEO_URL",YouTubeUrlParser.getVideoSecuredUrl(info.getVideoID()));
            intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, info.getVideoID());
            intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
            intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO_START_WITH_LANDSCAPE);
            intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
            intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
        } else if (info.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_TYPE_BIRTHDAY)) {
            intent = new Intent(context, CustomWishActivity.class);
            intent.setAction("birthday");
        } else if (info.getNotificationType().equalsIgnoreCase(Constants.NOTIFICATION_TYPE_ANNIVERSARY)) {
            intent = new Intent(context, CustomWishActivity.class);
            intent.setAction("anniversary");
        }
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //showNotificationMessage(info.getTitle().trim(), info.getContent(), new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), intent);
            //context.startActivity(intent);
        }
        return intent;
    }

    private NewsFeed getNewsFeed() {
        NewsFeed feed = new NewsFeed();
        feed.setmTitle(info.getTitle());
        feed.setmCOntent(info.getContent());
        feed.setmDate(info.getDate());
        feed.setmExcerpt(info.getExcerpt());
        feed.setmId(info.getId());
        feed.setmImage(info.getFeatureImage());
        feed.setmNewsMedia(info.getNewsMedia());
        feed.setmNewsTags(info.getTagsList());
        feed.setmNewsTweets(info.getNewsTweets());

        feed.setmTweetIds(info.getNewsTweetsIDsList());
        feed.setmTagCount(info.getTagCount());
        return feed;
    }

    private void showNotificationMessage(String title, String message, String timeStamp, Intent intent) {
        Log.v(TAG, "showNotificationMessage()=>Title: " + title + " <=> Message: " + message);
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

}
