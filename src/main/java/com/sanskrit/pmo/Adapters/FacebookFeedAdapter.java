package com.sanskrit.pmo.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Utils.ProportionalImageView;
import com.sanskrit.pmo.network.server.models.FacebookFeed;
import com.sanskrit.pmo.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FacebookFeedAdapter extends RecyclerView.Adapter<FacebookFeedAdapter.ViewHolder> {
    private AppCompatActivity context;
    private List<FacebookFeed> objects;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProportionalImageView feedImageView;
        TextView name;
        ImageView profilePic;
        TextView statusMsg;
        TextView timeStamp;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.timeStamp = (TextView) itemView.findViewById(R.id.timestamp);
            this.statusMsg = (TextView) itemView.findViewById(R.id.txtStatusMsg);
            this.profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            this.feedImageView = (ProportionalImageView) itemView.findViewById(R.id.feedImage1);
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    try {

                        String id = ((FacebookFeed) FacebookFeedAdapter.this.objects.get(ViewHolder.this.getAdapterPosition())).getId();
                        if (id.contains("_")) {
                            id = id.substring(id.indexOf("_") + 1, id.length());
                        }

                        Uri url = Uri.parse("https://www.facebook.com/PMOIndia/posts/" + id);
                        //Uri url = Uri.parse("fb://feed/PMOIndia/posts/" + id);
                        Log.e("error", "yes" + url);

                        //FacebookFeedAdapter.this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/PMOIndia/posts/" + id)));
                        FacebookFeedAdapter.this.context.startActivity(new Intent("android.intent.action.VIEW", url));
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Facebook not installed", Toast.LENGTH_SHORT).show();
                        String id = ((FacebookFeed) FacebookFeedAdapter.this.objects.get(ViewHolder.this.getAdapterPosition())).getId();
                        if (id.contains("_")) {
                            id = id.substring(id.indexOf("_") + 1, id.length());
                        }
                        FacebookFeedAdapter.this.context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/PMOIndia/posts/" + id)));
                    }
                }
            });
        }
    }

    public FacebookFeedAdapter(AppCompatActivity context, List<FacebookFeed> feeds) {
        this.context = context;
        this.objects = feeds;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_facebook_feed, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        try {
            FacebookFeed feed = (FacebookFeed) this.objects.get(i);
            viewHolder.name.setText("PMO India");
            viewHolder.timeStamp.setText(DateUtil.dateToPastTenseString(DateUtil.stringToDate(feed.getCreated_time())));
            if (TextUtils.isEmpty(feed.getMessage())) {
                viewHolder.statusMsg.setVisibility(View.GONE);
            } else {
                viewHolder.statusMsg.setText(feed.getMessage());
                viewHolder.statusMsg.setVisibility(View.VISIBLE);
            }
            if (!(feed.getProfile_picture() == null || feed.getProfile_picture().equals(""))) {
                Picasso.with(this.context).load(feed.getProfile_picture()).into(viewHolder.profilePic);
            }
            if (feed.getFb_post_image() == null || feed.getFb_post_image().equals("")) {
                viewHolder.feedImageView.setVisibility(View.GONE);
                return;
            }
            Picasso.with(this.context).load(feed.getFb_post_image()).into(viewHolder.feedImageView);
            viewHolder.feedImageView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItemCount() {
        return this.objects.size();
    }

    public int getItemViewType(int position) {
        return this.objects.size();
    }

    public void updateDataSet(List<FacebookFeed> feeds) {
        this.objects = feeds;
        notifyDataSetChanged();
    }

    public void addDataSet(List<FacebookFeed> feeds) {
        this.objects.addAll(feeds);
        notifyDataSetChanged();
    }

    public void clearDataSet() {
        this.objects.clear();
        notifyDataSetChanged();
    }
}
