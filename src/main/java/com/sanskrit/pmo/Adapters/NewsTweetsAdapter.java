package com.sanskrit.pmo.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.R;

import java.util.List;

public class NewsTweetsAdapter extends RecyclerView.Adapter<NewsTweetsAdapter.ViewHolder> {
    private AppCompatActivity context;
    private List<String> objects;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            this.content = (TextView) itemView.findViewById(R.id.content);
        }
    }

    public NewsTweetsAdapter(AppCompatActivity context, List<String> objects) {
        this.context = context;
        this.objects = objects;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news_tweets, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        try {
            viewHolder.content.setText((CharSequence) this.objects.get(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItemCount() {
        return this.objects.size();
    }

    public void updateDateSet(List<String> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }
}
