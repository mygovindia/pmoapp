package com.sanskrit.pmo.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;


//import android.support.customtabs.CustomTabsIntent.Builder;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.digits.sdk.android.BuildConfig;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sanskrit.pmo.App.Config;
import com.sanskrit.pmo.App.NotificationController;

import com.sanskrit.pmo.Fragments.HomeFragment;
import com.sanskrit.pmo.Models.NotificationInfo;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.async.SubmitFCMTokenAsync;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.pushnotify.PushActionActivity;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.IOUtils;
import com.sanskrit.pmo.utils.NavigationUtils;
import com.sanskrit.pmo.webview.CustomTabActivityHelper;
import com.sanskrit.pmo.webview.WebviewFallback;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    private static final int REQUEST_READ_PHONE_STATE = 101;
    private static final int REQUEST_MULTIPLE_PERMISSION = 102;
    GoogleCloudMessaging gcmObj;
    View headerView;
    private CustomTabActivityHelper mCustomTabActivityHelper;
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    int playServicesAvailable = 0;
    String regId = null;
    private List<String> enabledFilters;
    TextView name, email, appVersionTxt;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String fcmID;
    public static String IMEI;
    private TelephonyManager telephonyManager;
    private String androidId;

    public void onCreate(Bundle savedInstanceState) {
        setFullscreen(false);
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        setupBundle();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        this.appVersionTxt = (TextView) findViewById(R.id.app_version_txt);
        this.appVersionTxt.setText("v " + BuildConfig.VERSION_NAME);
        this.headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, this.navigationView, false);
        this.navigationView.addHeaderView(this.headerView);
        if (this.navigationView != null) {
            setupDrawerContent(this.navigationView);
        }


        androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        PreferencesUtility.setUniqueDeviceId(MainActivity.this, androidId);

        Log.d("Taoken", "onCreates: " + PreferencesUtility.getUniqueDeviceId(MainActivity.this));
        settingMenuVisible = true;
        notifMenuVisible = true;
        langMenuVisible = true;

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        setupCustomTabHelper();
        IOUtils.saveArrayToPreferences(MainActivity.this, new ArrayList<String>());

        if (PreferencesUtility.getTheme(this).equals("dark")) {

        } else if (PreferencesUtility.getTheme(this).equals("black")) {

        } else
            navigationView.getMenu().getItem(0).setChecked(true);


        // navigationView.setItemBackground(ContextCompat.getDrawable(MainActivity.this, android.R.color.transparent));

        if (getIntent().getAction() != null && getIntent().getAction().equals("pushaction")) {
            if (getIntent().getStringExtra("type").equals("news")) {
                NewsFeed newsFeed = new NewsFeed();
                newsFeed.mId = getIntent().getStringExtra("id");
                newsFeed.mTitle = getIntent().getStringExtra("title");
                NavigationUtils.navigateToNewsDetail(this, newsFeed, true);
            } else if (getIntent().getStringExtra("type").equals("poll")) {
                startActivity(new Intent(this, PollsActivity.class));
            } else {
                Intent intent = new Intent(this, PushActionActivity.class);
                String type = getIntent().getStringExtra("type");

                if (type.equals("anniversary")) {
                    intent.setAction("anniversary");
                } else if (type.equals("aux")) {
                    intent.setAction("aux");
                } else if (type.equals("birthday")) {
                    intent.setAction("birthday");

                } else if (type.equals("livestream")) {
                    intent.setAction("livestream");
                } else if (type.equals("location")) {
                    intent.setAction("location");
                }


                if (getIntent().getStringExtra("link") != null) {
                    intent.putExtra("link", getIntent().getStringExtra("link"));
                }
                startActivity(intent);
            }
        }
        /*if (!PreferencesUtility.getGcmRegistered(this)) {
            try {
                registerToGcmInBackground();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
      /*  if (PreferencesUtility.getLocationPushesEnabled(this)) {
            updateUserLocation();
        }*/

        setupReceiver();
        showFirebaseRegID();


    }

    private void setupBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(Constants.NOTIFICATION_INFO)) {
            NotificationInfo info = (NotificationInfo) bundle.getSerializable(Constants.NOTIFICATION_INFO);
            NotificationController controller = new NotificationController(this, info);
            Intent intent = controller.getResultIntent();
            startActivity(intent);
        }
    }

    private void submitFCMID() {
        SubmitFCMTokenAsync async = new SubmitFCMTokenAsync(this);
        async.execute(fcmID, IMEI);
    }

   /* @SuppressLint("MissingPermission")
    private void getDeviceDetails() {
        IMEI = telephonyManager.getDeviceId();
        if (IMEI == null || IMEI.length() == 0) {
            IMEI = "000000000000000";
        }
        Log.v(TAG, "IMEI: " + IMEI);
        PreferencesUtility.setIMEI(MainActivity.this, IMEI);
        if (!PreferencesUtility.isFCMSubmitted(this) && !PreferencesUtility.getMygovLoggedIn(this)) {
            Log.v(TAG, "IS_SUBMITTED: FALSE");
            submitFCMID();
        } else {
            Log.v(TAG, "IS_SUBMITTED: TRUE");
        }
    }*/

    private void setupReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    showFirebaseRegID();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };
    }

    private void showFirebaseRegID() {
        //SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        //fcmID = pref.getString("regId", null);
        fcmID = PreferencesUtility.getFCMID(getApplicationContext());
        Log.e(TAG, "Firebase reg id: " + fcmID);
        if (TextUtils.isEmpty(fcmID)) {
            Log.e(TAG, "Firebase Reg Id is not received yet!");
        }
    }

    protected void onStart() {
        super.onStart();
        this.mCustomTabActivityHelper.bindCustomTabsService(this);
    }

    protected void onStop() {
        super.onStop();
        this.mCustomTabActivityHelper.unbindCustomTabsService(this);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.mDrawerLayout.openDrawer((int) GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                MainActivity.this.updatePosition(menuItem);
                return true;
            }
        });
    }

    private void updatePosition(MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {

            case R.id.nav_home:
                //navigationView.getMenu().getItem(0).setChecked(false);
                this.mDrawerLayout.closeDrawers();
                //IOUtils.saveArrayToPreferences(MainActivity.this,new ArrayList<String>());
                IOUtils.saveArrayToPreferences(MainActivity.this, new ArrayList<String>());

                fragment = new HomeFragment();
                break;
            case R.id.nav_gallery:
                this.mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, GalleryActivity.class));
                break;
            case R.id.nav_former_pm:
                this.mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, FormerPMActivity.class));
                break;
            case R.id.nav_track_record:
                this.mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, TrackRecordActivity.class));
                break;
            case R.id.nav_about_pm:
                this.mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, AboutPMActivity.class));
                break;
            case R.id.nav_mkb:
                this.mDrawerLayout.closeDrawers();
                Intent intent = new Intent(this, MKBActivity.class);
                intent.setAction("Audio");
                startActivity(intent);
                break;
            case R.id.nav_infographics:
                this.mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, InfographicsActivity.class));
                break;
            case R.id.nav_quotes:
                this.mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, QuotesActivity.class));
                break;
            case R.id.donate_pm:
                this.mDrawerLayout.closeDrawers();
                launchCustomTab("https://www.pmindia.gov.in/en/pms-funds/");
                break;
            case R.id.donate_ndf:
                this.mDrawerLayout.closeDrawers();
                launchCustomTab("https://www.pmindia.gov.in/en/national-defence-fund/");
                break;
            case R.id.interact_pm:
                this.mDrawerLayout.closeDrawers();
                launchCustomTab("https://pmindia.gov.in/en/interact-with-honble-pm/");
                break;
            case R.id.privacy_policy:
                this.mDrawerLayout.closeDrawers();
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                //launchCustomTab("http://pmindia.gov.in/en/interact-with-honble-pm/");
                break;

        }
        if (fragment != null) {
            menuItem.setChecked(true);

            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    transaction.commit();
                }
            }, 350);
        }
    }

    private void launchCustomTab(String url) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
      //  Builder intentBuilder =CustomTabsIntent.Builder()
        intentBuilder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        intentBuilder.setShowTitle(true);
        CustomTabActivityHelper.openCustomTab(this, intentBuilder.build(), Uri.parse(url), new WebviewFallback(), false);
    }

    private void setupCustomTabHelper() {
        this.mCustomTabActivityHelper = new CustomTabActivityHelper();
        this.mCustomTabActivityHelper.setConnectionCallback(this.mConnectionCallback);
    }

    private void setProfileInHeader(View headerView) {
        name = (TextView) headerView.findViewById(R.id.username);
        email = (TextView) headerView.findViewById(R.id.email);
        View header = headerView.findViewById(R.id.rootNavHeader);
        ImageView editProfile = (ImageView) headerView.findViewById(R.id.edit_profile);
        if (PreferencesUtility.getMygovLoggedIn(this)) {
            name.setText(PreferencesUtility.getMygovOauthName(this));
            email.setText(PreferencesUtility.getMygovOauthEmail(this));
        } else {
            name.setText(getString(R.string.login_view_profile));
            email.setText(getString(R.string.login_view_profile));
        }
        header.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferencesUtility.getMygovLoggedIn(MainActivity.this)) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                } else {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, NativeLoginActivity.class));
                }
            }
        });
        editProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferencesUtility.getMygovLoggedIn(MainActivity.this)) {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                } else {
                    MainActivity.this.startActivity(new Intent(MainActivity.this, NativeLoginActivity.class));
                }
            }
        });
    }

    protected void onResume() {
        super.onResume();
        try {
            setProfileInHeader(this.headerView);
            if (PreferencesUtility.getMygovLoggedIn(this)) {

                if (name != null && email != null) {
                    name.setText(PreferencesUtility.getMygovOauthName(this));
                    email.setText(PreferencesUtility.getMygovOauthEmail(this));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        if (!PreferencesUtility.isFCMSubmitted(this) && !PreferencesUtility.getMygovLoggedIn(this)) {
            SubmitFCMTokenAsync async = new SubmitFCMTokenAsync(this);
            async.execute(fcmID, PreferencesUtility.getUniqueDeviceId(MainActivity.this));
        }

        /*if (checkPermissionStatus()) {
            getDeviceDetails();
        }*/


        //  if(mDrawerLayout.isDrawerOpen())
      /*  if(mDrawerLayout!=null)
        this.mDrawerLayout.closeDrawers();*/

        // Firebase
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        //NotificationUtils.clearNotifications(getApplicationContext());
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
    }*/

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null && (fragment instanceof HomeFragment)) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CustomTabActivityHelper.ConnectionCallback mConnectionCallback = new CustomTabActivityHelper.ConnectionCallback() {
        @Override
        public void onCustomTabsConnected() {

        }
        @Override
        public void onCustomTabsDisconnected() {

        }
    };


    /*class Register extends AsyncTask<Void, Void, String> {
        Register() {
        }

        protected String doInBackground(Void... params) {
            String msg = " ";
            try {
                if (MainActivity.this.playServicesAvailable == 0) {
                    if (MainActivity.this.gcmObj == null) {
                        MainActivity.this.gcmObj = GoogleCloudMessaging.getInstance(MainActivity.this);
                    }
                    MainActivity.this.regId = MainActivity.this.gcmObj.register("339180625670");
                    return "Registration ID :" + MainActivity.this.regId;
                }
                MainActivity.this.regId = null;
                return msg;
            } catch (IncompatibleClassChangeError ex) {
                // Crashlytics.logException(ex);
                return "Error";
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }

        protected void onPostExecute(String msg) {
            if (MainActivity.this.regId != null) {
                MainActivity.this.registerGCM(MainActivity.this.regId);
            }
        }
    }


    private void registerGCM(final String key) {
        SanskritClient.getInstance(this).getRequestToken(Utils.getPermaToken(this), new RequestTokenListener() {
            public void success(RequestToken token) {
                if (token.mErrorResponse == null) {
                    MainActivity.this.registerGCM(key, token.mToken);
                }
            }

            public void failure() {
            }
        });
    }

    private void registerGCM(final String key, String token) {
        SanskritClient.getInstance(this).registerGCMKey(key, token, new ResponseListener() {
            public void success(Response response) {
                PreferencesUtility.setGcmRegistered(MainActivity.this, true);
                PreferencesUtility.setGCMKey(MainActivity.this, key);
                Log.d(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE, "Registered with GCM");
            }

            public void failure() {
                Log.d(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE, "Error registering with GCM");
            }
        });
    }

    private void registerToGcmInBackground() {
        new Register().execute(new Void[]{null, null, null});
    }*/


    @Override
    public void onBackPressed() {

        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else
                super.onBackPressed();

        }
    }

}
