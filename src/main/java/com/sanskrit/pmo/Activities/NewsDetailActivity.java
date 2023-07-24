package com.sanskrit.pmo.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.sanskrit.pmo.Adapters.MediaAdapter;
import com.sanskrit.pmo.Adapters.NewsTweetsAdapter;
import com.sanskrit.pmo.Fragments.ShareFragment;
import com.sanskrit.pmo.Fragments.TwitterLoginFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.Utils.ParallaxScrimageView;
import com.sanskrit.pmo.Utils.ShareUtils;
import com.sanskrit.pmo.Utils.Util;
import com.sanskrit.pmo.network.mygov.MygovClient;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.callbacks.NewsCommentsListener;
import com.sanskrit.pmo.network.mygov.callbacks.NewsFeedListener;
import com.sanskrit.pmo.network.mygov.callbacks.ResponseListener;
import com.sanskrit.pmo.network.mygov.models.ImageObject;
import com.sanskrit.pmo.network.mygov.models.InsertNewComment;
import com.sanskrit.pmo.network.mygov.models.MediaObject;
import com.sanskrit.pmo.network.mygov.models.NewsComment;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.network.server.SanskritClient;
import com.sanskrit.pmo.permissions.Nammu;
import com.sanskrit.pmo.permissions.PermissionCallback;
import com.sanskrit.pmo.player.youtube.YouTubeUrlParser;
import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.Result;
import com.sanskrit.pmo.twitter.core.TwitterAuthException;
import com.sanskrit.pmo.twitter.core.TwitterCore;
import com.sanskrit.pmo.twitter.core.TwitterException;
import com.sanskrit.pmo.twitter.core.models.Tweet;
import com.sanskrit.pmo.twitter.tweetui.CompactTweetView;
import com.sanskrit.pmo.twitter.tweetui.TweetUtils;
import com.sanskrit.pmo.utils.DateUtil;
import com.sanskrit.pmo.utils.ShareObject;
import com.sanskrit.pmo.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.client.Response;

