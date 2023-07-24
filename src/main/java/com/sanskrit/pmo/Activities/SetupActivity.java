package com.sanskrit.pmo.Activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.callbacks.ResponseListener;
import com.sanskrit.pmo.network.server.SanskritClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import retrofit.client.Response;

public abstract class SetupActivity extends AppCompatActivity {
    private static int FIRST_PAGE_NUM = 0;
    Context applicationContext;
    String doa = "";
    String dob = "";
    private List<ImageView> dots;
    private List<Fragment> fragments = new Vector();
    private boolean isVibrateOn = false;
    char[] latitide = new char[0];
    char[] logitude = new char[0];
    private PagerAdapter mPagerAdapter;
    private Vibrator mVibrator;
    private ViewPager pager;
    ProgressDialog progressDialog;
    private boolean showSkip = true;
    private int slidesNumber;
    private int vibrateIntensity = 20;


    protected final void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_appsetup);
        final TextView skipButton = (TextView) findViewById(R.id.skip);
        final ImageView nextButton = (ImageView) findViewById(R.id.next);
        final ImageView doneButton = (ImageView) findViewById(R.id.done);
        this.mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        this.applicationContext = this;

        this.progressDialog = new ProgressDialog(this.applicationContext);
        skipButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SetupActivity.this.pager.getCurrentItem() == 2) {
                    if (SetupActivity.this.isVibrateOn) {
                        SetupActivity.this.mVibrator.vibrate((long) SetupActivity.this.vibrateIntensity);
                    }
                    SetupActivity.this.onSkipPressed();
                    return;
                }
                SetupActivity.this.pager.setCurrentItem(SetupActivity.this.pager.getCurrentItem() + 1);
            }
        });
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SetupActivity.this.isVibrateOn) {
                    SetupActivity.this.mVibrator.vibrate((long) SetupActivity.this.vibrateIntensity);
                }
                SetupActivity.this.pager.setCurrentItem(SetupActivity.this.pager.getCurrentItem() + 1);
            }
        });
        doneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SetupActivity.this.isVibrateOn) {
                    SetupActivity.this.mVibrator.vibrate((long) SetupActivity.this.vibrateIntensity);
                }
                SetupActivity.this.onDonePressed();
            }
        });
        this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), this.fragments);
        this.pager = (ViewPager) findViewById(R.id.view_pager);
        this.pager.setAdapter(this.mPagerAdapter);
        this.pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                SetupActivity.this.selectDot(position);
                if (position == SetupActivity.this.slidesNumber - 1) {
                    skipButton.setVisibility(View.INVISIBLE);
                    nextButton.setVisibility(View.GONE);
                    doneButton.setVisibility(View.VISIBLE);
                } else {
                    skipButton.setVisibility(View.VISIBLE);
                    doneButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.VISIBLE);
                }
                if (!SetupActivity.this.showSkip) {
                    skipButton.setVisibility(View.INVISIBLE);
                }
                switch (position) {
                    case 0:
                        skipButton.setVisibility(View.VISIBLE);
                        return;
                    case 1:
                        skipButton.setVisibility(View.VISIBLE);
                        return;
                    case 2:
                        skipButton.setVisibility(View.VISIBLE);
                        return;
                    default:
                        return;
                }
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
        init(savedInstanceState);
        loadDots();
        doneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SetupActivity.this.progressDialog.setMessage(SetupActivity.this.getString(R.string.updating_profile));
                SetupActivity.this.progressDialog.setCancelable(false);
                SetupActivity.this.progressDialog.show();
                SetupActivity.this.setNewDates();
            }
        });
    }

    private void loadDots() {
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        this.dots = new ArrayList();
        this.slidesNumber = this.fragments.size();
        for (int i = 0; i < this.slidesNumber; i++) {
            ImageView dot = new ImageView(this);
            dot.setImageDrawable(getResources().getDrawable(R.drawable.indicator_dot_grey));
            dotLayout.addView(dot, new LayoutParams(-2, -2));
            this.dots.add(dot);
        }
        selectDot(FIRST_PAGE_NUM);
    }


    public void selectDot(int index) {
        Resources res = getResources();
        int i = 0;
        while (i < this.fragments.size()) {
            ((ImageView) this.dots.get(i)).setImageDrawable(res.getDrawable(i == index ? R.drawable.indicator_dot_white : R.drawable.indicator_dot_grey));
            i++;
        }
    }

    public void addSlide(Fragment fragment, Context context) {
        this.fragments.add(Fragment.instantiate(context, fragment.getClass().getName()));
        this.mPagerAdapter.notifyDataSetChanged();
    }

    public void setBarColor(int color) {
        ((LinearLayout) findViewById(R.id.bottom)).setBackgroundColor(color);
    }

    public void setSeparatorColor(int color) {
        (findViewById(R.id.bottom_separator)).setBackgroundColor(color);
    }

    public void setSkipText(String text) {
        ((TextView) findViewById(R.id.skip)).setText(text);
    }

    public void showSkipButton(boolean showButton) {
        this.showSkip = showButton;
        if (!showButton) {
            ((TextView) findViewById(R.id.skip)).setVisibility(View.INVISIBLE);
        }
    }

    public void setVibrate(boolean vibrate) {
        this.isVibrateOn = vibrate;
    }

    public void setVibrateIntensity(int intensity) {
        this.vibrateIntensity = intensity;
    }

    public void setDOA(String doa) {
        this.doa = doa;
    }

    public void setDOB(String dob) {
        this.dob = dob;
    }

    public void setLATLNG(char[] lat, char[] lon) {
        this.latitide = lat;
        this.logitude = lon;
    }

    public void setNewDates() {
        /*try {
            SanskritClient.getInstance(this).getRequestToken(Utils.getPermaToken(this), new RequestTokenListener() {
                @Override
                public void failure() {
                    SetupActivity.this.setNewLocation();
                }

                @Override
                public void success(RequestToken token) {
                    if (token.mErrorResponse == null) {
                        SetupActivity.this.setDates(token.mToken);
                        return;
                    }
                    SetupActivity.this.setNewLocation();
                    Log.d("lol11", "errorrrrr");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.some_error_occurred), Toast.LENGTH_SHORT).show();
        }*/
        SetupActivity.this.setDates(PreferencesUtility.getFCMID(getApplicationContext()), latitide.toString(), logitude.toString());
    }

    private void setDates(String token, String lat, String lng) {
        //Code Added
        if (this.doa != null && this.doa.trim().length() > 0)
            PreferencesUtility.setServerDOA(applicationContext, this.doa);
        if (this.dob != null && this.dob.trim().length() > 0)
            PreferencesUtility.setServerDOB(applicationContext, this.dob);
        //
        SanskritClient.getInstance(this).setNewDates(token, lat, lng, PreferencesUtility.getMygovOauthEmail(this), PreferencesUtility.getPrefMobileNumber(this), this.doa, this.dob, new ResponseListener() {
            @Override
            public void failure() {
                // SetupActivity.this.setNewLocation();
                SetupActivity.this.loadMainActivity();

            }

            @Override
            public void success(Response response) {
                //SetupActivity.this.setNewLocation();
                SetupActivity.this.loadMainActivity();
            }
        });


    }

