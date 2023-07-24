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

import com.sanskrit.pmo.Activities.GalleryPagerActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.gallery.ImageGallerySubModel;
import com.sanskrit.pmo.utils.Constants;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class GalleryImagesAdapter extends RecyclerView.Adapter<GalleryImagesAdapter.ViewHolder> {
    private Activity context;
    private List<ImageGallerySubModel> gallery;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView content;
        protected ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            this.content = (TextView) itemView.findViewById(R.id.content);
            this.image = (ImageView) itemView.findViewById(R.id.gallery_image);
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(GalleryImagesAdapter.this.context, GalleryPagerActivity.class);
                    intent.putExtra("image_list", Parcels.wrap(GalleryImagesAdapter.this.gallery));
                    intent.putExtra(Constants.POSITION, ViewHolder.this.getAdapterPosition());
                    GalleryImagesAdapter.this.context.startActivity(intent);
                }
            });
        }
    }

    public GalleryImagesAdapter(Activity context, List<ImageGallerySubModel> feeds) {
        this.context = context;
        this.gallery = feeds;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery_images, null));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (((ImageGallerySubModel) this.gallery.get(i)).mUrl != null && !((ImageGallerySubModel) this.gallery.get(i)).mUrl.equals("")) {
            Picasso.with(this.context).load(((ImageGallerySubModel) this.gallery.get(i)).mUrl).placeholder((int) R.drawable.ic_image_black_36dp).error((int) R.drawable.ic_image_black_36dp).into(viewHolder.image);
        }
    }

    public int getItemCount() {
        return this.gallery.size();
    }

    public void updateDataSet(List<ImageGallerySubModel> gallery) {
        this.gallery = gallery;
        notifyDataSetChanged();
    }
}