import static com.sanskrit.pmo.utils.Utils.getImageUri;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsDetailActivity extends BaseActivity {

    ImageButton back;
    ImageButton back2;
    private Button commentCount;
    private View commentFooter;
    private PostCommentsAdapter commentsAdapter;
    private ListView commentsList;
    public TextView date;
    private EditText enterComment;
    public TextView excerpt;
    private NewsFeed feed;
    ParallaxScrimageView imageView;
    MediaAdapter imagesAdapter;
    RecyclerView mediaImagesrecyclerview;
    RecyclerView newsTweetsRecyclerview;
    private ImageButton postComment;
    private Button readMore;
    View rootView;
    private Button share;
    private boolean shouldFetch;
    private View spacer;
    public TextView title;
    private List<Long> tweetIds;
    NewsTweetsAdapter tweetsAdapter;
    LinearLayout tweetsLayout;
    LinearLayout linearLogin;
    TextInputLayout inputLayout;
    private LinearLayout linearComment;
    Bitmap shareBitmap;
    private Uri shareUri;
    View parent;

    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_news_detail);
        getWindow().setSoftInputMode(32);
        //this.rootView = findViewById(R.id.rootview);
        View postDescription = getLayoutInflater().inflate(R.layout.layout_news_description, this.commentsList, false);
        this.title = (TextView) postDescription.findViewById(R.id.title);
        this.excerpt = (TextView) postDescription.findViewById(R.id.description);
        this.share = (Button) postDescription.findViewById(R.id.action_three);
        this.date = (TextView) postDescription.findViewById(R.id.author_time);
        this.imageView = (ParallaxScrimageView) findViewById(R.id.image);
        this.tweetsLayout = (LinearLayout) postDescription.findViewById(R.id.tweets_layout);
        this.spacer = postDescription.findViewById(R.id.shot_spacer);
        this.back = (ImageButton) findViewById(R.id.back);
        this.back2 = (ImageButton) postDescription.findViewById(R.id.back2);
        this.commentsList = (ListView) findViewById(R.id.comments);
        this.commentsList.addHeaderView(postDescription);
        this.commentsList.setOnScrollListener(this.scrollListener);
        this.mediaImagesrecyclerview = (RecyclerView) postDescription.findViewById(R.id.recyclerviewmediaimages);
        this.mediaImagesrecyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.newsTweetsRecyclerview = (RecyclerView) postDescription.findViewById(R.id.recyclerviewnewstweets);
        this.newsTweetsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        this.readMore = (Button) postDescription.findViewById(R.id.action_one);
        this.share = (Button) postDescription.findViewById(R.id.action_three);
        this.commentCount = (Button) postDescription.findViewById(R.id.action_two);
        this.parent = postDescription.findViewById(R.id.container);
        fetchNewsDetails();
        this.commentFooter = getLayoutInflater().inflate(R.layout.layout_enter_comment, this.commentsList, false);
        inputLayout = (TextInputLayout) this.commentFooter.findViewById(R.id.input_layout);
        this.enterComment = (EditText) this.commentFooter.findViewById(R.id.comment);
        this.postComment = (ImageButton) this.commentFooter.findViewById(R.id.post_comment);
        this.linearLogin = (LinearLayout) this.commentFooter.findViewById(R.id.linear_not_login);
        linearComment = (LinearLayout) commentFooter.findViewById(R.id.linear_comment);

        ///   this.enterComment.setOnFocusChangeListener(this.enterCommentFocus);
        this.commentsList.addFooterView(this.commentFooter);
        //  this.postComment.setEnabled(false);


        linearComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PreferencesUtility.getMygovLoggedIn(NewsDetailActivity.this)) {
                    NewsDetailActivity.this.startActivity(new Intent(NewsDetailActivity.this, NativeLoginActivity.class).setAction("MyGov"));

                }
            }
        });
        linearLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PreferencesUtility.getMygovLoggedIn(NewsDetailActivity.this)) {
                    NewsDetailActivity.this.startActivity(new Intent(NewsDetailActivity.this, NativeLoginActivity.class).setAction("MyGov"));

                }
            }
        });
        if (PreferencesUtility.getMygovLoggedIn(this)) {
            enterComment.setFocusable(true);
            this.enterComment.setEnabled(true);
            enterComment.setClickable(false);

            inputLayout.setHint(getString(R.string.add_comment));
            this.postComment.setClickable(true);
            this.postComment.setEnabled(true);
            linearLogin.setVisibility(View.GONE);
            linearComment.setFocusable(false);
            linearComment.setClickable(false);

        } else {
            linearLogin.setVisibility(View.VISIBLE);

            enterComment.setFocusable(false);
            //  this.enterComment.setEnabled(false);

            linearComment.setFocusable(true);
            linearComment.setClickable(true);
            // inputLayout.setHint(getString(R.string.add_comment));
            //this.postComment.setClickable(false);
            //this.postComment.setEnabled(false);
        }

        enterComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PreferencesUtility.getMygovLoggedIn(NewsDetailActivity.this)) {
                    NewsDetailActivity.this.startActivity(new Intent(NewsDetailActivity.this, NativeLoginActivity.class).setAction("MyGov"));

                }
            }
        });

        this.postComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!PreferencesUtility.getMygovLoggedIn(NewsDetailActivity.this)) {
                    NewsDetailActivity.this.startActivity(new Intent(NewsDetailActivity.this, NativeLoginActivity.class).setAction("MyGov"));

                } else {

                    if (NewsDetailActivity.this.enterComment.getText().toString().length() > 20) {
                        NewsDetailActivity.this.postComment();
                    } else {
                        NewsDetailActivity.this.enterComment.setError(NewsDetailActivity.this.getString(R.string.error_comment_validation));
                        Toast.makeText(NewsDetailActivity.this, NewsDetailActivity.this.getString(R.string.error_comment_validation), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if (Utils.isLollipop()) {
            this.back.setVisibility(View.VISIBLE);
            back.setClickable(true);
            back.setEnabled(true);
            this.back2.setVisibility(View.GONE);
        } else {
            this.back.setVisibility(View.GONE);
            this.back2.setVisibility(View.VISIBLE);
            back2.setClickable(true);
            back2.setEnabled(true);

        }
        this.back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsDetailActivity.this.onBackPressed();
            }
        });
        this.back2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsDetailActivity.this.onBackPressed();
            }
        });
        this.share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (Utils.isMarshmallow()) {
//                    checkPermissionAndThenShare();
//                } else {
                    share();
//                }

               /* shareObject.setImageUri(shareUri);
                shareObject.setUrl(Utils.generatePmIndiaShareUrl(NewsDetailActivity.this.feed.mId));
                ShareFragment.newInstance(shareObject).show(NewsDetailActivity.this.getSupportFragmentManager(), "Share Fragment");
*/
            }
        });
        this.spacer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (feed.mImage != null && feed.mImage.length() > 0) {
                    Intent intent = new Intent(NewsDetailActivity.this, ViewImageActivity.class);
                    ImageObject imageObject = new ImageObject();
                    imageObject.setUrl(NewsDetailActivity.this.feed.mImage);
                    imageObject.setTitle(NewsDetailActivity.this.feed.mTitle);
                    imageObject.setId(String.valueOf(NewsDetailActivity.this.feed.mId));
                    intent.putExtra("image", Parcels.wrap(imageObject));
                    NewsDetailActivity.this.startActivity(intent);
                }
            }
        });
        this.readMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewsDetailActivity.this, NewsReaderActivity.class);
                intent.putExtra("newsitem", Parcels.wrap(NewsDetailActivity.this.feed));
                NewsDetailActivity.this.startActivity(intent);
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
//                    Nammu.askForPermission(NewsDetailActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE", NewsDetailActivity.this.permissionReadstorageCallback);
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
//            Snackbar.make(findViewById(R.id.container), NewsDetailActivity.this.getString(R.string.storage_permission_required), Snackbar.LENGTH_SHORT).show();
//
//        }
//    };

    private void share() {
        // Delete Downloaded Image
        if (shareUri != null)
            Util.deleteFileFromUri(NewsDetailActivity.this, shareUri);

        final ShareObject shareObject = new ShareObject();
        shareObject.setTitle(NewsDetailActivity.this.feed.mTitle);
        Picasso.with(NewsDetailActivity.this).load(feed.mImage).into(new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, NewsDetailActivity.this.feed.mTitle, null);
                    shareUri = Uri.parse(path);
                    shareObject.setImageUri(shareUri);
                    shareObject.setUrl(Utils.generatePmIndiaShareUrl(NewsDetailActivity.this.feed.mId));
                    ShareUtils su= new ShareUtils(NewsDetailActivity.this,shareObject);
                    su.shareToFacebookViaApp();
//                    ShareFragment.newInstance(shareObject).show(NewsDetailActivity.this.getSupportFragmentManager(), "Share Fragment");
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

    private void fetchNewsDetails() {
        this.shouldFetch = getIntent().getBooleanExtra("shouldfetch", false);
        this.feed = (NewsFeed) Parcels.unwrap(getIntent().getParcelableExtra("newsitem"));
        this.title.setText(this.feed.mTitle);
        if (this.feed.mDate != null) {

            this.date.setText(DateUtil.dateToPastTenseString(DateUtil.stringToDate(this.feed.mDate)));
        }
        this.commentsList.setAdapter(getNoCommentsAdapter());
        if (!this.shouldFetch) {
            this.shouldFetch = false;
            setNewsDetails();
        } else if (this.feed.mId != null) {
            MygovClient.getInstance(this).getNewsDetail(PreferencesUtility.getLanguagePrefernce(this), this.feed.mId, new NewsFeedListener() {
                @Override
                public void failure() {
                    if (feed.mCOntent != null) {
                        excerpt.setMaxLines(3);
                        excerpt.setEllipsize(TextUtils.TruncateAt.END);
                        excerpt.setText(Html.fromHtml(feed.mCOntent));
                    }
                    if (feed.mImage != null && feed.mImage.length() > 0) {
                        Picasso.with(NewsDetailActivity.this).load(feed.mImage).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {

                                NewsDetailActivity.this.imageView.setImageBitmap(bitmap);
                                shareUri = getImageUri(NewsDetailActivity.this, bitmap);

                                NewsDetailActivity.this.back.setImageResource(R.drawable.ic_arrow_back_white_24dp);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                    }
                }

                @Override
                public void success(List<NewsFeed> response) {
                    if (response != null && response.size() != 0) {
                        NewsDetailActivity.this.feed = (NewsFeed) response.get(0);
                        NewsDetailActivity.this.shouldFetch = false;
                        NewsDetailActivity.this.setNewsDetails();
                    }
                }
            });

        }


    }

    private void setNewsDetails() {
        this.title.setText(this.feed.mTitle);
        if (this.feed.mExcerpt != null) {
            this.excerpt.setText(this.feed.mExcerpt);
        }
        this.date.setText(DateUtil.dateToPastTenseString(DateUtil.stringToDate(this.feed.mDate)));
        if (this.feed.mImage != null && this.feed.mImage.length() > 0) {

            Picasso.with(this).load(this.feed.mImage).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {

                    NewsDetailActivity.this.imageView.setImageBitmap(bitmap);
                    shareUri = getImageUri(NewsDetailActivity.this, bitmap);
                    NewsDetailActivity.this.back.setImageResource(R.drawable.ic_arrow_back_white_24dp);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
        try {
            Document document = Jsoup.parse(this.feed.mNewsMedia);
            Elements pngs = document.select("img[src$=.png]");
            Elements jpgs = document.select("img[src$=.jpg]");
            Elements iframes = document.select("iframe");
            if (!(pngs.size() == 0 && jpgs.size() == 0 && iframes.size() == 0)) {
                int i;
                List<MediaObject> imageList = new ArrayList();
                for (i = 0; i < iframes.size(); i++) {
                    imageList.add(new MediaObject("video", null, YouTubeUrlParser.getVideoId(((Element) iframes.get(i)).attr("src")), null, null));
                }
                for (i = 0; i < pngs.size(); i++) {
                    imageList.add(new MediaObject("image", ((Element) pngs.get(i)).attr("src"), null, this.feed.mId, this.feed.mTitle));
                }
                for (i = 0; i < jpgs.size(); i++) {
                    imageList.add(new MediaObject("image", ((Element) jpgs.get(i)).attr("src"), null, this.feed.mId, this.feed.mTitle));
                }
                this.imagesAdapter = new MediaAdapter(this, imageList);
                this.mediaImagesrecyclerview.setAdapter(this.imagesAdapter);
                this.mediaImagesrecyclerview.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!(this.feed.mTweetIds == null || this.feed.mTweetIds.size() == 0)) {
            List<Long> ids = new ArrayList();
            for (String s : this.feed.mTweetIds) {
                try {
                    ids.add(Long.valueOf(Long.parseLong(s)));
                }catch (NumberFormatException n)
                {
                    n.printStackTrace();
                }
            }
            this.tweetIds = ids;
//           By:- Gaurab Comments
            if (ids.size() != 0) {
                String commaSepIds = TextUtils.join(",", ids);
//                if (TwitterCore.getInstance().getSessionManager().getActiveSession() != null) {
//                    loadAuthenticatedTweets(ids);
//                } else {
//                    loadTweets(commaSepIds);
//                }
                loadTweets(commaSepIds);
            }
        }
        MygovClient.getInstance(this).getNewsComments(PreferencesUtility.getLanguagePrefernce(this), this.feed.mId, new NewsCommentsListener() {
            @Override
            public void onCommnetsFetched(List<NewsComment> comments) {
                if (comments == null || comments.size() == 0) {
                    NewsDetailActivity.this.commentsList.setAdapter(NewsDetailActivity.this.getNoCommentsAdapter());
                    return;
                }
                NewsDetailActivity.this.commentCount.setText(comments.size() + NewsDetailActivity.this.getString(R.string.comments));
                NewsDetailActivity.this.commentsAdapter = new PostCommentsAdapter(NewsDetailActivity.this, R.layout.item_news_comment, comments);
                NewsDetailActivity.this.commentsList.setAdapter(NewsDetailActivity.this.commentsAdapter);
                NewsDetailActivity.this.commentsList.setDivider(NewsDetailActivity.this.getResources().getDrawable(R.drawable.list_divider));
                NewsDetailActivity.this.commentsList.setDividerHeight(NewsDetailActivity.this.getResources().getDimensionPixelSize(R.dimen.divider_height));

            }

            @Override
            public void onError() {
                NewsDetailActivity.this.commentsList.setAdapter(NewsDetailActivity.this.getNoCommentsAdapter());

            }
        });

    }


    public void loadTweets(String ids) {
        SanskritClient.getInstance(this).lookupTweets(ids, new GenericCallback() {
            public void success(Object response) {

                for (Object tweet : (List) response) {
                    CompactTweetView tweetView;
                    if (NewsDetailActivity.this.isLightTheme) {
                        tweetView = new CompactTweetView(NewsDetailActivity.this, (Tweet) tweet, (int) R.style.CustomTweetStyleLight);
                    } else if (NewsDetailActivity.this.isDarkTheme) {
                        tweetView = new CompactTweetView(NewsDetailActivity.this, (Tweet) tweet, (int) R.style.CustomTweetStyleDark);
                    } else {
                        tweetView = new CompactTweetView(NewsDetailActivity.this, (Tweet) tweet, (int) R.style.tw__TweetDarkWithActionsStyle);
                    }


                    tweetView.setOnActionCallback(NewsDetailActivity.this.actionCallback);
                    NewsDetailActivity.this.tweetsLayout.addView(tweetView);
                }
            }

            public void failure() {

            }
        });
    }


    public void loadAuthenticatedTweets(List<Long> ids) {
        TweetUtils.loadTweets((List) ids, new Callback<List<Tweet>>() {
            @Override
            public void failure(TwitterException twitterException) {
            }

            @Override
            public void success(Result<List<Tweet>> result) {
                ArrayList<Tweet> tweetArrayList = (ArrayList<Tweet>) result.data;
                for (Tweet tweet : tweetArrayList) {
                    CompactTweetView tweetView;
                    if (NewsDetailActivity.this.isLightTheme) {
                        tweetView = new CompactTweetView(NewsDetailActivity.this, tweet, (int) R.style.CustomTweetStyleLight);
                    } else if (NewsDetailActivity.this.isDarkTheme) {
                        tweetView = new CompactTweetView(NewsDetailActivity.this, tweet, (int) R.style.CustomTweetStyleDark);
                    } else {
                        tweetView = new CompactTweetView(NewsDetailActivity.this, tweet, (int) R.style.tw__TweetDarkWithActionsStyle);
                    }
                    tweetView.setOnActionCallback(NewsDetailActivity.this.actionCallback);
                    NewsDetailActivity.this.tweetsLayout.addView(tweetView);
                }
            }


        });
    }

    public void loadAuthenticatedTweets() {
        if (this.tweetIds != null && this.tweetIds.size() != 0) {
            loadAuthenticatedTweets(this.tweetIds);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.action_search:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ListAdapter getNoCommentsAdapter() {
        return new ArrayAdapter(this, R.layout.post_no_comments, new String[]{getString(R.string.no_comments)});
    }

    private void postComment() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.sending_comment));
        progressDialog.show();
        progressDialog.setCancelable(true);
        InsertNewComment comment = new InsertNewComment();
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        comment.setIp(Utils.getIpAddress(this));
        comment.setComment(this.enterComment.getText().toString());
        comment.setName(PreferencesUtility.getMygovOauthName(this));
        comment.setEmail(PreferencesUtility.getMygovOauthEmail(this));
        comment.setDate(format.format(now));
        comment.setId(this.feed.mId);
        comment.setCommentId(this.feed.mId);
        MygovClient.getInstance(this).postNewNewsComment(comment, new ResponseListener() {
            public void success(Response response) {
                progressDialog.dismiss();
                enterComment.setText("");
                Toast.makeText(NewsDetailActivity.this, R.string.comment_added, Toast.LENGTH_LONG).show();
            }

            public void failure() {
                progressDialog.dismiss();
                Toast.makeText(NewsDetailActivity.this, R.string.error_adding_comment, Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("TwitterLoginFragment");
            if (fragment != null && (fragment instanceof TwitterLoginFragment)) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private OnScrollListener scrollListener = new OnScrollListener() {
        public void onScroll(AbsListView view, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
            if (NewsDetailActivity.this.commentsList.getMaxScrollAmount() > 0 && firstVisibleItemPosition == 0 && NewsDetailActivity.this.commentsList.getChildAt(0) != null) {
                NewsDetailActivity.this.imageView.setOffset((int) NewsDetailActivity.this.commentsList.getChildAt(0).getTop());
            }
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            imageView.setImmediatePin(scrollState == 2);
        }
    };

    final Callback<Tweet> actionCallback = new Callback<Tweet>() {
        public void success(Result<Tweet> result) {
        }

        public void failure(TwitterException exception) {
            if (exception instanceof TwitterAuthException) {
                new TwitterLoginFragment().show(NewsDetailActivity.this.getSupportFragmentManager(), "TwitterLoginFragment");
            }
        }
    };

    private OnFocusChangeListener enterCommentFocus = new OnFocusChangeListener() {
        public void onFocusChange(View view, boolean hasFocus) {
            NewsDetailActivity.this.postComment.setActivated(hasFocus);
        }
    };


    public class PostCommentsAdapter extends ArrayAdapter<NewsComment> {
        private final LayoutInflater inflater;

        public PostCommentsAdapter(Context context, int resource, List<NewsComment> comments) {
            super(context, resource, comments);
            this.inflater = LayoutInflater.from(context);
        }

        public View getView(int position, View view, ViewGroup container) {
            if (view == null) {
                view = newNewCommentView(position, container);
            }
            bindComment((NewsComment) getItem(position), position, view);
            return view;
        }

        private View newNewCommentView(int position, ViewGroup parent) {
            View view = this.inflater.inflate(R.layout.item_news_comment, parent, false);
            view.setTag(R.id.comment_author, view.findViewById(R.id.comment_author));
            view.setTag(R.id.comment_time_ago, view.findViewById(R.id.comment_time_ago));
            view.setTag(R.id.comment_text, view.findViewById(R.id.comment_text));
            return view;
        }

        private void bindComment(NewsComment comment, int position, View view) {
            TextView timeAgo = (TextView) view.getTag(R.id.comment_time_ago);
            TextView commentBody = (TextView) view.getTag(R.id.comment_text);
            ((TextView) view.getTag(R.id.comment_author)).setText(comment.mAuthor);
            timeAgo.setText(DateUtil.dateToPastTenseString(DateUtil.stringToDate(comment.mDate)));
            commentBody.setText(Html.fromHtml(comment.mText));
        }

        public boolean hasStableIds() {
            return true;
        }

        public long getItemId(int position) {
            return Long.parseLong(((NewsComment) getItem(position)).mId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        // Delete Downloaded Image
        if (shareUri != null)
            Util.deleteFileFromUri(NewsDetailActivity.this, shareUri);
        super.onDestroy();
    }
}
