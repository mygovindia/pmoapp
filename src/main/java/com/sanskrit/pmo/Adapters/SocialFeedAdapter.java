package com.sanskrit.pmo.adapters;

import android.content.Intent;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubePlayer;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Utils.SocialFeed;
import com.sanskrit.pmo.Utils.YoutubeFeed;
import com.sanskrit.pmo.player.youtube.Orientation;
import com.sanskrit.pmo.player.youtube.YouTubePlayerActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class SocialFeedAdapter extends RecyclerView.Adapter<SocialFeedAdapter.ViewHolder> {

    private AppCompatActivity context;
    private List<SocialFeed> objects;
    SimpleDateFormat df;

    public SocialFeedAdapter(AppCompatActivity context, List<SocialFeed> feeds) {
        this.context = context;
        this.objects = feeds;
        df = new SimpleDateFormat("dd MMM yyyy");
    }


    @Override
    public SocialFeedAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        return new SocialFeedAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_socialfeed_youtube, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(SocialFeedAdapter.ViewHolder viewHolder, final int i) {
        SocialFeed socialFeed = objects.get(i);
        YoutubeFeed youtubeFeed = socialFeed.getYoutubeFeed();
        Picasso.with(context).load(youtubeFeed.getThumbnail()).into(viewHolder.thumbnail);
        viewHolder.content.setText(Html.fromHtml((youtubeFeed.getDescription())));
        viewHolder.content.setMovementMethod(LinkMovementMethod.getInstance());
        viewHolder.feedDate.setText(df.format(youtubeFeed.getDate()));
        viewHolder.feedTitle.setText("PMOfficeIndia");
        viewHolder.feedType.setText("Youtube");
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView content;
        protected TextView feedTitle, feedDate, feedType, readmore, share;
        protected ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.feed_content);
            feedTitle = (TextView) itemView.findViewById(R.id.feed_title);
            feedDate = (TextView) itemView.findViewById(R.id.feed_date);
            feedType = (TextView) itemView.findViewById(R.id.feed_type);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            readmore = (TextView) itemView.findViewById(R.id.read_more);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intent = new Intent(context, YouTubePlayerActivity.class);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, objects.get(getAdapterPosition()).getYoutubeFeed().getVideoId());
                    intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO_START_WITH_LANDSCAPE);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });


        }
    }

    public void updateDataSet(List<SocialFeed> feeds) {
        objects = feeds;
        notifyDataSetChanged();
    }

    public void addDataSet(List<SocialFeed> feeds) {
        objects.addAll(feeds);
        notifyDataSetChanged();
    }

}