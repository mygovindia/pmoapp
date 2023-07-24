package com.sanskrit.pmo.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.CommonFunctions;
import com.sanskrit.pmo.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NativeSignupActivity extends AppCompatActivity {
    private static final String TAG = NativeSignupActivity.class.getSimpleName();
    private static final int REQUEST_MULTIPLE_PERMISSION = 101;
    Toolbar toolbar;
    public static final String API_KEY = "57076294a5e2ab7fe000000151da1b87c5224e1a4d05c7d8553d2aa2";
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String stringDate;
    TextView textViewDate;
    String dobDate = "";
    String dobMonth = "";
    String dobYear = "";
    RadioGroup radioGroup;
    String gender = "male";
    EditText editTextfullName;
    EditText editTextMail, editTextMobileNumber;
    String fullName, email, mobileNumber, country;
    private Spinner countryspinner;
    String[] stringArray;
    private String ucntryid;
    RelativeLayout relativeLayoutOTP;
    LinearLayout linearLayoutRegister;
    Button btnSubmitOtp, btnResend;
    private String otpnumber;
    EditText editTextOTP;
    BroadcastReceiver receiver = new SMSBroadcast();
    private String deviceToken;

    private String IMEI;
    private TelephonyManager telephonyManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_signup);
        initView();

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        //SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        //deviceToken = pref.getString("regId", null);
        deviceToken = PreferencesUtility.getFCMID(getApplicationContext());
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        textViewDate = findViewById(R.id.tv_dob);
        btnSubmitOtp = findViewById(R.id.btn_submitotp);
        btnResend = findViewById(R.id.btn_resend);
        editTextOTP = findViewById(R.id.edt_opt);

        radioGroup = findViewById(R.id.radio_group);
        editTextfullName = findViewById(R.id.edt_name);
        editTextMail = findViewById(R.id.edt_email);
        editTextMobileNumber = findViewById(R.id.edt_mobile);
        countryspinner = (Spinner) findViewById(R.id.countryspinner);
        linearLayoutRegister = (LinearLayout) findViewById(R.id.linear_register);
        relativeLayoutOTP = (RelativeLayout) findViewById(R.id.opt_layout);

        stringArray = getResources().getStringArray(R.array.country_arrays);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.text_item, stringArray);
        this.countryspinner.setAdapter(arrayAdapter);
        /*this.countryspinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    ((InputMethodManager) NativeSignupActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(NativeSignupActivity.this.getWindowToken(), 0);
                } catch (Exception e) {
                }
                return false;
            }
        });*/

        this.countryspinner.setSelection(arrayAdapter.getPosition("India"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.register));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radio_male:
                        gender = "male";
                        break;
                    case R.id.radio_female:
                        gender = "female";
                        break;
                    case R.id.radio_other:
                        gender = "other";
                        break;
                    default:
                        gender = "male";
                }
            }

        });

    }

    public void onDateSelect(View view) {
        getDate();
    }


    private void getDate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();


        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        stringDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + "";


                        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = null;
                        try {
                            date = format1.parse(stringDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String dayOfTheWeek = (String) DateFormat.format("EEEE", date); // Thursday
                        dobDate = (String) DateFormat.format("dd", date); // 20
                        dobMonth = (String) DateFormat.format("MMMM", date); // Jun
                        ///String monthNumber = (String) DateFormat.format("MM", date); // 06
                        dobYear = (String) DateFormat.format("yyyy", date); // 2013

                        dayOfTheWeek = dayOfTheWeek.substring(0, 3);

                        textViewDate.setText(dobDate + " " + dobMonth + " " + dobYear);


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    public void onRegister(View view) {

        if (CommonFunctions.isNetworkOnline(NativeSignupActivity.this)) {
            NativeSignupActivity.this.fullName = NativeSignupActivity.this.editTextfullName.getText().toString().trim();
            if (NativeSignupActivity.this.fullName.equals("")) {
                Toast.makeText(NativeSignupActivity.this.getApplicationContext(), R.string.enter_valid_otp, Toast.LENGTH_SHORT).show();
                return;
            }
            NativeSignupActivity.this.email = NativeSignupActivity.this.editTextMail.getText().toString().trim();
            if (!NativeSignupActivity.this.email.isEmpty()) {
                if (!NativeSignupActivity.this.email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    Toast.makeText(NativeSignupActivity.this.getApplicationContext(), R.string.please_enter_valid_email_id, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            int selectedItemPosition = NativeSignupActivity.this.countryspinner.getSelectedItemPosition();
            NativeSignupActivity.this.country = stringArray[selectedItemPosition];
            NativeSignupActivity.this.mobileNumber = NativeSignupActivity.this.editTextMobileNumber.getText().toString().trim();
            if (NativeSignupActivity.this.mobileNumber.isEmpty()) {
                Toast.makeText(NativeSignupActivity.this.getApplicationContext(), R.string.please_enter_mobile_number, Toast.LENGTH_SHORT).show();
                return;
            }
            if (NativeSignupActivity.this.country.equalsIgnoreCase("India")) {
                if (NativeSignupActivity.this.mobileNumber.length() != 10) {
                    Toast.makeText(NativeSignupActivity.this.getApplicationContext(), R.string.please_enter_valid_mobile_number, Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Patterns.PHONE.matcher(NativeSignupActivity.this.mobileNumber).matches()) {
                    Toast.makeText(NativeSignupActivity.this.getApplicationContext(), R.string.please_enter_valid_mobile_number, Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (NativeSignupActivity.this.mobileNumber.length() < 5 || NativeSignupActivity.this.mobileNumber.length() > 15) {
                Toast.makeText(NativeSignupActivity.this.getApplicationContext(), R.string.please_enter_valid_mobile_number, Toast.LENGTH_SHORT).show();
                return;
            } else if (!Patterns.PHONE.matcher(NativeSignupActivity.this.mobileNumber).matches()) {
                Toast.makeText(NativeSignupActivity.this.getApplicationContext(), R.string.please_enter_valid_mobile_number, Toast.LENGTH_SHORT).show();
                return;
            }

            /*if (dobDate.trim().length() < 1) {
                Toast.makeText(NativeSignupActivity.this.getApplicationContext(), R.string.please_select_your_dob, Toast.LENGTH_SHORT).show();
                return;
            }*/

            if (NativeSignupActivity.this.country.equals("Select Country")) {
                Toast.makeText(NativeSignupActivity.this.getApplicationContext(), R.string.please_select_your_country, Toast.LENGTH_SHORT).show();
                return;
            }
            NativeSignupActivity.this.ucntryid = NativeSignupActivity.this.getResources().getStringArray(R.array.CountryCodes)[selectedItemPosition];
            new RegisterNewUser().execute(new String[]{""});
        } else
            CommonFunctions.ShowMessageToUser(NativeSignupActivity.this, CommonFunctions.NetworkMessage);
    }

    /*@SuppressLint("MissingPermission")
    private void getDeviceDetails() {
        IMEI = telephonyManager.getDeviceId();
        if (IMEI == null || IMEI.length() == 0) {
            IMEI = "000000000000000";
        }
        Log.v(TAG, "IMEI: " + IMEI);
        PreferencesUtility.setIMEI(NativeSignupActivity.this, IMEI);
    }*/

    public void onSubmitOTP(View view) {

        if (CommonFunctions.isNetworkOnline(NativeSignupActivity.this)) {
            otpnumber = editTextOTP.getText().toString();
            //   RegisterActivity.this.otpnumber = RegisterActivity.this.otpbox.getText().toString().trim();
            if (NativeSignupActivity.this.otpnumber.trim().length() < 1) {
                Toast.makeText(NativeSignupActivity.this.getApplicationContext(), R.string.enter_your_otp, Toast.LENGTH_SHORT).show();
                return;
            } else
                new RegisterNewUser().execute(new String[]{otpnumber});

        } else
            CommonFunctions.ShowMessageToUser(NativeSignupActivity.this, CommonFunctions.NetworkMessage);

    }

    public void onResenOPT(View view) {
        if (CommonFunctions.isNetworkOnline(NativeSignupActivity.this)) {
            new RegisterNewUser().execute(new String[]{"TRUE"});
            btnResend.setVisibility(View.GONE);
            btnSubmitOtp.setAlpha(1f);
            btnSubmitOtp.setEnabled(true);
        } else
            CommonFunctions.ShowMessageToUser(NativeSignupActivity.this, CommonFunctions.NetworkMessage);


    }

    class RegisterNewUser extends AsyncTask<String, Void, String> {
        final OkHttpClient client;
        String message;
        final Dialog myDialog;
        boolean otpstatus1;
        String responsecode;
        String status;

        private RegisterNewUser() {
            this.client = CommonFunctions.HtppcallforInterprator();
            this.myDialog = CommonFunctions.showDialog(NativeSignupActivity.this);
            this.message = getString(R.string.please_try_again_later);
            this.otpstatus1 = true;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            this.myDialog.show();
            this.myDialog.setCancelable(true);
            this.myDialog.setCanceledOnTouchOutside(false);
        }

        protected String doInBackground(String... strArr) {
            try {
                String str = strArr[0];
                try {
                    FormBody.Builder add = new FormBody.Builder()
                            .add("name", NativeSignupActivity.this.fullName)
                            .add("email", NativeSignupActivity.this.email)
                            .add("gateway", NativeSignupActivity.this.ucntryid)
                            .add("mobile", NativeSignupActivity.this.mobileNumber)
                            .add("gender", NativeSignupActivity.this.gender).
                                    add("dob_date", dobDate)
                            .add("dob_month", dobMonth)
                            .add("dob_year", dobYear);

                    if (!str.equals("")) {
                        if (str.equals("TRUE")) {
                            add.add("resend_otp", str);
                        } else {
                            add.add("otp", str);
                        }
                    }
                    Response execute = this.client.newCall(new Request.Builder().url("https://api.mygov.in/userregister_ver1/?api_key=" + API_KEY).post(add.build()).build()).execute();
                    if (execute.isSuccessful()) {
                        try {
                            JSONObject jSONObject = new JSONObject(execute.body().string());
                            this.status = jSONObject.getString(NotificationCompat.CATEGORY_STATUS);
                            this.responsecode = jSONObject.getString("responsecode");
                            this.message = jSONObject.getString("message");
                        } catch (JSONException e) {
                            this.otpstatus1 = false;
                        }
                    } else {
                        this.otpstatus1 = false;
                    }
                } catch (IOException e2) {
                    this.otpstatus1 = false;
                }
            } catch (Exception e3) {
            }
            return null;
        }

        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            if (myDialog != null && myDialog.isShowing()) myDialog.dismiss();
            String langCode = PreferencesUtility.getLanguagePrefernce(NativeSignupActivity.this);
            if (langCode.equalsIgnoreCase("hi")) {
                this.message = getFilteredHindiMessage(this.message != null ? this.message.trim() : null, langCode);
            }

            try {
                if (this.status.contains("fail")) {
                    if (this.responsecode.equals("AUTH004")) {
                        Toast.makeText(NativeSignupActivity.this, this.message, Toast.LENGTH_LONG).show();

                        // CommonFunctions.ShowMessageToUser(NativeSignupActivity.this, getString(R.string.registration_failed) + this.message);
                    } else if (this.responsecode.equals("AUTH005")) {
                        Toast.makeText(NativeSignupActivity.this, this.message, Toast.LENGTH_LONG).show();

                        // CommonFunctions.ShowMessageToUser(NativeSignupActivity.this, getString(R.string.otp_verification_failed) + this.message);
                    } else if (this.responsecode.equals("AUTH006")) {
                        Toast.makeText(NativeSignupActivity.this, this.message, Toast.LENGTH_LONG).show();

                        // CommonFunctions.ShowMessageToUser(NativeSignupActivity.this, getString(R.string.otp_verification_failed) + this.message);
                    } else {
                        Toast.makeText(NativeSignupActivity.this, this.message, Toast.LENGTH_LONG).show();

                        // CommonFunctions.ShowMessageToUser(NativeSignupActivity.this, this.message);
                    }

                } else {
                    if (this.responsecode.equals("AUTH001")) {
                        Toast.makeText(NativeSignupActivity.this, this.message, Toast.LENGTH_LONG).show();

                        // CommonFunctions.ShowMessageToUser(NativeSignupActivity.this, this.message);
                    } else if (this.responsecode.equals("AUTH002")) {
                        Toast.makeText(NativeSignupActivity.this, this.message, Toast.LENGTH_LONG).show();

                        // CommonFunctions.ShowMessageToUser(NativeSignupActivity.this, this.message);
                        linearLayoutRegister.setVisibility(View.GONE);
                        relativeLayoutOTP.setVisibility(View.VISIBLE);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                NativeSignupActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnResend.setVisibility(View.VISIBLE);
                                        // btnSubmitOtp.setAlpha(.5f);
                                        btnSubmitOtp.setEnabled(true);
                                    }
                                });
                            }
                        }, 60000);

                    } else if (this.responsecode.equals("AUTH003")) {
                        //moveToLoginActivity(message);
                        PMSignupAsync async = new PMSignupAsync();
                        async.execute(message);
                    } else {
                        Toast.makeText(NativeSignupActivity.this, this.message, Toast.LENGTH_LONG).show();

                        //CommonFunctions.ShowMessageToUser(NativeSignupActivity.this, this.message);
                    }
                }
            } catch (Exception e) {
                CommonFunctions.ShowMessageToUser(NativeSignupActivity.this, getString(R.string.unable_to_connect));
            }

        }
    }

    private void moveToLoginActivity(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NativeSignupActivity.this);
        builder.setTitle("Message");
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(NativeSignupActivity.this, NativeLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

            }
        });
        builder.show();
    }

    private String getFilteredHindiMessage(String message, String langCode) {
        Log.v(TAG, "getFilteredHindiMessage(-,-)=> Message: " + message + "<=> Lang: " + langCode);
        if (message == null) return message;
        if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_wrong_referral_code, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_wrong_referral_code, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_wrong_entry_attempt_exceed, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_wrong_entry_attempt_exceed, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_mobile_already_registered, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_mobile_already_registered, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_email_already_registered, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_email_already_registered, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_email_already_registered_not_confirmed, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_email_already_registered_not_confirmed, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_already_registered, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_already_registered, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_wrong_otp, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_wrong_otp, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.successfully_registered, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.successfully_registered, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_registration_failed, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_registration_failed, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.otp_sent_mobile_success_1, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.otp_sent_mobile_success_1, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.otp_sent_email_success_1, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.otp_sent_email_success_1, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.otp_sent_email_success_2, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.otp_sent_email_success_2, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_invalid_mobile_no, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_invalid_mobile_no, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_invalid_name, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_invalid_name, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_invalid_email, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_invalid_email, langCode);
        } else if (message.equalsIgnoreCase(Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_unknown, "en"))) {
            message = Utils.getStringByLocale(NativeSignupActivity.this, R.string.error_unknown, langCode);
        }
        return message;
    }


    public void onResume() {
        super.onResume();
        try {
            LocalBroadcastManager.getInstance(this).registerReceiver(this.receiver, new IntentFilter("otp"));
        } catch (Exception e) {
        }
        /*if (checkPermissionStatus()) {
            getDeviceDetails();
        }*/
    }

    /*private boolean checkPermissionStatus() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionReadPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionReadPhoneState != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_MULTIPLE_PERMISSION);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }
*/
   /* @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MULTIPLE_PERMISSION: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        getDeviceDetails();
                    } else {
                        Toast.makeText(NativeSignupActivity.this, getString(R.string.enable_permission), Toast.LENGTH_SHORT).show();
                    }
                }
                return;
            }
        }
    }*/


    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiver);
    }

    class SMSBroadcast extends BroadcastReceiver {
        SMSBroadcast() {
        }

        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equalsIgnoreCase("otp")) {
                    Object substring = CommonFunctions.stripNonDigits(intent.getStringExtra("message")).substring(0, 6);
                    editTextOTP.setText(substring.toString());
                    editTextOTP.setSelection(substring.toString().length());
                    if (CommonFunctions.cbuilder1 != null) {
                        AlertDialog create = CommonFunctions.cbuilder1.create();
                        if (create.isShowing()) {
                            create.dismiss();
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    class PMSignupAsync extends AsyncTask<String, Void, String> {
        final String TAG = PMSignupAsync.class.getSimpleName();
        final OkHttpClient client;
        private Dialog myDialog;
        String message;
        boolean status =true;


        private PMSignupAsync() {
            this.client = CommonFunctions.HtppcallforInterprator();
            myDialog = CommonFunctions.showDialog(NativeSignupActivity.this);
        }

        protected void onPreExecute() {
            super.onPreExecute();
            myDialog.show();
            myDialog.setCancelable(true);
            myDialog.setCanceledOnTouchOutside(false);
        }

        protected String doInBackground(String... params) {
            try {
                message = params[0];
                FormBody.Builder add = new FormBody.Builder()
                        .add("email", NativeSignupActivity.this.email)
                        .add("mobile", mobileNumber)
                        .add("password", "")
                        .add("notification", PreferencesUtility.getPrefNotificationOnOf(NativeSignupActivity.this))
                        .add("mobile", NativeSignupActivity.this.mobileNumber)
                        .add("full_name", NativeSignupActivity.this.fullName)
                        .add("gender", NativeSignupActivity.this.gender)
                        .add("dob", dobDate + "/" + dobMonth + "/" + dobYear)
                        .add("device_type", "android")
                        .add("device_token", deviceToken)
                        .add("imei", PreferencesUtility.getUniqueDeviceId(NativeSignupActivity.this));
                Response execute = this.client.newCall(new Request.Builder()
                        .url("https://api.pmindia.gov.in/mobile_signup_insert")
                        .addHeader("Auth", "6069452e562e5afc18365b5b29394c8b9ec14593a3f6454fdee9e09b23e50222")
                        .post(add.build()).build())
                        .execute();
                if (execute.isSuccessful()) {
                    status=true;
                    String response = execute.body().string();
                    Log.v(TAG, "SIGNUP_RESPONSE: " + response);
                    try {
                        JSONObject jSONObject = new JSONObject(execute.body().string());
                        Log.v(TAG, "SIGNUP_RESPONSE: " + jSONObject);
                        response = jSONObject.toString();
                    } catch (JSONException e) {
                        Log.v(TAG, "JSONException: " + e.getMessage());
                    } catch (Exception e) {
                        Log.v(TAG, "Exception: " + e.getMessage());
                    }
                    return response;
                }
            } catch (Exception e) {
                status=false;
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            if (myDialog != null && myDialog.isShowing()) myDialog.dismiss();
            if (status) {
                PreferencesUtility.setPrefPrefMobileNumber(NativeSignupActivity.this, mobileNumber);

                moveToLoginActivity(message);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(NativeSignupActivity.this);
                //builder.setTitle(getString(R.string.unable_to_connect));
                builder.setCancelable(false);
                builder.setMessage(getString(R.string.unable_to_connect));
                builder.setPositiveButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        PMSignupAsync async = new PMSignupAsync();
                        async.execute(message);
                    }
                });
                builder.show();
            }
        }

        private String getResult(String response) {
            return response.substring(1, response.length() - 1);
        }
    }

}
