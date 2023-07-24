package com.sanskrit.pmo.Adapters;

import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.network.mygov.models.FunctionalChartModel;

import java.util.List;

public class FunctionalChartAdapter extends RecyclerView.Adapter<FunctionalChartAdapter.ViewHolder> {
    private List<FunctionalChartModel> arrayList;
    private AppCompatActivity context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView call;
        protected TextView designation;
        protected TextView name;
        protected TextView number;

        public ViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.designation = (TextView) itemView.findViewById(R.id.designation);
            this.number = (TextView) itemView.findViewById(R.id.number);
            this.call = (ImageView) itemView.findViewById(R.id.call);
            this.call.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.DIAL");
                    intent.setData(Uri.parse("tel:011" + ((FunctionalChartModel) FunctionalChartAdapter.this.arrayList.get(ViewHolder.this.getAdapterPosition())).number));
                    FunctionalChartAdapter.this.context.startActivity(intent);
                }
            });
        }
    }

    public FunctionalChartAdapter(AppCompatActivity context, List<FunctionalChartModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_functional_chart, null));
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        FunctionalChartModel chart = (FunctionalChartModel) this.arrayList.get(i);
        viewHolder.name.setText(chart.name);
        viewHolder.designation.setText(chart.designation);
        viewHolder.number.setText(this.context.getString(R.string.functional_chart_number) + chart.number);
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    public void updateDataSet(List<FunctionalChartModel> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }
}
