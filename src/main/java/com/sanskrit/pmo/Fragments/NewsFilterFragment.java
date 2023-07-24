package com.sanskrit.pmo.Fragments;

import android.app.Dialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.utils.IOUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsFilterFragment extends BottomSheetDialogFragment {
    NewsTagsAdapter adapter;
    private List<String> enabledFilters;
    TextView filterRecyclerviewText;
    private BottomSheetBehavior mBehavior;
    RecyclerView recyclerView;
    TextView save;


    private class NewsTagsAdapter extends RecyclerView.Adapter<NewsTagsAdapter.ViewHolder> {
        List<String> arrayList;

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView content;
            protected ImageView status;

            public ViewHolder(View itemView) {
                super(itemView);
                this.content = (TextView) itemView.findViewById(R.id.content);
                this.status = (ImageView) itemView.findViewById(R.id.filter_status);
                itemView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        try {
                            if (ViewHolder.this.getAdapterPosition() != -1) {
                                String filter = (String) NewsTagsAdapter.this.arrayList.get(ViewHolder.this.getAdapterPosition());
                                if (NewsFilterFragment.this.enabledFilters.size() == 0) {
                                    NewsFilterFragment.this.enabledFilters.add(filter);
                                } else if (NewsFilterFragment.this.enabledFilters.contains(filter)) {
                                    NewsFilterFragment.this.enabledFilters.remove(filter);
                                } else {
                                    NewsFilterFragment.this.enabledFilters.add(filter);
                                }
                                NewsTagsAdapter.this.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        public NewsTagsAdapter(List<String> arrayList) {
            this.arrayList = arrayList;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news_filter_tag, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.content.setText((CharSequence) this.arrayList.get(i));
            if (NewsFilterFragment.this.enabledFilters.contains(this.arrayList.get(i))) {
                viewHolder.status.setVisibility(View.VISIBLE);
            } else {
                viewHolder.status.setVisibility(View.GONE);
            }
        }

        public int getItemCount() {
            return this.arrayList.size();
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_news_filter, null);
        dialog.setContentView(view);
        this.mBehavior = BottomSheetBehavior.from((View) view.getParent());
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        this.filterRecyclerviewText = (TextView) view.findViewById(R.id.filters_recyclerview_text);
        this.save = (TextView) view.findViewById(R.id.btn_save_filters);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<String> tags = new ArrayList();
        tags.add("Economy");
        tags.add("Farmer Welfare");
        tags.add("Rural Development");
        tags.add("Infrastructure");
        tags.add("Technology");
        tags.add("Education & Skills");
        tags.add("Health & Social Welfare");
        tags.add("India & the world");
        tags.add("Governance");
        enabledFilters = IOUtils.getArrayFromPref(getActivity());
        if (enabledFilters.contains("")) {
            enabledFilters.remove("");
        }
        this.adapter = new NewsTagsAdapter(tags);
        this.recyclerView.setAdapter(this.adapter);
        this.save.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //enabledFilters = IOUtils.getArrayFromPref(getActivity());
                IOUtils.saveArrayToPreferences(NewsFilterFragment.this.getActivity(), NewsFilterFragment.this.enabledFilters);
                ((FeedFragment) NewsFilterFragment.this.getParentFragment()).filtersUpdated(NewsFilterFragment.this.enabledFilters);
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public void onStart() {
        super.onStart();
        this.mBehavior.setState(3);
    }
}
