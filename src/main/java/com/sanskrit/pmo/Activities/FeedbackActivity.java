package com.sanskrit.pmo.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.callbacks.ResponseListener;
import com.sanskrit.pmo.network.server.SanskritClient;
import com.sanskrit.pmo.uiwidgets.NumberRatingBar;
import com.sanskrit.pmo.utils.Utils;

import retrofit.client.Response;

public class FeedbackActivity extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((int) R.string.feedback);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final NumberRatingBar numberRatingBar2 = (NumberRatingBar) findViewById(R.id.rating_bar_2);
        final NumberRatingBar numberRatingBar1 = (NumberRatingBar) findViewById(R.id.rating_bar_1);
        final NumberRatingBar numberRatingBar3 = (NumberRatingBar) findViewById(R.id.rating_bar_3);
        final NumberRatingBar numberRatingBar4 = (NumberRatingBar) findViewById(R.id.rating_bar_4);
        final EditText feedback = (EditText) findViewById(R.id.feedback);
        this.searchMenuVisible = false;
        this.notifMenuVisible = false;


        FrameLayout button = (FrameLayout) findViewById(R.id.submit_feedback_button);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    if (numberRatingBar1.getProgress() == 0 || numberRatingBar2.getProgress() == 0 || numberRatingBar3.getProgress() == 0 || numberRatingBar4.getProgress() == 0) {
                        Toast.makeText(FeedbackActivity.this, R.string.error_rate_all_feedback, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    final ProgressDialog progressDialog = new ProgressDialog(FeedbackActivity.this);
                    progressDialog.setMessage(FeedbackActivity.this.getString(R.string.submitting_feedback));
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                    SanskritClient.getInstance(FeedbackActivity.this).submitFeedback(String.valueOf(feedback.getText()), PreferencesUtility.getMygovOauthEmail(FeedbackActivity.this), String.valueOf(numberRatingBar1.getProgress()), String.valueOf(numberRatingBar2.getProgress()), String.valueOf(numberRatingBar3.getProgress()), String.valueOf(numberRatingBar4.getProgress()), Utils.getIpAddress(FeedbackActivity.this), new ResponseListener() {
                        public void success(Response response) {

                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(FeedbackActivity.this, R.string.feedback_submitted, Toast.LENGTH_SHORT).show();
                            FeedbackActivity.this.finish();
                        }

                        public void failure() {
                            if (progressDialog != null && progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(FeedbackActivity.this, R.string.feedback_error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
