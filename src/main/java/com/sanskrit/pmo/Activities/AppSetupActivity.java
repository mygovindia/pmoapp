package com.sanskrit.pmo.Activities;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sanskrit.pmo.Fragments.AnniversaryPickerFragment;
import com.sanskrit.pmo.Fragments.BirthdayPickerFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;


public class AppSetupActivity extends SetupActivity {

    public void init(Bundle savedInstanceState) {


        addSlide(new BirthdayPickerFragment(), getApplicationContext());
        addSlide(new AnniversaryPickerFragment(), getApplicationContext());
        // addSlide(new LocationPickerFragment(), getApplicationContext());
    }

    private void loadMainActivity() {
        PreferencesUtility.setProfileFirstRun(this, false);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void onSkipPressed() {
        final Builder builder = new Builder(this);
        builder.setTitle(R.string.skip);
        builder.setMessage(getString(R.string.skip_setup_message) + getString(R.string.skip_setup_message2));
        builder.setPositiveButton(R.string.skip, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppSetupActivity.this.loadMainActivity();
            }
        });
        builder.setNegativeButton(R.string.cancel, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });
        builder.create().show();
    }

    public void onDonePressed() {
        loadMainActivity();
    }

    public void getStarted(View v) {
        loadMainActivity();
    }
}
