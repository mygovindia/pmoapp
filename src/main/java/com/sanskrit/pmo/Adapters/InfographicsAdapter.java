package com.sanskrit.pmo.Adapters;

import android.content.Intent;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.Activities.ViewImageActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.ImageObject;
import com.sanskrit.pmo.network.mygov.models.Post;
import com.sanskrit.pmo.utils.DateUtil;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

public class InfographicsAdapter extends RecyclerView.Adapter<InfographicsAdapter.ViewHolder> {
    private AppCompatActivity context;
    private List<Post> posts;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView date;
        protected ImageView image;
        protected TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.image = (ImageView) itemView.findViewById(R.id.image_infographics);

        }
    }

    public InfographicsAdapter(AppCompatActivity context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_infographics, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Post post = (Post) this.posts.get(i);
        try {
            viewHolder.title.setText(Html.fromHtml(post.title));
            if (post.date != null) {
                viewHolder.date.setText(DateUtil.dateToString(DateUtil.stringToDate(post.date)));
            }
            if (viewHolder.image != null && post.feature_image != null && post.feature_image.length() != 0) {
                Picasso.with(this.context).load(post.feature_image).into(viewHolder.image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewHolder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (post.feature_image != null && post.feature_image.length() != 0) {
                        Intent intent = new Intent(context, ViewImageActivity.class);
                        ImageObject imageObject = new ImageObject();
                        imageObject.setUrl(post.feature_image);
                        imageObject.setTitle(post.title);
                        imageObject.setId(String.valueOf(post.id));
                        intent.putExtra("image", Parcels.wrap(imageObject));
                        intent.putExtra("Infographics", true);
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public int getItemCount() {
        return posts != null ? posts.size() : 0;
    }

    public void updateDateSet(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public void addDataSet(List<Post> posts) {
        this.posts.addAll(posts);
        notifyDataSetChanged();
    }

    public void clearAdapter() {

        posts.clear();
        notifyDataSetChanged();

    }
}
