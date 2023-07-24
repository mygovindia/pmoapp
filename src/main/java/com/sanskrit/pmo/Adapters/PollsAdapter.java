package com.sanskrit.pmo.Adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanskrit.pmo.Activities.PollDetailActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.polls.PollImagesUnd;
import com.sanskrit.pmo.network.mygov.models.polls.PollModel;
import com.sanskrit.pmo.network.mygov.models.polls.PollTimeUnd;
import com.sanskrit.pmo.utils.DateUtil;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

import static com.sanskrit.pmo.Fragments.ShareFragment.LINE_SEPARATOR_UNIX;

import androidx.recyclerview.widget.RecyclerView;

public class PollsAdapter extends RecyclerView.Adapter<PollsAdapter.ViewHolder> {
    private Context context;
    private List<PollModel> objects;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected View container;
        protected TextView date;
        protected ImageView image;
        protected TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.image = (ImageView) itemView.findViewById(R.id.image);
            this.container = itemView.findViewById(R.id.container);
            itemView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(PollsAdapter.this.context, PollDetailActivity.class);
                    intent.putExtra("poll", Parcels.wrap(PollsAdapter.this.objects.get(ViewHolder.this.getAdapterPosition())));
                    PollsAdapter.this.context.startActivity(intent);
                }
            });
        }
    }

    public PollsAdapter(Context context, List<PollModel> pollModels) {
        this.context = context;
        this.objects = pollModels;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_poll_header, viewGroup, false));
    }


    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PollModel poll = (PollModel) this.objects.get(i);
        viewHolder.title.setText(poll.getTitle());
        String endText = this.context.getString(R.string.poll_ends);
        if (new Date().after(DateUtil.stringToDate(((PollTimeUnd) poll.getField_deadline().getUnd().get(0)).getValue()))) {
            endText = this.context.getString(R.string.poll_ends_past);
        }
        viewHolder.date.setText(this.context.getString(R.string.poll_started) + DateUtil.dateToPastTenseString(DateUtil.stringToDate(((PollTimeUnd) poll.getField_start_date().getUnd().get(0)).getValue())) + LINE_SEPARATOR_UNIX + endText + DateUtil.dateToPastTenseString(DateUtil.stringToDate(((PollTimeUnd) poll.getField_deadline().getUnd().get(0)).getValue())));
        if (viewHolder.image != null) {
            Picasso.with(this.context).load(((PollImagesUnd) poll.getField_theme_image().getUnd().get(0)).getFull_url()).into(viewHolder.image);
        }
    }

    public int getItemCount() {
        return this.objects.size();
    }

    public void updateDataSet(List<PollModel> pollModels) {
        this.objects = pollModels;
        notifyDataSetChanged();
    }

    public void addDataSet(List<PollModel> pollModels) {
        this.objects.addAll(pollModels);
        notifyDataSetChanged();
    }
}
