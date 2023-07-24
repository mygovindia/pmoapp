package com.sanskrit.pmo.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Utils.ProportionalImageView;
import com.sanskrit.pmo.network.mygov.models.MediaCoverageDatailContent;
import com.sanskrit.pmo.utils.DateUtil;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MediaCoverageFeedAdapter extends RecyclerView.Adapter<MediaCoverageFeedAdapter.ViewHolder> {
    private AppCompatActivity context;
    private int lastPosition = -1;
    private List<MediaCoverageDatailContent> objects;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected View container;
        protected TextView date;
        protected TextView excerpt;
        protected ProportionalImageView image;
        protected TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.image = (ProportionalImageView) itemView.findViewById(R.id.image);
            this.excerpt = (TextView) itemView.findViewById(R.id.excerpt);
            this.container = itemView.findViewById(R.id.container);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(objects.get(ViewHolder.this.getAdapterPosition()).getMediaCoverURL()));
                    context.startActivity(browserIntent);

                    /*try {
                        NavigationUtils.navigateToMediaCoverageDetail(MediaCoverageFeedAdapter.this.context, (MediaCoverageDatailContent) MediaCoverageFeedAdapter.this.objects.get(ViewHolder.this.getAdapterPosition()), false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                }
            });
        }

        public void clearAnimation() {
            this.container.clearAnimation();
        }
    }

    public MediaCoverageFeedAdapter(AppCompatActivity context, List<MediaCoverageDatailContent> feeds) {
        this.context = context;
        this.objects = feeds;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_media_coverage_header, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        try {
            MediaCoverageDatailContent mediaCoverageModel = (MediaCoverageDatailContent) this.objects.get(i);
            viewHolder.title.setText(mediaCoverageModel.getTitle());
            if (viewHolder.excerpt != null) {
                viewHolder.excerpt.setText(mediaCoverageModel.getExcerpt());
            }
            if (!(viewHolder.image == null || mediaCoverageModel.getFeature_image() == null || mediaCoverageModel.getFeature_image().equals(""))) {
                Picasso.with(this.context).load(mediaCoverageModel.getFeature_image()).into(viewHolder.image);
            }
            if (mediaCoverageModel.getDate() != null) {
                viewHolder.date.setText(DateUtil.dateToString(DateUtil.stringToDate(mediaCoverageModel.getDate())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItemCount() {
        return this.objects.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public void updateDataSet(List<MediaCoverageDatailContent> mediaCoverageModels) {
        this.objects = mediaCoverageModels;
        notifyDataSetChanged();
    }

    public void addDataSet(List<MediaCoverageDatailContent> feeds) {
        this.objects.addAll(feeds);
        notifyDataSetChanged();
    }

    public void clearDataSet() {
        this.objects.clear();
        notifyDataSetChanged();
    }

    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.clearAnimation();
    }

    @SuppressLint("ResourceType")
    private void setAnimation(View viewToAnimate, int position) {
        if (position > this.lastPosition) {
            viewToAnimate.startAnimation(AnimationUtils.loadAnimation(this.context, 17432578));
            this.lastPosition = position;
        }
    }
}
