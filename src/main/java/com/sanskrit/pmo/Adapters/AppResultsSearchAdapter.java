package com.sanskrit.pmo.Adapters;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Utils.SearchUtils;

import java.util.ArrayList;

public class AppResultsSearchAdapter extends RecyclerView.Adapter<AppResultsSearchAdapter.ItemHolder> {
    private Activity mContext;
    private ArrayList<Integer> searchResults = new ArrayList();

    public class ItemHolder extends RecyclerView.ViewHolder implements OnClickListener {
        protected TextView resultTitle = ((TextView) this.itemView.findViewById(R.id.result_title));

        public ItemHolder(View view) {
            super(view);
            view.setOnClickListener(this);
        }

        public void onClick(View v) {
            AppResultsSearchAdapter.this.mContext.startActivity(SearchUtils.getIntentForAppResults(AppResultsSearchAdapter.this.mContext, ((Integer) AppResultsSearchAdapter.this.searchResults.get(getAdapterPosition())).intValue()));
        }
    }

    public AppResultsSearchAdapter(Activity context, ArrayList<Integer> arrayList) {
        this.mContext = context;
        this.searchResults = arrayList;
    }

    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_appresults, null));
    }

    public void onBindViewHolder(ItemHolder itemHolder, int i) {
        itemHolder.resultTitle.setText((CharSequence) SearchUtils.getApplicationContentArray().get(((Integer) this.searchResults.get(i)).intValue()));
    }

    public void onViewRecycled(ItemHolder itemHolder) {
    }

    public int getItemCount() {
        return this.searchResults.size();
    }

    public void updateDataSet(ArrayList<Integer> arrayList) {
        this.searchResults = arrayList;
        notifyDataSetChanged();
    }
}
