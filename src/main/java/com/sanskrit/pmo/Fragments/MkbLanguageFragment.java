package com.sanskrit.pmo.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anupcowkur.reservoir.Reservoir;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sanskrit.pmo.Adapters.MKBAudioAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.server.SanskritClient;
import com.sanskrit.pmo.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MkbLanguageFragment extends BottomSheetDialogFragment {
    LanguagesAdapter adapter;
    private BottomSheetBehavior mBehavior;
    private MKBAudioAdapter parentAdapter;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    LinearLayout bottomSheet;
    TextView tv_all_lang;

    public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.ViewHolder> {
        private Context context;
        private List<String> languages;

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView langName;

            public ViewHolder(View itemView) {
                super(itemView);
                this.langName = (TextView) itemView.findViewById(R.id.lang_name);
                itemView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        PreferencesUtility.setMkbLanguageName(LanguagesAdapter.this.context, (String) LanguagesAdapter.this.languages.get(ViewHolder.this.getAdapterPosition()));
                        PreferencesUtility.setMkbLanguageCode(LanguagesAdapter.this.context, ((String) LanguagesAdapter.this.languages.get(ViewHolder.this.getAdapterPosition())).toLowerCase());
                        MkbLanguageFragment.this.onLanguageChanged((String) LanguagesAdapter.this.languages.get(ViewHolder.this.getAdapterPosition()));
                        MkbLanguageFragment.this.parentAdapter.updateLanguage();
                        MkbLanguageFragment.this.getDialog().dismiss();
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
            viewHolder.langName.setText(((String) this.languages.get(i)).substring(0, 1).toUpperCase() + ((String) this.languages.get(i)).substring(1));
            viewHolder.langName.setTextColor(getActivity().getResources().getColor(R.color.black_grey));


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
        this.bottomSheet = (LinearLayout) view.findViewById(R.id.bottomSheet);
        this.tv_all_lang = (TextView) view.findViewById(R.id.tv_all_lang);

        tv_all_lang.setTextColor(getActivity().getResources().getColor(R.color.black_grey));
        bottomSheet.setBackgroundColor(Color.parseColor("#ffffff"));
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.adapter = new LanguagesAdapter(getActivity(), new ArrayList());
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(this.adapter);
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
        this.adapter.updateDataSet(languages);
        return dialog;
    }

    private void fetchLanguages() {
        SanskritClient.getInstance(getActivity()).getMkbLanguages(new GenericCallback() {
            @Override
            public void failure() {
                MkbLanguageFragment.this.progressBar.setVisibility(View.GONE);
                if (MkbLanguageFragment.this.getActivity() != null) {
                    Toast.makeText(MkbLanguageFragment.this.getActivity(), R.string.error_getting_languages, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void success(Object response) {
                List<String> languages = (List) response;
                MkbLanguageFragment.this.adapter.updateDataSet(languages);
                MkbLanguageFragment.this.progressBar.setVisibility(View.GONE);
                try {
                    Reservoir.put(Constants.CACHE_MKB_LANGUAGES, languages);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        });
    }

    public void setOriginalAdapter(MKBAudioAdapter adapter) {
        this.parentAdapter = adapter;
    }

    public void onStart() {
        super.onStart();
        this.mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void onLanguageChanged(String newLang) {
        PreferencesUtility.setMkbLanguageName(getActivity(), newLang);

        if (newLang.equals("Telugu")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "te");
        } else if (newLang.equals("Marathi")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "mr");
        } else if (newLang.equals("Gujarati")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "gu");
        } else if (newLang.equals("Malayalam")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "ml");
        } else if (newLang.equals("Manipuri")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "mni");
        } else if (newLang.equals("Odia")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "ory");
        } else if (newLang.equals("Punjabi")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "pa");
        } else if (newLang.equals("English")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "en");
        } else if (newLang.equals("Hindi")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "hi");
        } else if (newLang.equals("Assamese")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "asm");
        } else if (newLang.equals("Tamil")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "ta");
        } else if (newLang.equals("Kannada")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "kn");
        } else if (newLang.equals("Bengali")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "bn");
        }

        /*else if (newLang.equals("Urdu")) {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "ur");
        }*/

        else {
            PreferencesUtility.setMkbLanguageCode(getActivity(), "en");
        }

    }
}
