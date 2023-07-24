package com.sanskrit.pmo.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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

import com.google.android.youtube.player.YouTubePlayer;
import com.sanskrit.pmo.Activities.TrackRecordDetailActivity;
import com.sanskrit.pmo.Activities.ViewImageActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.ImageObject;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.network.mygov.models.SearchModel;
import com.sanskrit.pmo.player.youtube.Orientation;
import com.sanskrit.pmo.player.youtube.YouTubePlayerActivity;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.DateUtil;
import com.sanskrit.pmo.utils.NavigationUtils;

import org.parceler.Parcels;

import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemHolder> {

    private Activity context;
    private List<SearchModel> results;

    public SearchAdapter(Activity context, List<SearchModel> list) {
        this.context = context;
        this.results = list;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        switch (viewType) {
            case 0:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_simple, viewGroup, false);
                break;

            case 1:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_simple, viewGroup, false);
                break;

            case 2:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_simple, viewGroup, false);
                break;

            case 3:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_simple, viewGroup, false);
                break;
            case 4:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_simple, viewGroup, false);
                break;

            default:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_simple, viewGroup, false);
                break;


        }
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemHolder viewHolder, int i) {
        SearchModel model = results.get(i);
        switch (getItemViewType(i)) {
            case 0:
                viewHolder.title.setText(model.getTitle());
                viewHolder.type.setText(context.getString(R.string.news));
                viewHolder.title.setMaxLines(2);
                viewHolder.content.setMaxLines(3);
                viewHolder.content.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.type.setBackgroundColor(context.getResources().getColor(R.color.pocket_color_2));
                if (viewHolder.content != null) {
                    viewHolder.content.setText(Html.fromHtml(model.getContent()));
                }
                if (model.getDate() != null)
                    viewHolder.date.setText(DateUtil.dateToString(DateUtil.stringToDate(model.getDate())));

                break;
            case 1:
                viewHolder.type.setText(context.getString(R.string.quotes));
                viewHolder.type.setBackgroundColor(context.getResources().getColor(R.color.pocket_color_4));
                viewHolder.content.setVisibility(View.VISIBLE);
                viewHolder.date.setVisibility(View.VISIBLE);
                viewHolder.title.setText(model.getTitle());
                try {
                    TypedValue typedValue = new TypedValue();
                    Resources.Theme theme = context.getTheme();
                    theme.resolveAttribute(R.attr.accentColor, typedValue, true);
                    int color = typedValue.data;

                    SpannableString ss1 = new SpannableString("\" " + model.getContent() + " \"");
                    ss1.setSpan(new ForegroundColorSpan(color), 0, 1, 0);
                    ss1.setSpan(new ForegroundColorSpan(color), ss1.length() - 2, ss1.length(), 0);
                    viewHolder.title.setText(ss1);
                } catch (Exception e) {
                    e.printStackTrace();
                    viewHolder.content.setText("\" " + model.getContent() + " \"");
                }

                break;
            case 2:
                viewHolder.title.setText(model.getTitle());
                viewHolder.type.setText(context.getString(R.string.track_record_menu));
                viewHolder.title.setMaxLines(2);
                viewHolder.content.setVisibility(View.GONE);
                viewHolder.type.setBackgroundColor(context.getResources().getColor(R.color.color_twitter));
                if (model.getDate() != null)
                    viewHolder.date.setText(DateUtil.dateToString(DateUtil.stringToDate(model.getDate())));

                break;
            case 3:
                viewHolder.title.setText(model.getTitle());
                viewHolder.type.setText(context.getString(R.string.youtube));
                viewHolder.title.setMaxLines(2);
                viewHolder.content.setVisibility(View.GONE);
                viewHolder.type.setBackgroundColor(context.getResources().getColor(R.color.pocket_color_3));
                if (model.getDate() != null)
                    viewHolder.date.setText(DateUtil.dateToString(DateUtil.stringToDate(model.getDate())));

                break;

            case 4:
                viewHolder.title.setText(model.getTitle());
                viewHolder.type.setText("Pages");
                viewHolder.title.setMaxLines(2);
                viewHolder.content.setVisibility(View.GONE);
                viewHolder.type.setBackgroundColor(context.getResources().getColor(R.color.pink));
                if (model.getDate() != null)
                    viewHolder.date.setText(DateUtil.dateToString(DateUtil.stringToDate(model.getDate())));

                break;
            default:
                viewHolder.title.setText(model.getTitle());
                viewHolder.type.setText(context.getString(R.string.news));
                viewHolder.title.setMaxLines(2);
                viewHolder.content.setMaxLines(3);
                viewHolder.content.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.type.setBackgroundColor(context.getResources().getColor(R.color.pocket_color_2));
           /* if (viewHolder.content != null) {
                viewHolder.content.setText(Html.fromHtml(model.getContent()));
            }*/
                if (model.getDate() != null)
                    viewHolder.date.setText(DateUtil.dateToString(DateUtil.stringToDate(model.getDate())));

                break;
        }
    }


    @Override
    public int getItemCount() {
        return results.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView title, date, content, type;
        protected ImageView image;

        public ItemHolder(View view) {
            super(view);

            title = (TextView) itemView.findViewById(R.id.feed_title);
            date = (TextView) itemView.findViewById(R.id.feed_date);
            image = (ImageView) itemView.findViewById(R.id.image);
            content = (TextView) itemView.findViewById(R.id.feed_content);
            type = (TextView) itemView.findViewById(R.id.feed_type);


            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            SearchModel model = results.get(getAdapterPosition());
            switch (model.getType()) {
                case Constants.SEARCH_TYPE_NEWS:
                    NewsFeed feed = new NewsFeed();
                    feed.mTitle = model.getTitle();
                    feed.mId = model.getId();
                    feed.mCOntent = model.getContent();
                    feed.mDate = model.getDate();
                    feed.mImage = model.getFeature_image();
                    NavigationUtils.navigateToNewsDetail(SearchAdapter.this.context, feed, true);

                    break;
                case Constants.SEARCH_TYPE_TARCK_RECORD:
                    Intent intent = new Intent(context, TrackRecordDetailActivity.class);
                    intent.putExtra("title", model.getTitle());
                    intent.putExtra("content", model.getContent());
                    intent.putExtra("date", date.getText().toString());
                    context.startActivity(intent);
                    break;
                case Constants.SEARCH_TYPE_VIDEO:
                    startYoutubeActivity(model.getYoutubeId());
                    break;

                case Constants.SEARCH_TYPE_PAGE:
                    NewsFeed feed1 = new NewsFeed();
                    feed1.mTitle = model.getTitle();
                    feed1.mId = model.getId();
                    feed1.mCOntent = model.getContent();
                    feed1.mDate = model.getDate();
                    feed1.mImage = model.getFeature_image();
                    NavigationUtils.navigateToNewsDetail(SearchAdapter.this.context, feed1, true);

                    break;
                case Constants.SEARCH_TYPE_QUOTES:
                    NewsFeed feed2 = new NewsFeed();
                    try {
                        /* if (feed2.mImage != null && feed2.mImage.length() != 0)*/
                        //{
                        Intent intent1 = new Intent(context, ViewImageActivity.class);
                        ImageObject imageObject = new ImageObject();
                        imageObject.setUrl(feed2.mImage);
                        imageObject.setTitle(feed2.mTitle);
                        imageObject.setId(String.valueOf(feed2.mId));
                        intent1.putExtra("image", Parcels.wrap(imageObject));
                        intent1.putExtra("Quote", true);
                        context.startActivity(intent1);
                        //}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (results.get(position).getType()) {
            case Constants.SEARCH_TYPE_NEWS:
                return 0;
            case Constants.SEARCH_TYPE_QUOTES:
                return 1;
            case Constants.SEARCH_TYPE_TARCK_RECORD:
                return 2;
            case Constants.SEARCH_TYPE_VIDEO:
                return 3;

            case Constants.SEARCH_TYPE_PAGE:
                return 4;
            default:
                return 0;
        }
    }

    public void updateDataSet(List<SearchModel> results) {
        this.results = results;
        notifyDataSetChanged();
    }

    public void addDataSet(List<SearchModel> results) {
        this.results.addAll(results);
        notifyDataSetChanged();
    }


    private void startYoutubeActivity(String videoId) {
        Intent intent = new Intent(context, YouTubePlayerActivity.class);
        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, videoId);
        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);
        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO_START_WITH_LANDSCAPE);
        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
        //  intent.setFlags(VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
        this.context.startActivity(intent);
    }
}
