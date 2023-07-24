package com.sanskrit.pmo.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.widget.TextView;

import com.sanskrit.pmo.Fragments.ProfilePreferenceFragment;
import com.sanskrit.pmo.Models.MyPojo;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.server.SanskritClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class ProfileActivity extends BaseActivity {
    TextView email;
    TextView name;
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_profile);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((int) R.string.title_profile);
        this.name = (TextView) findViewById(R.id.profile_name);
        this.email = (TextView) findViewById(R.id.profile_email);
        this.name.setText(PreferencesUtility.getMygovOauthName(this));
        this.email.setText(PreferencesUtility.getMygovOauthEmail(this));
        searchMenuVisible = false;
        getDOBOrDOAFromServer();
       // getFragmentManager().beginTransaction().replace(R.id.preference_container, new ProfilePreferenceFragment()).commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_search:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getDOBOrDOAFromServer() {
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage(getResources().getString(R.string.fetching_profile));
        progressDialog.setCancelable(false);
        progressDialog.show();

        String email = PreferencesUtility.getMygovOauthEmail(ProfileActivity.this);
        try {
            email = URLEncoder.encode(email, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String mobileNumber = PreferencesUtility.getPrefMobileNumber(ProfileActivity.this);

        if (email != null && email.length() > 0) {

            SanskritClient.getInstance(this).getProfileDOADOBFromEmail(email, new GenericCallback() {

                public void failure() {
                    progressDialog.dismiss();
                }

                @Override
                public void success(Object response) {
                    //   final String response = (String) obj;
                    MyPojo myPojo = (MyPojo) response;
                    progressDialog.dismiss();
                    try {
                        PreferencesUtility.setServerDOA(ProfileActivity.this, myPojo.getDOA());
                        PreferencesUtility.setServerDOB(ProfileActivity.this, myPojo.getDOB());
                        // setDOB(myPojo.getDOB());
                        //setDOA(myPojo.getDOA());
                        PreferencesUtility.setPrefNotificationOnOf(ProfileActivity.this, myPojo.getNotifs_on_off());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
        } else if (mobileNumber != null && mobileNumber.length() > 0) {
            SanskritClient.getInstance(this).getProfileDOADOBFromMobile(mobileNumber, new GenericCallback() {

                public void failure() {
                    progressDialog.dismiss();
                }

                @Override
                public void success(Object response) {
                    //   final String response = (String) obj;
                    MyPojo myPojo = (MyPojo) response;

                    progressDialog.dismiss();
                    try {
                        PreferencesUtility.setServerDOA(ProfileActivity.this, myPojo.getDOA());
                        PreferencesUtility.setServerDOB(ProfileActivity.this, myPojo.getDOB());
                        //setDOB(myPojo.getDOB());
                        //setDOA(myPojo.getDOA());
                        PreferencesUtility.setPrefNotificationOnOf(ProfileActivity.this, myPojo.getNotifs_on_off());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
        }

    }

}
