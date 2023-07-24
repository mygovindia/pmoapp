package com.sanskrit.pmo.Adapters;

import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.Activities.TrackRecordDetailActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.SearchModel;
import com.sanskrit.pmo.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by manoj on 27/3/18.
 */

public class SearchTrackRecordAdapter extends RecyclerView.Adapter<SearchTrackRecordAdapter.ViewHolder> {
    private AppCompatActivity context;
    private List<SearchModel> objects;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView newsTitle;
        protected ImageView thumbnail;
        protected TextView trackDate;

        public ViewHolder(View itemView) {
            super(itemView);
            this.newsTitle = (TextView) itemView.findViewById(R.id.news_title);
            this.trackDate = (TextView) itemView.findViewById(R.id.track_date);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.feed_picture);

        }
    }

    public SearchTrackRecordAdapter(AppCompatActivity context, List<SearchModel> records) {
        this.context = context;
        this.objects = records;
    }

    public SearchTrackRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SearchTrackRecordAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_track_record, viewGroup, false));
    }

    public void onBindViewHolder(final SearchTrackRecordAdapter.ViewHolder viewHolder, final int i) {
        try {
            viewHolder.trackDate.setText(DateUtil.dateToString(DateUtil.stringToDate(((SearchModel) this.objects.get(i)).getDate())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.newsTitle.setText(((SearchModel) this.objects.get(i)).getTitle());
        Picasso.with(this.context).load(((SearchModel) this.objects.get(i)).getFeature_image()).into(viewHolder.thumbnail);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SearchTrackRecordAdapter.this.context, TrackRecordDetailActivity.class);
                intent.putExtra("title", objects.get(i).getTitle());
                intent.putExtra("content", objects.get(i).getContent());
                intent.putExtra("image", objects.get(i).getFeature_image());
                intent.putExtra("date", viewHolder.trackDate.getText().toString());
                intent.putExtra("id", objects.get(i).getId());
                SearchTrackRecordAdapter.this.context.startActivity(intent);
            }
        });


    }

    public int getItemCount() {
        return this.objects.size();
    }

    public void updateDateSet(List<SearchModel> posts) {
        this.objects = posts;
        notifyDataSetChanged();
    }

    public void addDataSet(List<SearchModel> posts) {
        this.objects.addAll(posts);
        notifyDataSetChanged();
    }


}
