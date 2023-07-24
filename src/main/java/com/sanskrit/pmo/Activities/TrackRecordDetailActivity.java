package com.sanskrit.pmo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.sanskrit.pmo.Fragments.ShareFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.ShareUtils;
import com.sanskrit.pmo.Utils.Util;
import com.sanskrit.pmo.network.mygov.models.ImageObject;
import com.sanskrit.pmo.network.mygov.models.MediaObject;
import com.sanskrit.pmo.permissions.Nammu;
import com.sanskrit.pmo.permissions.PermissionCallback;
import com.sanskrit.pmo.player.youtube.Orientation;
import com.sanskrit.pmo.player.youtube.Quality;
import com.sanskrit.pmo.player.youtube.YouTubePlayerActivity;
import com.sanskrit.pmo.player.youtube.YouTubeThumbnail;
import com.sanskrit.pmo.player.youtube.YouTubeUrlParser;
import com.sanskrit.pmo.utils.ShareObject;
import com.sanskrit.pmo.utils.TintHelper;
import com.sanskrit.pmo.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TrackRecordDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    ProgressBar progressBar;

    TextView title, date;

    LinearLayout contentLayout;
    ViewGroup root;

    View itemView;
    ImageView mediaImage, videoOverlay;
    View videoOverlayView;

    FloatingActionButton fabShare;


    boolean isFrom;

    String titleStr, contentStr, imageStr, dateStr, idStr;
    private Uri shareUri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_trackrecord_detail);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.root = (ViewGroup) findViewById(R.id.root);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.title = (TextView) findViewById(R.id.news_title);
        this.date = (TextView) findViewById(R.id.news_date);
        this.contentLayout = (LinearLayout) findViewById(R.id.content_layout);
        this.fabShare = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle((int) R.string.track_record);

        //searchMenuVisible = false;

        if (PreferencesUtility.getTheme(this).equals("black")) {
            this.fabShare.setImageDrawable(TintHelper.tintDrawable(this.fabShare.getDrawable(), (int) ViewCompat.MEASURED_STATE_MASK));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleStr = getIntent().getStringExtra("title");
        contentStr = getIntent().getStringExtra("content");
        imageStr = getIntent().getStringExtra("image");
        dateStr = getIntent().getStringExtra("date");
        idStr = getIntent().getStringExtra("id");
        isFrom = getIntent().getBooleanExtra("isFromSearch", false);
        setNewsDetails();

        title.setText(titleStr);
        date.setText(dateStr);


        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (Utils.isMarshmallow()) {
//                    checkPermissionAndThenShare();
//                } else {
                    share();
//                }

               /* ShareObject shareObject = new ShareObject();
                shareObject.setTitle(titleStr);

                Picasso.with(TrackRecordDetailActivity.this).load(imageStr).into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {


                        shareUri = getImageUri(TrackRecordDetailActivity.this,bitmap);

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });*/

             /* shareObject.setImageUri(shareUri);
                shareObject.setImageUri(shareUri);

                shareObject.setUrl("http://pmindia.gov.in/?p=" + idStr);
                ShareFragment.newInstance(shareObject).show(TrackRecordDetailActivity.this.getSupportFragmentManager(), "Share Fragment");
*/
            }
        });
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void checkPermissionAndThenShare() {
//        if (Nammu.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE")) {
//            share();
//        } else if (Nammu.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
//            Snackbar.make(findViewById(R.id.root), getString(R.string.storage_permission_required), Snackbar.LENGTH_INDEFINITE).setAction((CharSequence) "OK", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Nammu.askForPermission(TrackRecordDetailActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE", TrackRecordDetailActivity.this.permissionReadstorageCallback);
//
//                }
//            }).show();
//        } else {
//            Nammu.askForPermission((Activity) this, "android.permission.WRITE_EXTERNAL_STORAGE", this.permissionReadstorageCallback);
//        }
//    }

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
//            Snackbar.make(findViewById(R.id.root), TrackRecordDetailActivity.this.getString(R.string.storage_permission_required), Snackbar.LENGTH_SHORT).show();
//
//        }
//    };

    private void share() {
        // Delete Downloaded Image
        if (shareUri != null) {
            Util.deleteFileFromUri(TrackRecordDetailActivity.this, shareUri);
        }
        final ShareObject shareObject = new ShareObject();
        shareObject.setTitle(titleStr);
        Picasso.with(TrackRecordDetailActivity.this).load(imageStr).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, titleStr, null);
                    shareUri = Uri.parse(path);
                    shareObject.setImageUri(shareUri);
                    shareObject.setUrl("http://pmindia.gov.in/?p=" + idStr);
                    ShareUtils su= new ShareUtils(TrackRecordDetailActivity.this,shareObject);
                    su.shareToFacebookViaApp();
//                    ShareFragment.newInstance(shareObject).show(TrackRecordDetailActivity.this.getSupportFragmentManager(), "Share Fragment");
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


    private void setNewsDetails() {

        String contentText = getIntent().getStringExtra("content");
        final String titleText = getIntent().getStringExtra("title");

        title.setText(titleText);
        date.setText(getIntent().getStringExtra("date"));

        String htmlBody = getIntent().getStringExtra("content").replaceAll("(<(/)img>)|(<img.+?>)", "");


        int nextImageIndex = contentText.indexOf("<img");
        int previousImageIndex = 0;


        try {
            Document document = Jsoup.parse(contentText);
            Elements pngs = document.select("img[src$=.png]");
            Elements jpgs = document.select("img[src$=.jpg]");
            Elements iframes = document.select("iframe");

            int numberImages = jpgs.size() + pngs.size();

            if (pngs.size() != 0 || jpgs.size() != 0 || iframes.size() != 0) {
                List<MediaObject> imageList = new ArrayList<>();

                for (int i = 0; i < iframes.size(); i++) {

                    String videoUrl = iframes.get(i).attr("src");
                    final MediaObject object = new MediaObject("video", null, YouTubeUrlParser.getVideoId(videoUrl), null, null);

                    itemView = LayoutInflater.from(this).inflate(R.layout.item_trackrecord_media, root, false);

                    mediaImage = (ImageView) itemView.findViewById(R.id.media_image);
                    videoOverlay = (ImageView) itemView.findViewById(R.id.video_overlay);
                    videoOverlayView = itemView.findViewById(R.id.video_overlay_view);

                    try {
                        videoOverlay.setVisibility(View.VISIBLE);
                        videoOverlayView.setVisibility(View.VISIBLE);
                        String thumbnailUrl = YouTubeThumbnail.getUrlFromVideoId(object.videoId, Quality.HIGH);
                        Picasso.with(TrackRecordDetailActivity.this).load(thumbnailUrl).into(mediaImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    contentLayout.addView(itemView);

                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                startYoutubeActivity(object.videoId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                if (numberImages == 0 || nextImageIndex == -1) {
                    TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.item_trackrecord_text, root, false);
                    textView.setText(HtmlCompat.fromHtml(htmlBody,Html.FROM_HTML_MODE_LEGACY));
                   // textView.setText(htmlBody,Html.FROM_HTML_MODE_LEGACY));
                    textView.setLinksClickable(true);
                  textView.setMovementMethod(LinkMovementMethod.getInstance());
                   // Linkify.addLinks(textView, Linkify.ALL);
                    contentLayout.addView(textView);
                    return;
                }


                Random random = new Random();
                for (int i = 0; i < pngs.size(); i++) {
                    imageList.add(new MediaObject("image", pngs.get(i).attr("src"), null, String.valueOf(random.nextInt(999)), titleText));
                }

                for (int i = 0; i < jpgs.size(); i++) {
                    imageList.add(new MediaObject("image", jpgs.get(i).attr("src"), null, String.valueOf(random.nextInt(999)), titleText));
                }

                boolean fistIteration = true;
                for (final MediaObject object : imageList) {

                    if (fistIteration) {
                        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.item_trackrecord_text, root, false);
                        textView.setText(HtmlCompat.fromHtml(contentText.substring(previousImageIndex, nextImageIndex).replaceAll("(<(/)img>)|(<img.+?>)", ""),Html.FROM_HTML_MODE_LEGACY));
                        textView.setLinksClickable(true);
                        textView.setMovementMethod(LinkMovementMethod.getInstance());
                        contentLayout.addView(textView);
                    }

                    View itemView = LayoutInflater.from(this).inflate(R.layout.item_trackrecord_media, root, false);

                    mediaImage = (ImageView) itemView.findViewById(R.id.media_image);
                    Picasso.with(TrackRecordDetailActivity.this).load(object.imageurl).into(mediaImage);
                    contentLayout.addView(itemView);
                    mediaImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TrackRecordDetailActivity.this, ViewImageActivity.class);
                            ImageObject imageObject = new ImageObject();
                            imageObject.setUrl(object.imageurl);
                            imageObject.setId(idStr);
                            imageObject.setTitle(object.title);
                            intent.putExtra("image", Parcels.wrap(imageObject));
                            startActivity(intent);
                        }
                    });

                    previousImageIndex = nextImageIndex;
                    nextImageIndex = contentText.indexOf("<img", previousImageIndex + 1);

                    TextView textView2 = (TextView) LayoutInflater.from(this).inflate(R.layout.item_trackrecord_text, root, false);

                    if (nextImageIndex != -1) {
                        textView2.setText(HtmlCompat.fromHtml(contentText.substring(previousImageIndex, nextImageIndex).replaceAll("(<(/)img>)|(<img.+?>)", ""),Html.FROM_HTML_MODE_LEGACY));

                    } else {
                        textView2.setText(HtmlCompat.fromHtml(contentText.substring(previousImageIndex, contentText.length()).replaceAll("(<(/)img>)|(<img.+?>)", ""),Html.FROM_HTML_MODE_LEGACY));
                    }


                    textView2.setLinksClickable(true);
                    textView2.setMovementMethod(LinkMovementMethod.getInstance());
                    contentLayout.addView(textView2);

                    fistIteration = false;


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.item_trackrecord_text, root, false);
            textView.setText(HtmlCompat.fromHtml(htmlBody,Html.FROM_HTML_MODE_LEGACY));
            // textView.setText(htmlBody,Html.FROM_HTML_MODE_LEGACY));
            textView.setLinksClickable(true);
             textView.setMovementMethod(LinkMovementMethod.getInstance());
            //Linkify.addLinks(textView, Linkify.ALL);
            contentLayout.addView(textView);

        }
    }


    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }


    private void startYoutubeActivity(String videoId) {
        Intent intent = new Intent(this, YouTubePlayerActivity.class);
        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, videoId);
        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, PlayerStyle.DEFAULT);
        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO_START_WITH_LANDSCAPE);
        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
        //  intent.setFlags(VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        // Delete Downloaded Image
        if (shareUri != null)
            Util.deleteFileFromUri(TrackRecordDetailActivity.this, shareUri);
        super.onDestroy();
    }

}
