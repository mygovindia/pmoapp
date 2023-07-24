package com.sanskrit.pmo.Adapters;

import android.app.Activity;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.Activities.ViewImageActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.ImageObject;
import com.sanskrit.pmo.network.mygov.models.SearchModel;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by manoj on 28/3/18.
 */

public class SearchGalleryAdapter extends RecyclerView.Adapter<SearchGalleryAdapter.ViewHolder> {
    private Activity context;
    private List<SearchModel> gallery;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView date;
        protected View footer;
        protected ImageView image;
        protected TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.gallery_title);
            this.date = (TextView) itemView.findViewById(R.id.gallery_date);
            this.image = (ImageView) itemView.findViewById(R.id.gallery_image);
            this.footer = itemView.findViewById(R.id.footer);

        }
    }


    public SearchGalleryAdapter(Activity context, List<SearchModel> feeds) {
        this.context = context;
        this.gallery = feeds;
    }

    public SearchGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SearchGalleryAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery, null));
    }

    public void onBindViewHolder(SearchGalleryAdapter.ViewHolder viewHolder, final int i) {
        if (!((gallery.get(i)).getFeature_image() == null || (gallery.get(i)).getFeature_image().equals(""))) {
            Picasso.with(this.context).load(gallery.get(i).getFeature_image()).placeholder((int) R.drawable.ic_image_black_36dp).error((int) R.drawable.ic_image_black_36dp).into(viewHolder.image);
        }
        viewHolder.title.setText(gallery.get(i).getTitle());
        viewHolder.date.setText(gallery.get(i).getDate());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(context, ViewImageActivity.class);
                ImageObject imageObject = new ImageObject();
                imageObject.setUrl(gallery.get(i).getFeature_image());
                imageObject.setTitle(gallery.get(i).getTitle());
                imageObject.setId(String.valueOf(gallery.get(i).getId()));
                intent.putExtra("image", Parcels.wrap(imageObject));
                context.startActivity(intent);

            }
        });
    }

    public int getItemCount() {
        return this.gallery.size();
    }

    public void updateDataSet(List<SearchModel> gallery) {
        this.gallery = gallery;
        notifyDataSetChanged();
    }

    public void addDataSet(List<SearchModel> gallery) {
        this.gallery.addAll(gallery);
        notifyDataSetChanged();
    }

  /*  public void clearAdapter()
    {
        gallery.clear();
        notifyDataSetChanged();
    }*/

    public static int getOpaqueColor(@ColorInt int paramInt) {
        return ViewCompat.MEASURED_STATE_MASK | paramInt;
    }
}
