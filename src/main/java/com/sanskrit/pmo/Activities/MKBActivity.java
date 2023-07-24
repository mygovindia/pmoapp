
package com.sanskrit.pmo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sanskrit.pmo.Fragments.MKBAudioFragment;
import com.sanskrit.pmo.Fragments.MKBVideoFragment;
import com.sanskrit.pmo.R;


public class MKBActivity extends BaseActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_mkb);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle((int) R.string.mann_ki_baat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getAction().equals("Audio")) {
            this.notifMenuVisible = false;
            invalidateOptionsMenu();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new MKBAudioFragment()).commit();
        } else if (getIntent().getAction().equals("Video")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new MKBVideoFragment()).commit();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            case R.id.action_search:
                // Toast.makeText(QuotesActivity.this, "call", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SearchMKBActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}