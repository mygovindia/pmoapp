package com.sanskrit.pmo.Fragments;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.sanskrit.pmo.Activities.SetupActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.utils.DateUtil;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

public class AnniversaryPickerFragment extends Fragment {
    public static final String TAG = AnniversaryPickerFragment.class.getSimpleName();
    EditText anniversary;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.appsetup_anniversary, container, false);
        this.anniversary = (EditText) rootView.findViewById(R.id.edittext_anniversary);
        this.anniversary.setInputType(0);
        try {
            String doa = PreferencesUtility.getServerDOA(getActivity());
            if (!(doa == null || doa.equals(""))) {
                try {
                    this.anniversary.setText(DateUtil.dateToString(new Date(Long.parseLong(doa) * 1000)));
                } catch (NumberFormatException e) {
                    Log.e(TAG, "NumberFormatException: " + e.getMessage());
                    this.anniversary.setText(DateUtil.dateStringToFormattedString1(doa));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            anniversary.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() != 1) {
                        return false;
                    }
                    Calendar mcurrentTime = Calendar.getInstance();
                    int year = mcurrentTime.get(Calendar.YEAR);
                    int month = mcurrentTime.get(Calendar.MONTH);
                    int date = mcurrentTime.get(Calendar.DATE);
                    Context context = AnniversaryPickerFragment.this.getActivity();
                    if (AnniversaryPickerFragment.isBrokenSamsungDevice()) {
                        context = new ContextThemeWrapper(AnniversaryPickerFragment.this.getActivity(), 16973939);
                    }
                    DatePickerDialog mDatePicker = new DatePickerDialog(context, new OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            AnniversaryPickerFragment.this.anniversary.setText(dayOfMonth + " " + new DateFormatSymbols().getMonths()[monthOfYear] + " " + year);
                            ((SetupActivity) AnniversaryPickerFragment.this.getActivity()).setDOA(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, year, month, date);
                    mDatePicker.setTitle("");
                    mDatePicker.show();
                    return true;
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
            //Crashlytics.logException(e2);
        }
        return rootView;
    }

    private static boolean isBrokenSamsungDevice() {
        return Build.MANUFACTURER.equalsIgnoreCase("samsung") && isBetweenAndroidVersions(21, 22);
    }

    private static boolean isBetweenAndroidVersions(int min, int max) {
        return VERSION.SDK_INT >= min && VERSION.SDK_INT <= max;
    }
}
