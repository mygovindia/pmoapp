package com.sanskrit.pmo.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubePlayer;
import com.sanskrit.pmo.Activities.CustomWishActivity;
import com.sanskrit.pmo.Activities.GalleryActivity;
import com.sanskrit.pmo.Activities.InfographicsActivity;
import com.sanskrit.pmo.Activities.MKBActivity;
import com.sanskrit.pmo.Activities.QuotesActivity;
import com.sanskrit.pmo.Activities.TrackRecordActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.network.server.models.Notification;
import com.sanskrit.pmo.player.youtube.Orientation;
import com.sanskrit.pmo.player.youtube.YouTubePlayerActivity;
import com.sanskrit.pmo.player.youtube.YouTubeUrlParser;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.NavigationUtils;

import java.util.Date;
import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Activity context;
    private List<Notification> objects;

    public NotificationAdapter(Activity context, List<Notification> objects) {
        this.context = context;
        this.objects = objects;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        Notification notification = objects.get(i);
        viewHolder.notification.setText(notification.getPush());

        long unixSeconds = Long.parseLong(notification.getTime());
        Date date = new Date(unixSeconds * 1000L);
        Log.e("Date", "onBindViewHolder: " + date);

        viewHolder.time.setText(DateUtils.getRelativeDateTimeString(context, date.getTime(), DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_SHOW_TIME));

    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView notification, time, date;

        public ViewHolder(View itemView) {
            super(itemView);

            notification = (TextView) itemView.findViewById(R.id.notification);
            date = (TextView) itemView.findViewById(R.id.notification_date);
            time = (TextView) itemView.findViewById(R.id.notification_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Notification object = objects.get(getAdapterPosition());
                    String type = object.getType();

                    Intent intent = null;
                    if (type.equalsIgnoreCase(Constants.NOTIFICATION_TYPE_LATEST_NEWS)) {
                        NewsFeed newsFeed = new NewsFeed();
                        newsFeed.mId = object.getExtra_id();
                        newsFeed.mTitle = object.getPush();
                        NavigationUtils.navigateToNewsDetail(context, newsFeed, true);

                    } else if (type.equalsIgnoreCase(Constants.NOTIFICATION_TYPE_INFOGRAPHICS)) {
                        intent = new Intent(context, InfographicsActivity.class);
                        context.startActivity(intent);
                    } else if (type.equalsIgnoreCase(Constants.NOTIFICATION_TYPE_GALLERY)) {
                        intent = new Intent(context, GalleryActivity.class);
                        context.startActivity(intent);
                    } else if (type.equalsIgnoreCase(Constants.NOTIFICATION_TYPE_TRACK_RECORD)) {
                        intent = new Intent(context, TrackRecordActivity.class);
                        context.startActivity(intent);
                    } else if (type.equalsIgnoreCase(Constants.NOTIFICATION_TYPE_PM_QUOTE)) {
                        intent = new Intent(context, QuotesActivity.class);
                        context.startActivity(intent);
                    } else if (type.equalsIgnoreCase(Constants.NOTIFICATION_TYPE_MANN_KI_BAAT)) {
                        intent = new Intent(context, MKBActivity.class);
                        intent.setAction("Audio");
                        context.startActivity(intent);
                    } else if (type.equalsIgnoreCase(Constants.NOTIFICATION_TYPE_LIVESTREAM)) {
                        intent = new Intent(context, YouTubePlayerActivity.class);
                        //Log.v("VIDEO_ID",info.getVideoID());
                        //Log.v("VIDEO_ID","ID: "+YouTubeUrlParser.getVideoId(info.getVideoID()));
                        //Log.v("VIDEO_URL",YouTubeUrlParser.getVideoSecuredUrl(info.getVideoID()));
                        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, YouTubeUrlParser.getVideoId(object.getLink()));
                        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO_START_WITH_LANDSCAPE);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                        context.startActivity(intent);
                    } else if (type.equalsIgnoreCase(Constants.NOTIFICATION_TYPE_BIRTHDAY)) {
                        intent = new Intent(context, CustomWishActivity.class);
                        intent.setAction("birthday");
                        context.startActivity(intent);
                    } else if (type.equalsIgnoreCase(Constants.NOTIFICATION_TYPE_ANNIVERSARY)) {
                        intent = new Intent(context, CustomWishActivity.class);
                        intent.setAction("anniversary");
                        context.startActivity(intent);
                    }
                   /* else if (type.equals("birthday") || type.equals("anniversary") || type.equals("livestream") || type.equals("poll")){
                         intent = new Intent(context, PushActionActivity.class);
                        intent.setAction(type);
                        intent.putExtra("link", object.getLink());
                        context.startActivity(intent);
                    }*/


/*
                    if (type.equalsIgnoreCase(Constants.NOTIFICATION_TYPE_LATEST_NEWS)) {
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
                    }*/


                }
            });

        }
    }

    public void updateDataSet(List<Notification> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    public void addDataSet(List<Notification> objects) {
        this.objects.addAll(objects);
        notifyDataSetChanged();
    }

    public void clearDataSet() {
        this.objects.clear();
        notifyDataSetChanged();
    }


}

