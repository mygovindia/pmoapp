package com.sanskrit.pmo.Fragments;

import android.app.Dialog;
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


public class TTSDialogFragment extends BottomSheetDialogFragment {
    Button cancel;
    private BottomSheetBehavior mBehavior;
    SeekBar pitch;
    SeekBar speech;


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_tts_dialog, null);
        dialog.setContentView(view);
        this.mBehavior = BottomSheetBehavior.from((View) view.getParent());
        this.speech = (SeekBar) view.findViewById(R.id.speed_seek);
        this.pitch = (SeekBar) view.findViewById(R.id.pitch_seek);
        this.cancel = (Button) view.findViewById(R.id.cancel_tts);
        this.speech.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    ((NewsReaderActivity) TTSDialogFragment.this.getActivity()).tts.setSpeechRate(((float) progress) * 0.1f);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        this.pitch.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    ((NewsReaderActivity) TTSDialogFragment.this.getActivity()).tts.setPitch(((float) progress) * 0.1f);
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
                TTSDialogFragment.this.getDialog().dismiss();
                try {
                    ((NewsReaderActivity) TTSDialogFragment.this.getActivity()).tts.stop();
                    ((NewsReaderActivity) TTSDialogFragment.this.getActivity()).tts.shutdown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return dialog;
    }

    public void onStart() {
        super.onStart();
        this.mBehavior.setState(3);
    }
}