/*
    public void setNewLocation() {
        try {
            if (this.latitide.equals("") || this.logitude.equals("")) {
                loadMainActivity();
            } else {
                SanskritClient.getInstance(this).getRequestToken(Utils.getPermaToken(this), new RequestTokenListener() {
                    @Override
                    public void failure() {
                        SetupActivity.this.loadMainActivity();
                    }

                    @Override
                    public void success(RequestToken token) {
                        if (token.mErrorResponse == null) {
                            SetupActivity.this.setLocation(token.mToken);
                            return;
                        }
                        SetupActivity.this.loadMainActivity();
                        Log.d("lol11", "errorrrrr");
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            loadMainActivity();
        }
    }
*/

    private void loadMainActivity() {
        this.progressDialog.dismiss();
        PreferencesUtility.setLastLocationTime(this, System.currentTimeMillis());
        PreferencesUtility.setProfileFirstRun(this, false);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

   /* private void setLocation(String token) {
        SanskritClient.getInstance(this).setUserLocation(token, Utils.urlEncodeString(String.valueOf(this.latitide)), Utils.urlEncodeString(String.valueOf(this.logitude)), new ResponseListener() {
            @Override
            public void failure() {
                SetupActivity.this.loadMainActivity();
            }

            @Override
            public void success(Response response) {
                PreferencesUtility.setLastLocationTime(SetupActivity.this, System.currentTimeMillis());
                SetupActivity.this.loadMainActivity();
            }


        });

        Arrays.fill(this.latitide, '0');
        Arrays.fill(this.logitude, '0');
    }*/


    public class PagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        public Fragment getItem(int position) {
            return (Fragment) this.fragments.get(position);
        }

        public int getCount() {
            return this.fragments.size();
        }
    }

    public abstract void init(Bundle bundle);

    public abstract void onDonePressed();

    public abstract void onSkipPressed();
}
