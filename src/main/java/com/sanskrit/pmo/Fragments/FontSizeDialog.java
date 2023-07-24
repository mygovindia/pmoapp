package com.sanskrit.pmo.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sanskrit.pmo.Activities.NewsReaderActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;


public class FontSizeDialog extends BottomSheetDialogFragment {
    Button cancel;
    SeekBar fontsize;
    private BottomSheetBehavior mBehavior;


    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        try {
            PreferencesUtility.setFontSize(getActivity(), this.fontsize.getProgress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_fontsize_dialog, null);
        dialog.setContentView(view);
        this.mBehavior = BottomSheetBehavior.from((View) view.getParent());
        this.fontsize = (SeekBar) view.findViewById(R.id.font_size);
        this.cancel = (Button) view.findViewById(R.id.cancel_tts);
        this.fontsize.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && (FontSizeDialog.this.getActivity() instanceof NewsReaderActivity)) {
                    NewsReaderActivity newsDetailActivity = (NewsReaderActivity) FontSizeDialog.this.getActivity();
                    newsDetailActivity.content.setTextSize((float) (progress + 3));
                    newsDetailActivity.title.setTextSize((float) (progress + 7));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        this.cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FontSizeDialog.this.getDialog().dismiss();

            }
        });
        this.fontsize.setProgress(PreferencesUtility.getFontSize(getActivity()));
        return dialog;
    }

    public void onStart() {
        super.onStart();
        this.mBehavior.setState(3);
    }
}
