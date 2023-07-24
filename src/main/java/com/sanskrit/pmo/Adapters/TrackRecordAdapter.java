package com.sanskrit.pmo.Adapters;

import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.Activities.TrackRecordDetailActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.TrackRecord;
import com.sanskrit.pmo.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrackRecordAdapter extends RecyclerView.Adapter<TrackRecordAdapter.ViewHolder> {
    private AppCompatActivity context;
    private List<TrackRecord.TrackRecordModel> objects;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView newsTitle;
        protected ImageView thumbnail;
        protected TextView trackDate;

        public ViewHolder(View itemView) {
            super(itemView);
            this.newsTitle = (TextView) itemView.findViewById(R.id.news_title);
            this.trackDate = (TextView) itemView.findViewById(R.id.track_date);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.feed_picture);
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(TrackRecordAdapter.this.context, TrackRecordDetailActivity.class);
                    intent.putExtra("title", ((TrackRecord.TrackRecordModel) TrackRecordAdapter.this.objects.get(ViewHolder.this.getAdapterPosition())).mTitle);
                    intent.putExtra("content", ((TrackRecord.TrackRecordModel) TrackRecordAdapter.this.objects.get(ViewHolder.this.getAdapterPosition())).mContent);
                    intent.putExtra("image", ((TrackRecord.TrackRecordModel) TrackRecordAdapter.this.objects.get(ViewHolder.this.getAdapterPosition())).mImage);
                    intent.putExtra("date", ViewHolder.this.trackDate.getText().toString());
                    intent.putExtra("id", ((TrackRecord.TrackRecordModel) TrackRecordAdapter.this.objects.get(ViewHolder.this.getAdapterPosition())).mID);
                    TrackRecordAdapter.this.context.startActivity(intent);
                }
            });
        }
    }

    public TrackRecordAdapter(AppCompatActivity context, List<TrackRecord.TrackRecordModel> records) {
        this.context = context;
        this.objects = records;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_track_record, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        try {
            viewHolder.trackDate.setText(DateUtil.dateToString(DateUtil.stringToDate(((TrackRecord.TrackRecordModel) this.objects.get(i)).mDate)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.newsTitle.setText(((TrackRecord.TrackRecordModel) this.objects.get(i)).mTitle);
        Picasso.with(this.context).load(((TrackRecord.TrackRecordModel) this.objects.get(i)).mImage).into(viewHolder.thumbnail);
    }

    public int getItemCount() {
        return this.objects.size();
    }

    public void updateDataSet(List<TrackRecord.TrackRecordModel> records) {
        this.objects = records;
        notifyDataSetChanged();
    }


    public void addDataSet(List<TrackRecord.TrackRecordModel> records) {
        this.objects.addAll(records);
        notifyDataSetChanged();
    }
}
