package com.sanskrit.pmo.Adapters;

import android.app.Activity;
import android.content.Intent;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.Activities.NewsReaderActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.network.mygov.models.SearchModel;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by manoj on 27/3/18.
 */

public class SearchFormerPMAdapter extends RecyclerView.Adapter<SearchFormerPMAdapter.ItemHolder> {
    private List<SearchModel> arraylist;

    private Activity mContext;


    public class ItemHolder extends RecyclerView.ViewHolder {
        protected TextView read = ((TextView) this.itemView.findViewById(R.id.read_more));
        protected ImageView thubmnail = ((ImageView) this.itemView.findViewById(R.id.thumbnail));
        protected TextView tvDate = ((TextView) this.itemView.findViewById(R.id.tv_date));
        protected TextView tvName = ((TextView) this.itemView.findViewById(R.id.tv_name));
        protected TextView tvDesc = ((TextView) this.itemView.findViewById(R.id.tv_desc));

        public ItemHolder(View view) {
            super(view);


        }

    }


    public SearchFormerPMAdapter(Activity context, List<SearchModel> arraylist) {
        this.mContext = context;
        this.arraylist = arraylist;

    }

    public SearchFormerPMAdapter.ItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SearchFormerPMAdapter.ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_former_pm, viewGroup, false));
    }

    public void onBindViewHolder(SearchFormerPMAdapter.ItemHolder itemHolder, int i) {

        final SearchModel object = (SearchModel) this.arraylist.get(i);
        try {
            itemHolder.tvName.setText(object.getTitle());
            itemHolder.tvDate.setText(object.getTenure());
            //itemHolder.tvDesc.setText(object.mCOntent);
            itemHolder.tvDesc.setMaxLines(3);
            itemHolder.tvDesc.setEllipsize(TextUtils.TruncateAt.END);
            itemHolder.tvDesc.setText(Html.fromHtml(object.getContent()));

            if (object.getFeature_image() != null && !object.getFeature_image().equals("")) {
                Picasso.with(this.mContext).load(object.getFeature_image()).into(itemHolder.thubmnail);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }


        if (itemHolder.read != null) {
            itemHolder.read.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        NewsFeed feed = new NewsFeed();
                        feed.mCOntent = object.getContent();
                        feed.mDate = object.getTenure();
                        feed.mTitle = object.getTitle();
                        Intent intent = new Intent(SearchFormerPMAdapter.this.mContext, NewsReaderActivity.class);
                        intent.putExtra("newsitem", Parcels.wrap(feed));
                        intent.putExtra("showShare", false);
                        intent.putExtra("isFrom", true);
                        SearchFormerPMAdapter.this.mContext.startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public int getItemCount() {
        return this.arraylist.size();
    }


    public void updateDateSet(List<SearchModel> posts) {
        this.arraylist = posts;
        notifyDataSetChanged();
    }

    public void addDataSet(List<SearchModel> posts) {
        this.arraylist.addAll(posts);
        notifyDataSetChanged();
    }
/*
    public void clearAdapter() {

        arraylist.clear();
        notifyDataSetChanged();

    }*/


}
