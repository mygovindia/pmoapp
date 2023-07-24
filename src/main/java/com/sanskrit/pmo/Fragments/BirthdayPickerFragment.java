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

public class BirthdayPickerFragment extends Fragment {
    public static final String TAG = BirthdayPickerFragment.class.getSimpleName();
    EditText birthday;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.appsetup_birthday, container, false);
        this.birthday = (EditText) rootView.findViewById(R.id.edittext_birthday);
        this.birthday.setInputType(0);
        try {
            String dob = PreferencesUtility.getServerDOB(getActivity());
            if (!(dob == null || dob.equals(""))) {
                try {
                    this.birthday.setText(DateUtil.dateToString(new Date(Long.parseLong(dob) * 1000)));
                } catch (NumberFormatException e) {
                    Log.e(TAG, "NumberFormatException: " + e.getMessage());
                    this.birthday.setText(DateUtil.dateStringToFormattedString1(dob));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.birthday.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() != 1) {
                        return false;
                    }
                    Calendar mcurrentTime = Calendar.getInstance();
                    int year = mcurrentTime.get(Calendar.YEAR);
                    int month = mcurrentTime.get(Calendar.MONTH);
                    int date = mcurrentTime.get(Calendar.DATE);
                    Context context = BirthdayPickerFragment.this.getActivity();
                    if (BirthdayPickerFragment.isBrokenSamsungDevice()) {
                        context = new ContextThemeWrapper(BirthdayPickerFragment.this.getActivity(), 16973939);
                    }
                    DatePickerDialog mDatePicker = new DatePickerDialog(context, new OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            BirthdayPickerFragment.this.birthday.setText(dayOfMonth + " " + new DateFormatSymbols().getMonths()[monthOfYear] + " " + year);
                            ((SetupActivity) BirthdayPickerFragment.this.getActivity()).setDOB(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);


                        }
                    }, year, month, date);
                    mDatePicker.setTitle("");
                    mDatePicker.show();
                    return true;
                }
            });
        } catch (Exception e2) {
            e2.printStackTrace();
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
