package com.sanskrit.pmo.Adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Utils.ProportionalImageView;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.utils.DateUtil;
import com.sanskrit.pmo.utils.NavigationUtils;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private AppCompatActivity context;
    private int lastPosition = -1;
    private List<NewsFeed> objects;

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
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    try {
                        NavigationUtils.navigateToNewsDetail(FeedAdapter.this.context, (NewsFeed) FeedAdapter.this.objects.get(ViewHolder.this.getAdapterPosition()), false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void clearAnimation() {
            this.container.clearAnimation();
        }
    }

    public FeedAdapter(AppCompatActivity context, List<NewsFeed> feeds) {
        this.context = context;
        this.objects = feeds;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news_header, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        try {
            NewsFeed feed = (NewsFeed) this.objects.get(i);
            Log.e("TAG", "onBindViewHolder: " + feed.mDate);
            viewHolder.title.setText(feed.mTitle);
            if (viewHolder.excerpt != null) {
                viewHolder.excerpt.setText(feed.mExcerpt);
            }
            if (!(viewHolder.image == null || feed.mImage == null || feed.mImage.equals(""))) {
                Picasso.with(this.context).load(feed.mImage).into(viewHolder.image);
            }
            if (feed.mDate != null) {
                viewHolder.date.setText(DateUtil.dateToString(DateUtil.stringToDate(feed.mDate)));
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

    public void updateDataSet(List<NewsFeed> feeds) {
        this.objects = feeds;
        notifyDataSetChanged();
    }

    public void addDataSet(List<NewsFeed> feeds) {
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

    private void setAnimation(View viewToAnimate, int position) {
        if (position > this.lastPosition) {
            viewToAnimate.startAnimation(AnimationUtils.loadAnimation(this.context, 17432578));
            this.lastPosition = position;
        }
    }
}