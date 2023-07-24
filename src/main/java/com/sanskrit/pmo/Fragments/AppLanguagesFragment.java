package com.sanskrit.pmo.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sanskrit.pmo.R;

import java.util.ArrayList;
import java.util.List;

public class AppLanguagesFragment extends BottomSheetDialogFragment {
    LanguagesAdapter adapter;
    private BottomSheetBehavior mBehavior;
    RecyclerView recyclerView;

    public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.ViewHolder> {
        private Context context;
        private List<String> languages;

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView langName;

            public ViewHolder(View itemView) {
                super(itemView);
                this.langName = (TextView) itemView.findViewById(R.id.lang_name);
                itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AppLanguagesFragment.this.getParentFragment() instanceof LanguageSelectionFragment) {
                            ((LanguageSelectionFragment) AppLanguagesFragment.this.getParentFragment()).onLanguageChanged((String) LanguagesAdapter.this.languages.get(ViewHolder.this.getAdapterPosition()));
                            AppLanguagesFragment.this.getDialog().dismiss();
                        }
                    }
                });

            }
        }

        public LanguagesAdapter(Context context, List<String> languages) {
            this.context = context;
            this.languages = languages;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mkb_language, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.langName.setText((CharSequence) this.languages.get(i));
        }

        public int getItemCount() {
            return this.languages.size();
        }

        public void updateDataSet(List<String> languages) {
            this.languages = languages;
            notifyDataSetChanged();
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.dialog_mkb_languages, null);
        dialog.setContentView(view);
        this.mBehavior = BottomSheetBehavior.from((View) view.getParent());
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        List<String> languages = new ArrayList();
        languages.add("English");
        languages.add("Hindi");
        languages.add("Assamese");
        languages.add("Bengali");
        languages.add("Gujarati");
        languages.add("Kannada");
        languages.add("Malayalam");
        languages.add("Manipuri");
        languages.add("Marathi");
        languages.add("Odia");
        languages.add("Punjabi");
        languages.add("Tamil");
        languages.add("Telugu");
        //languages.add("Urdu");
        this.adapter = new LanguagesAdapter(getActivity(), languages);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(this.adapter);
        return dialog;
    }

    public void onStart() {
        super.onStart();
        this.mBehavior.setState(3);
    }
}
