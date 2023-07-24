package com.sanskrit.pmo.Adapters;

import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.sanskrit.pmo.Activities.ViewImageActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.ImageObject;
import com.sanskrit.pmo.network.mygov.models.MediaObject;
import com.sanskrit.pmo.player.youtube.Orientation;
import com.sanskrit.pmo.player.youtube.Quality;
import com.sanskrit.pmo.player.youtube.YouTubePlayerActivity;
import com.sanskrit.pmo.player.youtube.YouTubeThumbnail;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {
    private Context context;
    private List<MediaObject> imageList;
    private boolean vertical = false;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mediaImage;
        protected ImageView videoOverlay;
        protected View videoOverlayView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mediaImage = (ImageView) itemView.findViewById(R.id.media_image);
            this.videoOverlay = (ImageView) itemView.findViewById(R.id.video_overlay);
            this.videoOverlayView = itemView.findViewById(R.id.video_overlay_view);
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (((MediaObject) MediaAdapter.this.imageList.get(ViewHolder.this.getAdapterPosition())).type.equals("video")) {
                        try {
                            MediaAdapter.this.startYoutubeActivity(((MediaObject) MediaAdapter.this.imageList.get(ViewHolder.this.getAdapterPosition())).videoId);
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    MediaObject mediaObject = (MediaObject) MediaAdapter.this.imageList.get(ViewHolder.this.getAdapterPosition());
                    Intent intent = new Intent(MediaAdapter.this.context, ViewImageActivity.class);
                    ImageObject imageObject = new ImageObject();
                    imageObject.setUrl(mediaObject.imageurl);
                    imageObject.setId(mediaObject.imageId);
                    imageObject.setTitle(mediaObject.title);
                    intent.putExtra("image", Parcels.wrap(imageObject));
                    Log.e("lol", mediaObject.imageurl);
                    MediaAdapter.this.context.startActivity(intent);
                }
            });
        }
    }

    public MediaAdapter(Context context, List<MediaObject> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    public MediaAdapter(Context context, List<MediaObject> imageList, boolean vertical) {
        this.context = context;
        this.imageList = imageList;
        this.vertical = vertical;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (this.vertical) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_trackrecord_media, viewGroup, false));
        }
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_media_images, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        String type = ((MediaObject) this.imageList.get(i)).type;
        int obj = 0;
        switch (type.hashCode()) {
            case 100313435:
                if (type.equals("image")) {
                    obj = 0;
                    break;
                }
                break;
            case 112202875:
                if (type.equals("video")) {
                    obj = 1;
                    break;
                }
                break;
        }
        switch (obj) {
            case 0:
                try {
                    viewHolder.videoOverlay.setVisibility(View.GONE);
                    viewHolder.videoOverlayView.setVisibility(View.GONE);
                    Picasso.with(this.context).load(((MediaObject) this.imageList.get(i)).imageurl).into(viewHolder.mediaImage);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            case 1:
                try {
                    viewHolder.videoOverlay.setVisibility(View.VISIBLE);
                    viewHolder.videoOverlayView.setVisibility(View.VISIBLE);
                    Picasso.with(this.context).load(YouTubeThumbnail.getUrlFromVideoId(((MediaObject) this.imageList.get(i)).videoId, Quality.HIGH)).into(viewHolder.mediaImage);
                    return;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }

    public int getItemCount() {
        return this.imageList.size();
    }

    private void startYoutubeActivity(String videoId) {
        Intent intent = new Intent(this.context, YouTubePlayerActivity.class);
        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, videoId);
        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, PlayerStyle.DEFAULT);
        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO_START_WITH_LANDSCAPE);
        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
        //intent.setFlags(VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
        this.context.startActivity(intent);
    }

    public void updateDataSet(List<MediaObject> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }
}
