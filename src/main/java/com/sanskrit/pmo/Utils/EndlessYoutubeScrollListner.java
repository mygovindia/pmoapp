package com.sanskrit.pmo.utils;



import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class EndlessYoutubeScrollListner extends RecyclerView.OnScrollListener {
    public int current_page = 1;
    public int firstVisibleItem;
    public boolean loading = true;
    public RecyclerView.LayoutManager mLayoutManager;
    public int previousTotal = 0;
    public int totalItemCount;
    public int visibleItemCount;
    public int visibleThreshold = 15;

    public abstract void onLoadMore(int i);

    public EndlessYoutubeScrollListner() {

    }

    public EndlessYoutubeScrollListner(RecyclerView.LayoutManager linearLayoutManager) {
        this.mLayoutManager = linearLayoutManager;
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        this.visibleItemCount = recyclerView.getChildCount();
        this.totalItemCount = this.mLayoutManager.getItemCount();
        if (this.mLayoutManager instanceof LinearLayoutManager) {
            this.firstVisibleItem = ((LinearLayoutManager) this.mLayoutManager).findFirstVisibleItemPosition();
        } else if (this.mLayoutManager instanceof GridLayoutManager) {
            this.firstVisibleItem = ((GridLayoutManager) this.mLayoutManager).findFirstVisibleItemPosition();
        } else if (this.mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] array = new int[2];
            ((StaggeredGridLayoutManager) this.mLayoutManager).findFirstVisibleItemPositions(array);
            this.firstVisibleItem = array[1];
        }
        if (this.loading && this.totalItemCount > this.previousTotal) {
            this.loading = false;
            this.previousTotal = this.totalItemCount;
        }
        if (!this.loading && this.totalItemCount - this.visibleItemCount <= this.firstVisibleItem + this.visibleThreshold) {
            this.current_page++;
            onLoadMore(this.current_page);
            this.loading = true;
        }
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void resetAllData() {
        current_page = 1;
        loading = true;
        previousTotal = 0;
        visibleThreshold = 15;

    }


}
