package com.sanskrit.pmo.Activities;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.MyGovCacheClient;
import com.sanskrit.pmo.network.mygov.PrivacyPolicy;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;

import java.util.List;

public class PrivacyPolicyActivity extends BaseActivity {
    private Toolbar toolbar;

    TextView tvPrivacyPolicy;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.searchMenuVisible = false;
        this.notifMenuVisible = false;

        getSupportActionBar().setTitle((int) R.string.drawer_item_privacy_policy);

        inItView();

        progressbar.setVisibility(View.VISIBLE);

        MyGovCacheClient.getInstance(this).getPrivacyPolicy(PreferencesUtility.getLanguagePrefernce(this), new GenericCallback() {


            @Override
            public void success(Object response) {
                progressbar.setVisibility(View.GONE);
                List<PrivacyPolicy> privacyPolicies = (List<PrivacyPolicy>) response;
                Spanned result;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    result = HtmlCompat.fromHtml(privacyPolicies.get(0).getPost_content().replaceAll("\\n", "<br>"), HtmlCompat.FROM_HTML_MODE_LEGACY);

                } else {
                    result = HtmlCompat.fromHtml(privacyPolicies.get(0).getPost_content().replaceAll("\\n", "<br>"), HtmlCompat.FROM_HTML_MODE_LEGACY);
                }
                tvPrivacyPolicy.setText(result);
                tvPrivacyPolicy.setLinksClickable(true);
                tvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

            }

            @Override
            public void failure() {
                progressbar.setVisibility(View.GONE);

            }
        });
    }

    private void inItView() {

        tvPrivacyPolicy = findViewById(R.id.tv_privacy_policy);
        progressbar = findViewById(R.id.progressbar);


    }

    // http://api.pmindia.gov.in/privacy-policy/en/1
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return false;
    }
}
