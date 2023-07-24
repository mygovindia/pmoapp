package com.sanskrit.pmo.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.Activities.TrackRecordDetailActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.network.mygov.models.SearchModel;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.DateUtil;
import com.sanskrit.pmo.utils.NavigationUtils;

import java.util.List;

/**
 * Created by manoj on 15/3/18.
 */

public class SearchAdapter_backup extends RecyclerView.Adapter<SearchAdapter_backup.ItemHolder> {
    private Activity context;
    private List<SearchModel> results;

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView content = ((TextView) this.itemView.findViewById(R.id.feed_content));
        protected TextView date = ((TextView) this.itemView.findViewById(R.id.feed_date));
        protected ImageView image = ((ImageView) this.itemView.findViewById(R.id.image));
        protected TextView title = ((TextView) this.itemView.findViewById(R.id.feed_title));
        protected TextView type = ((TextView) this.itemView.findViewById(R.id.feed_type));

        public ItemHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }

        public void onClick(View v) {
            SearchModel model = (SearchModel) SearchAdapter_backup.this.results.get(getAdapterPosition());
            String type = model.getType();
            if (type.equals(Constants.SEARCH_TYPE_NEWS)) {
                NewsFeed feed = new NewsFeed();
                feed.mTitle = model.getTitle();
                feed.mId = model.getId();
                feed.mCOntent = model.getContent();
                feed.mDate = model.getDate();

                NavigationUtils.navigateToNewsDetail(SearchAdapter_backup.this.context, feed, true);

            } else if (type.equals(Constants.SEARCH_TYPE_TARCK_RECORD)) {
                Intent intent = new Intent(SearchAdapter_backup.this.context, TrackRecordDetailActivity.class);
                intent.putExtra("title", model.getTitle());
                intent.putExtra("content", model.getContent());
                intent.putExtra("date", this.date.getText().toString());
                SearchAdapter_backup.this.context.startActivity(intent);
            }
        }
    }

    public SearchAdapter_backup(Activity context, List<SearchModel> list) {
        this.context = context;
        this.results = list;
    }

    public SearchAdapter_backup.ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        switch (viewType) {
            case 0:
            case 1:
            case 2:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_simple, viewGroup, false);
                break;
            default:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mkb_video, null);
                break;
        }
        return new SearchAdapter_backup.ItemHolder(v);
    }

    public void onBindViewHolder(SearchAdapter_backup.ItemHolder viewHolder, int i) {
        SearchModel model = (SearchModel) this.results.get(i);
        switch (getItemViewType(i)) {
            case 0:
                viewHolder.title.setText(model.getTitle());
                viewHolder.type.setText(this.context.getString(R.string.news));
                viewHolder.title.setMaxLines(2);
                viewHolder.content.setMaxLines(3);
                viewHolder.content.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.type.setBackgroundColor(this.context.getResources().getColor(R.color.pocket_color_2));
                if (viewHolder.content != null) {
                    viewHolder.content.setText(Html.fromHtml(model.getContent()));
                }
                if (model.getDate() != null) {
                    viewHolder.date.setText(DateUtil.dateToString(DateUtil.stringToDate(model.getDate())));
                    return;
                }
                return;
            case 1:
                viewHolder.type.setText(this.context.getString(R.string.quotes));
                viewHolder.type.setBackgroundColor(this.context.getResources().getColor(R.color.pocket_color_4));
                viewHolder.content.setVisibility(View.GONE);
                viewHolder.date.setVisibility(View.GONE);
                try {
                    TypedValue typedValue = new TypedValue();
                    this.context.getTheme().resolveAttribute(R.color.colorAccent, typedValue, true);
                    int color = typedValue.data;
                    SpannableString ss1 = new SpannableString("\" " + model.getContent() + " \"");
                    ss1.setSpan(new ForegroundColorSpan(color), 0, 1, 0);
                    ss1.setSpan(new ForegroundColorSpan(color), ss1.length() - 2, ss1.length(), 0);
                    viewHolder.title.setText(ss1);
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    viewHolder.content.setText("\" " + model.getContent() + " \"");
                    return;
                }
            case 2:
                viewHolder.title.setText(model.getTitle());
                viewHolder.type.setText(this.context.getString(R.string.track_record_menu));
                viewHolder.title.setMaxLines(2);
                viewHolder.content.setVisibility(View.GONE);
                viewHolder.type.setBackgroundColor(this.context.getResources().getColor(R.color.pocket_color_1));
                if (model.getDate() != null) {
                    viewHolder.date.setText(DateUtil.dateToString(DateUtil.stringToDate(model.getDate())));
                    return;
                }
                return;
            default:
                return;
        }
    }

    public int getItemCount() {
        return results != null ? results.size() : 0;
    }

    public int getItemViewType(int position) {
        String type = ((SearchModel) this.results.get(position)).getType();
        if (type.equals(Constants.SEARCH_TYPE_NEWS)) {
            return 0;
        } else if (type.equals(Constants.SEARCH_TYPE_TARCK_RECORD)) {
            return 2;
        } else if (type.equals(Constants.SEARCH_TYPE_QUOTES)) {
            return 1;
        } else {
            return 0;
        }

    }

    public void updateDataSet(List<SearchModel> results) {
        this.results = results;
        // notifyDataSetChanged();
    }

    public void addDataSet(List<SearchModel> results) {
        this.results.addAll(results);
        //notifyDataSetChanged();
    }
}
