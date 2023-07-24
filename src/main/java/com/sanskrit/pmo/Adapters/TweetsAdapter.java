package com.sanskrit.pmo.Adapters;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.R;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;

import java.util.List;

/**
 * Created by manoj on 22/12/17.
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private Context context;
    private List<Tweet> tweets;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    Tweet tweet;
    private boolean isFooterEnabled = true;
    private OnLoadMoreListener onLoadMoreListener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        CompactTweetView tweetView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tweetView = (CompactTweetView) itemView;
        }
    }

    public TweetsAdapter(Context context, List<Tweet> statuse, RecyclerView recyclerView) {
        this.context = context;
        this.tweets = statuse;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);


                    if (dy > 0) {
                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                        if (!loading && totalItemCount < (lastVisibleItem + visibleThreshold)) {
                            // End has been reached
                            // Do something
                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }
                            loading = true;
                        }


                    } else {


                        // ((AllConnectedMemberListActivity) mContext).progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }

    }


    public void setLoaded() {
        loading = false;
    }

    /*  @Override
      public int getItemViewType(int position) {
          return itemsList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
      }
  */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }


    public TweetsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {


        return new TweetsAdapter.ViewHolder(new CompactTweetView(this.context, tweet, R.style.tw__TweetLightWithActionsStyle));
    }


    public void onBindViewHolder(TweetsAdapter.ViewHolder viewHolder, int i) {


        viewHolder.tweetView.setTweet(tweets.get(i));
        // viewHolder.tweetView.setOnActionCallback();
    }

    public int getItemCount() {
        return this.tweets.size();
    }

    public void updateDataSet(List<Tweet> tweets) {
        this.tweets = tweets;
        notifyDataSetChanged();
    }

    public void addDataSet(List<Tweet> tweets) {
        this.tweets.addAll(tweets);
        notifyDataSetChanged();
    }
}
