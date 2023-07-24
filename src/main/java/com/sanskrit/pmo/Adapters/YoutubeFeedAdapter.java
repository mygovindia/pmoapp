package com.sanskrit.pmo.Adapters;

import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubePlayer;
import com.sanskrit.pmo.Models.YotubeFeedModels.Content;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.player.youtube.Orientation;
import com.sanskrit.pmo.player.youtube.YouTubePlayerActivity;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class YoutubeFeedAdapter extends RecyclerView.Adapter<YoutubeFeedAdapter.ViewHolder> {

    private AppCompatActivity context;

    private List<Content> feedList;
    private SimpleDateFormat df;

    public YoutubeFeedAdapter(AppCompatActivity context, List<Content> feedList) {
        this.context = context;
        this.feedList = feedList;
        df = new SimpleDateFormat("dd MMM yyyy");
    }


    @NonNull
    @Override
    public YoutubeFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_socialfeed_youtube, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeFeedAdapter.ViewHolder viewHolder, final int i) {

        try {
            Picasso.with(context).load(feedList.get(i).getThumbnail()).into(viewHolder.thumbnail);
            viewHolder.content.setText(Html.fromHtml((feedList.get(i).getDescription())));
            viewHolder.content.setMovementMethod(LinkMovementMethod.getInstance());

            Date yDate;
            try {
                yDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'").parse(feedList.get(i).getPublishedAt());
                viewHolder.feedDate.setText(df.format(yDate));
            } catch (ParseException e) {
                yDate = null;
                e.printStackTrace();
            }

            viewHolder.feedTitle.setText("PMOfficeIndia");
            viewHolder.feedType.setText("Youtube");

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, YouTubePlayerActivity.class);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, feedList.get(i).getId());
                    intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO_START_WITH_LANDSCAPE);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                }
            });

        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return
                feedList != null ? feedList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView content;
        protected TextView feedTitle, feedDate, feedType;
        protected ImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            content = (TextView) itemView.findViewById(R.id.feed_content);
            feedTitle = (TextView) itemView.findViewById(R.id.feed_title);
            feedDate = (TextView) itemView.findViewById(R.id.feed_date);
            feedType = (TextView) itemView.findViewById(R.id.feed_type);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

        }


    }


    public void updateDataSet(List<Content> feeds, RecyclerView recyclerView) {
        this.feedList = feeds;
        notifyItemRangeChanged(0, feedList.size());
        recyclerView.invalidateItemDecorations();
        recyclerView.invalidate();

    }

    public void addDataSet(List<Content> feeds, RecyclerView recyclerView) {
        this.feedList.addAll(feeds);
        // notifyDataSetChanged();
        notifyItemRangeChanged(0, feedList.size());
        recyclerView.invalidateItemDecorations();
        recyclerView.invalidate();


    }
}
