package com.sanskrit.pmo.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.collection.SparseArrayCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.nineoldandroids.view.ViewHelper;
import com.sanskrit.pmo.Fragments.FormerPMFragment;
import com.sanskrit.pmo.Fragments.ShareFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.ShareUtils;
import com.sanskrit.pmo.Utils.Util;
import com.sanskrit.pmo.network.mygov.MyGovCacheClient;
import com.sanskrit.pmo.network.mygov.callbacks.FormerPMListener;
import com.sanskrit.pmo.network.mygov.models.FormerPM;
import com.sanskrit.pmo.parallaxheader.AlphaForegroundColorSpan;
import com.sanskrit.pmo.parallaxheader.ScrollTabHolder;
import com.sanskrit.pmo.parallaxheader.ScrollTabHolderFragment;
import com.sanskrit.pmo.permissions.Nammu;
import com.sanskrit.pmo.permissions.PermissionCallback;
import com.sanskrit.pmo.utils.ShareObject;
import com.sanskrit.pmo.utils.TintHelper;
import com.sanskrit.pmo.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class FormerPMActivity extends BaseActivity implements ScrollTabHolder, ViewPager.OnPageChangeListener {
    private static AccelerateDecelerateInterpolator sSmoothInterpolator = new AccelerateDecelerateInterpolator();
    FloatingActionButton fabShare;
    private View foreground;
    public List<FormerPM> formerPms = new ArrayList();
    private ImageView icon;
    ImageView imageView;
    private int mActionBarHeight;
    private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
    private View mHeader;
    private int mHeaderHeight;
    private ImageView mHeaderLogo;
    private int mMinHeaderHeight;
    private int mMinHeaderTranslation;
    private PagerAdapter mPagerAdapter;
    private TabLayout mPagerSlidingTabStrip;
    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();
    private SpannableString mSpannableString;
    private TypedValue mTypedValue = new TypedValue();
    private ViewPager mViewPager;
    private ProgressBar progressBar;
    private TextView title;
    private Toolbar toolbar;
    private String imagePath;
    private Uri shareUri;


    public class PagerAdapter extends FragmentPagerAdapter {
        private ScrollTabHolder mListener;
        private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders = new SparseArrayCompat();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void setTabHolderScrollingContent(ScrollTabHolder listener) {
            this.mListener = listener;
        }

        public CharSequence getPageTitle(int position) {
            return ((FormerPM) FormerPMActivity.this.formerPms.get(position)).mTitle;
        }

        public int getCount() {
            return FormerPMActivity.this.formerPms.size();
        }

        public Fragment getItem(int position) {
            ScrollTabHolderFragment fragment = (ScrollTabHolderFragment) FormerPMFragment.newInstance(position);
            this.mScrollTabHolders.put(position, fragment);
            if (this.mListener != null) {
                fragment.setScrollTabHolder(this.mListener);
            }
            return fragment;
        }

        public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
            return this.mScrollTabHolders;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        // setFullscreen(true);
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_former_pm);
        this.mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        this.mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        this.mMinHeaderTranslation = (-this.mMinHeaderHeight) + getActionBarHeight();
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.imageView = (ImageView) findViewById(R.id.header_picture);
        this.foreground = findViewById(R.id.foreground);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        this.icon = (ImageView) findViewById(R.id.icon);
        this.title = (TextView) findViewById(R.id.title);
        this.mPagerSlidingTabStrip = (TabLayout) findViewById(R.id.tabs);
        this.mSpannableString = new SpannableString("");
        this.mHeaderLogo = (ImageView) findViewById(R.id.header_logo);
        this.mHeader = findViewById(R.id.header);
        this.mViewPager = (ViewPager) findViewById(R.id.pager);
        this.mViewPager.setOffscreenPageLimit(2);
        this.mViewPager.addOnPageChangeListener(this);
        this.mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(-1);
        ViewHelper.setAlpha(getActionBarIconView(), 0.0f);
        getSupportActionBar().setBackgroundDrawable(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((int) R.string.former_pms);
        this.mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        this.mPagerAdapter.setTabHolderScrollingContent(this);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mPagerSlidingTabStrip.setupWithViewPager(this.mViewPager);
        this.fabShare = (FloatingActionButton) findViewById(R.id.fab);
        fetchData();
        if (PreferencesUtility.getTheme(this).equals("black")) {
            this.fabShare.setImageDrawable(TintHelper.tintDrawable(this.fabShare.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));
        }
    }

    private void fetchData() {
        MyGovCacheClient.getInstance(this).getFormerPMs(PreferencesUtility.getLanguagePrefernce(this), new FormerPMListener() {

            @Override
            public void success(List<FormerPM> response) {
                FormerPMActivity.this.formerPms = response;
                //Collections.reverse(formerPms);

                FormerPMActivity.this.updateAdapter();
                FormerPMActivity.this.updateHeaderImage(FormerPMActivity.this.mViewPager.getCurrentItem());
                FormerPMActivity.this.progressBar.setVisibility(View.GONE);
                FormerPMActivity.this.fabShare.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (Utils.isMarshmallow()) {
//                            checkPermissionAndThenShare();
//                        } else {
                            share();
//                        }
                    }
                });
            }

            @Override
            public void failure() {

            }

        });
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void checkPermissionAndThenShare() {
//        if (Nammu.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
//            share();
//        } else if (Nammu.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
//            Snackbar.make(findViewById(R.id.container), getString(R.string.storage_permission_required), Snackbar.LENGTH_INDEFINITE).setAction((CharSequence) "OK", new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Nammu.askForPermission(FormerPMActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE", FormerPMActivity.this.permissionReadstorageCallback);
//
//                }
//            }).show();
//        } else {
//            Nammu.askForPermission((Activity) this, "android.permission.WRITE_EXTERNAL_STORAGE", this.permissionReadstorageCallback);
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//
//    final PermissionCallback permissionReadstorageCallback = new PermissionCallback() {
//        @Override
//        public void permissionGranted() {
//            share();
//        }
//
//        @Override
//        public void permissionRefused() {
//            Snackbar.make(findViewById(R.id.container), FormerPMActivity.this.getString(R.string.storage_permission_required), Snackbar.LENGTH_SHORT).show();
//        }
//    };

    private void share() {

        // Delete Downloaded Image
        if (shareUri != null)
            Util.deleteFileFromUri(FormerPMActivity.this, shareUri);

        final ShareObject shareObject = new ShareObject();

        Picasso.with(FormerPMActivity.this).load(imagePath).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                //shareUri = getImageUri(FormerPMActivity.this,bitmap);

                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", null);
                shareUri = Uri.parse(path);
                shareObject.setImageUri(shareUri);


                try {
                    shareObject.setTitle(((FormerPM) FormerPMActivity.this.formerPms.get(FormerPMActivity.this.mViewPager.getCurrentItem())).mTitle);
                    shareObject.setUrl(((FormerPM) FormerPMActivity.this.formerPms.get(FormerPMActivity.this.mViewPager.getCurrentItem())).mLink);
                    ShareUtils su= new ShareUtils(FormerPMActivity.this,shareObject);
                    su.shareToFacebookViaApp();
//                    ShareFragment.newInstance(shareObject).show(FormerPMActivity.this.getSupportFragmentManager(), "Share Fragment");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void updateAdapter() {
        this.mPagerAdapter.notifyDataSetChanged();
        this.mPagerAdapter.setTabHolderScrollingContent(this);
    }

    private void updateHeaderImage(int position) {
        this.imageView.setImageDrawable(null);
        if (this.formerPms.size() != 0) {
            Picasso.with(this).load(((FormerPM) this.formerPms.get(position)).mImage).into(this.imageView);

            imagePath = formerPms.get(position).mImage;
        }
    }

    public void onPageScrollStateChanged(int arg0) {
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        try {
            ((ScrollTabHolder) this.mPagerAdapter.getScrollTabHolders().valueAt(position)).adjustScroll((int) (((float) this.mHeader.getHeight()) + ViewHelper.getTranslationY(this.mHeader)), this.mHeaderHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateHeaderImage(position);
    }

    public void onScroll(ScrollView view, int x, int y, int oldX, int oldY, int pagePosition) {
        if (this.mViewPager.getCurrentItem() == pagePosition) {
            ViewHelper.setTranslationY(this.mHeader, (float) Math.max(-view.getScrollY(), this.mMinHeaderTranslation));
            float ratio = clamp(ViewHelper.getTranslationY(this.mHeader) / ((float) this.mMinHeaderTranslation), 0.0f, 1.0f);
            //interpolate(this.mHeaderLogo, getActionBarIconView(), sSmoothInterpolator.getInterpolation(ratio));
            setTitleAlpha(clamp((5.0f * ratio) - 4.0f, 0.0f, 1.0f));
        }
    }

    public void adjustScroll(int scrollHeight, int headerTranslationY) {
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }

    private void interpolate(View view1, View view2, float interpolation) {
        getOnScreenRect(this.mRect1, view1);
        getOnScreenRect(this.mRect2, view2);
        float scaleX = 1.0f + (((this.mRect2.width() / this.mRect1.width()) - 1.0f) * interpolation);
        float scaleY = 1.0f + (((this.mRect2.height() / this.mRect1.height()) - 1.0f) * interpolation);
        float translationY = 0.5f * ((((this.mRect2.top + this.mRect2.bottom) - this.mRect1.top) - this.mRect1.bottom) * interpolation);
        ViewHelper.setTranslationX(view1, 0.5f * ((((this.mRect2.left + this.mRect2.right) - this.mRect1.left) - this.mRect1.right) * interpolation));
        ViewHelper.setTranslationY(view1, translationY - ViewHelper.getTranslationY(this.mHeader));
        ViewHelper.setScaleX(view1, scaleX);
        ViewHelper.setScaleY(view1, scaleY);
    }

    private RectF getOnScreenRect(RectF rect, View view) {
        rect.set((float) view.getLeft(), (float) view.getTop(), (float) view.getRight(), (float) view.getBottom());
        return rect;
    }

    @TargetApi(11)
    public int getActionBarHeight() {
        if (this.mActionBarHeight != 0) {
            return this.mActionBarHeight;
        }
        if (VERSION.SDK_INT > 11) {
            getTheme().resolveAttribute(16843499, this.mTypedValue, true);
        } else {
            getTheme().resolveAttribute(R.attr.actionBarSize, this.mTypedValue, true);
        }
        this.mActionBarHeight = TypedValue.complexToDimensionPixelSize(this.mTypedValue.data, getResources().getDisplayMetrics());
        return this.mActionBarHeight;
    }

    private void setTitleAlpha(float alpha) {
        this.mAlphaForegroundColorSpan.setAlpha(alpha);
        this.mSpannableString.setSpan(this.mAlphaForegroundColorSpan, 0, this.mSpannableString.length(), 33);
        this.title.setText(this.mSpannableString);
    }

    private ImageView getActionBarIconView() {
        return this.icon;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_search:
                // Toast.makeText(FormerPMActivity.this, "call", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SearchFormerPMActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        // Delete Downloaded Image
        if (shareUri != null)
            Util.deleteFileFromUri(FormerPMActivity.this, shareUri);
        super.onDestroy();
    }
}
