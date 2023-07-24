package com.sanskrit.pmo.Activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sanskrit.pmo.Fragments.FontSizeDialog;
import com.sanskrit.pmo.Fragments.ShareFragment;
import com.sanskrit.pmo.Fragments.TTSDialogFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.ShareUtils;
import com.sanskrit.pmo.Utils.Util;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.permissions.Nammu;
import com.sanskrit.pmo.permissions.PermissionCallback;
import com.sanskrit.pmo.utils.ShareObject;
import com.sanskrit.pmo.utils.TintHelper;
import com.sanskrit.pmo.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Locale;

public class NewsReaderActivity extends BaseActivity {
    public TextView content;
    FloatingActionButton fabShare;

    NestedScrollView scrollView;
    public TextView title;
    public TextToSpeech tts;
    boolean ttsVisible = false;
    Toolbar toolbar;
    boolean isMkb;
    private NewsFeed feed;
    private Uri shareUri;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_news_reader);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle((CharSequence) "");
        ab.setElevation(5.0f);
        this.scrollView = (NestedScrollView) findViewById(R.id.scrollview);
        this.title = (TextView) findViewById(R.id.title);
        this.content = (TextView) findViewById(R.id.content);
        this.fabShare = (FloatingActionButton) findViewById(R.id.fab);
        int progress = PreferencesUtility.getFontSize(this);
        this.content.setTextSize((float) (progress + 3));
        this.title.setTextSize((float) (progress + 9));
        feed = (NewsFeed) Parcels.unwrap(getIntent().getParcelableExtra("newsitem"));
        isMkb = getIntent().getBooleanExtra("isFrom", false);
        if (feed.mCOntent != null) {
            this.content.setText(Html.fromHtml(feed.mCOntent));
        }
        this.title.setText(feed.mTitle);
        this.searchMenuVisible = false;
        this.notifMenuVisible = false;
        invalidateOptionsMenu();
        this.ttsVisible = true;
        if (PreferencesUtility.getLanguagePrefernce(this).equals("en")) {
            invalidateOptionsMenu();
        }
        this.fabShare.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

               /* ShareObject shareObject = new ShareObject();
                shareObject.setTitle(feed.mTitle);
                // shareObject.setImageUri(shareUri);

                shareObject.setUrl(Utils.generatePmIndiaShareUrl(feed.mId));
                ShareFragment.newInstance(shareObject).show(NewsReaderActivity.this.getSupportFragmentManager(), "Share Fragment");

*/
//                if (Utils.isMarshmallow()) {
//                    checkPermissionAndThenShare();
//                } else {
                    share();
//                }

            }
        });
        if (PreferencesUtility.getTheme(this).equals("black")) {
            this.fabShare.setImageDrawable(TintHelper.tintDrawable(this.fabShare.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));
        }
        if (!getIntent().getBooleanExtra("showShare", true)) {
            this.fabShare.setVisibility(View.GONE);
        }
    }


//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void checkPermissionAndThenShare() {
//        if (Nammu.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
//            share();
//        } else if (Nammu.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
//            Snackbar.make(findViewById(R.id.container), getString(R.string.storage_permission_required), Snackbar.LENGTH_INDEFINITE).setAction((CharSequence) "OK", new OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Nammu.askForPermission(NewsReaderActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE", NewsReaderActivity.this.permissionReadstorageCallback);
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
//            Snackbar.make(findViewById(R.id.container), NewsReaderActivity.this.getString(R.string.storage_permission_required), Snackbar.LENGTH_SHORT).show();
//
//        }
//    };

    private void share() {
        // Delete Downloaded Image
        if (shareUri != null)
            Util.deleteFileFromUri(NewsReaderActivity.this, shareUri);

        final ShareObject shareObject = new ShareObject();
        shareObject.setTitle(NewsReaderActivity.this.feed.mTitle);
        Picasso.with(NewsReaderActivity.this).load(feed.mImage).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                try {

                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, NewsReaderActivity.this.feed.mTitle, null);
                    shareUri = Uri.parse(path);
                    shareObject.setImageUri(shareUri);
                    shareObject.setUrl(Utils.generatePmIndiaShareUrl(NewsReaderActivity.this.feed.mId));
                   // ShareFragment.newInstance(shareObject).show(NewsReaderActivity.this.getSupportFragmentManager(), "Share Fragment");

                    ShareUtils su= new ShareUtils(NewsReaderActivity.this,shareObject);
                    su.shareToFacebookViaApp();
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


    /* private void share() {
         ShareObject shareObject = new ShareObject();
         shareObject.setTitle(this.feed.mTitle);
         // shareObject.setImageUri(shareUri);

         shareObject.setUrl(Utils.generatePmIndiaShareUrl(feed.mId));
         ShareFragment.newInstance(shareObject).show(NewsReaderActivity.this.getSupportFragmentManager(), "Share Fragment");

     }
 */
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_tts_font, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        //menu.findItem(R.id.action_tts).setVisible(this.ttsVisible);
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;

            case R.id.action_tts:
                setUpTTS();
                TTSDialogFragment ttsFragment = new TTSDialogFragment();
                ttsFragment.setCancelable(false);
                ttsFragment.show(getSupportFragmentManager(), "Dialog Fragment");
                break;
            case R.id.action_fontsize:
                FontSizeDialog fontSizeDialog = new FontSizeDialog();
                fontSizeDialog.setCancelable(true);
                fontSizeDialog.show(getSupportFragmentManager(), "Dialog Fragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpTTS() {
        this.tts = new TextToSpeech(getApplicationContext(), new OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != -1) {

                    if (isMkb) {
                        NewsReaderActivity.this.tts.setLanguage(new Locale(PreferencesUtility.getMkbLanguageCode(NewsReaderActivity.this)));
                    } else
                        NewsReaderActivity.this.tts.setLanguage(new Locale(PreferencesUtility.getLanguagePrefernce(NewsReaderActivity.this)));


                    try {
                        if (content.getText().length() > 4000)
                            speech(NewsReaderActivity.this.content.getText().toString());
                        else
                            NewsReaderActivity.this.tts.speak(NewsReaderActivity.this.content.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //  NewsReaderActivity.this.tts.speak(NewsReaderActivity.this.content.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);


                }
            }
        });
    }


//    public static void detectLanguage(String sourceText, PrintStream out) {
//        Translate translate = createTranslateService();
//        List<Detection> detections = translate.detect(ImmutableList.of(sourceText));
//        System.out.println("Language(s) detected:");
//        for (Detection detection : detections) {
//            out.printf("\t%s\n", detection);
//        }
//    }
//    public static Translate createTranslateService() {
//        return TranslateOptions.newBuilder().build().getService();
//    }

    private void speech(String charSequence) throws Exception {

        int position;
        int sizeOfChar = charSequence.length();
        String testStri = charSequence.substring(0, sizeOfChar);


        int next = 2000;
        int pos = 0;
        while (true) {
            String temp = "";
            Log.e("in loop", "" + pos);

            try {

                temp = testStri.substring(pos, next);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, temp);
                tts.speak(temp, TextToSpeech.QUEUE_ADD, params);

                pos = pos + 2000;
                next = next + 2000;

            } catch (Exception e) {
                temp = testStri.substring(pos, testStri.length());
                tts.speak(temp, TextToSpeech.QUEUE_ADD, null);
                break;

            }

        }

    }


    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public void onPause() {
        if (this.tts != null) {
            this.tts.stop();
            this.tts.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // Delete Downloaded Image
        if (shareUri != null)
            Util.deleteFileFromUri(NewsReaderActivity.this, shareUri);
        super.onDestroy();
    }
}
