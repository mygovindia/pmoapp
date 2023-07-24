package com.sanskrit.pmo.Adapters;

import android.app.Activity;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.Activities.GalleryImagesActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.gallery.ImageGalleryModel;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private Activity context;
    private List<ImageGalleryModel> gallery;

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
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(GalleryAdapter.this.context, GalleryImagesActivity.class);
                        intent.putExtra("gallery", Parcels.wrap(GalleryAdapter.this.gallery.get(ViewHolder.this.getAdapterPosition())));
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                       /* if (Utils.isLollipop()) {
                            GalleryAdapter.this.context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GalleryAdapter.this.context, new Pair[]{Pair.create(ViewHolder.this.image, "gallery_image")}).toBundle());
                            return;
                        }*/
                        GalleryAdapter.this.context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public GalleryAdapter(Activity context, List<ImageGalleryModel> feeds) {
        this.context = context;
        this.gallery = feeds;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gallery, null));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (!(((ImageGalleryModel) this.gallery.get(i)).mFeatureImage == null || ((ImageGalleryModel) this.gallery.get(i)).mFeatureImage.equals(""))) {
            Picasso.with(this.context).load(((ImageGalleryModel) this.gallery.get(i)).mFeatureImage).placeholder((int) R.drawable.ic_image_black_36dp).error((int) R.drawable.ic_image_black_36dp).into(viewHolder.image);
        }
        viewHolder.title.setText(((ImageGalleryModel) this.gallery.get(i)).mTitle);
        viewHolder.date.setText(((ImageGalleryModel) this.gallery.get(i)).mDate);
    }

    public int getItemCount() {
        return this.gallery.size();
    }

    public void updateDataSet(List<ImageGalleryModel> gallery) {
        this.gallery = gallery;
        notifyDataSetChanged();
    }

    public void addDataSet(List<ImageGalleryModel> gallery) {
        this.gallery.addAll(gallery);
        notifyDataSetChanged();
    }

    public static int getOpaqueColor(@ColorInt int paramInt) {
        return ViewCompat.MEASURED_STATE_MASK | paramInt;
    }
}
