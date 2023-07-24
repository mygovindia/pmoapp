package com.sanskrit.pmo.Adapters;

import android.app.Activity;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.internal.ServerProtocol;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.server.models.MkbVideo;
import com.sanskrit.pmo.player.youtube.Orientation;
import com.sanskrit.pmo.player.youtube.Quality;
import com.sanskrit.pmo.player.youtube.YouTubePlayerActivity;
import com.sanskrit.pmo.player.youtube.YouTubeThumbnail;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MKBVideoAdapter extends RecyclerView.Adapter<MKBVideoAdapter.ItemHolder> {
    private List<MkbVideo> arraylist;
    private Activity mContext;

    public class ItemHolder extends RecyclerView.ViewHolder implements OnClickListener {
        // protected TextView episodeNumber = ((TextView) this.itemView.findViewById(R.id.episode_number));
        protected ImageView thumbnail = ((ImageView) this.itemView.findViewById(R.id.thumbnail));
        protected TextView trackDate = ((TextView) this.itemView.findViewById(R.id.track_date));
        protected TextView trackDuration = ((TextView) this.itemView.findViewById(R.id.track_duration));
        protected TextView trackName = ((TextView) this.itemView.findViewById(R.id.track_name));
        protected TextView type = ((TextView) this.itemView.findViewById(R.id.type));

        public ItemHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }

        public void onClick(View v) {
            MKBVideoAdapter.this.startYoutubeActivity(((MkbVideo) MKBVideoAdapter.this.arraylist.get(getAdapterPosition())).getVideo_id());
        }
    }


    public MKBVideoAdapter(Activity context, List<MkbVideo> arraylist) {
        this.mContext = context;
        this.arraylist = arraylist;
    }

    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mkb_video, viewGroup, false));
    }

    public void onBindViewHolder(ItemHolder itemHolder, int i) {
        MkbVideo object = (MkbVideo) this.arraylist.get(i);
        itemHolder.trackName.setText(object.getName());
        itemHolder.trackDate.setText(object.getDate());
        Picasso.with(this.mContext).load(YouTubeThumbnail.getUrlFromVideoId(object.getVideo_id(), Quality.FIRST)).into(itemHolder.thumbnail);
        if (object.isLive().equals(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE)) {
            itemHolder.type.setText("Live");
        } else {
            itemHolder.type.setVisibility(View.GONE);
        }
    }

    public int getItemCount() {
        return this.arraylist != null ? this.arraylist.size() : 0;
    }

    private void startYoutubeActivity(String videoId) {
        Intent intent = new Intent(this.mContext, YouTubePlayerActivity.class);
        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, videoId);
        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, PlayerStyle.DEFAULT);
        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO_START_WITH_LANDSCAPE);
        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
        //  intent.setFlags(VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
        this.mContext.startActivity(intent);
    }

    public void updateDataSet(List<MkbVideo> list) {
        this.arraylist = list;
        notifyDataSetChanged();
    }
}
