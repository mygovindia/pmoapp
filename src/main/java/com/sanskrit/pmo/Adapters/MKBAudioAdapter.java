
package com.sanskrit.pmo.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.internal.ServerProtocol;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.sanskrit.pmo.Activities.BaseActivity;
import com.sanskrit.pmo.Activities.MKBActivity;
import com.sanskrit.pmo.Activities.NewsReaderActivity;
import com.sanskrit.pmo.Fragments.MKBAudioFragment;
import com.sanskrit.pmo.Fragments.MkbLanguageFragment;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.network.server.models.MkbAudio;
import com.sanskrit.pmo.network.server.models.MkbVideo;
import com.sanskrit.pmo.player.MusicService;
import com.sanskrit.pmo.player.MusicStateAdapterListener;
import com.sanskrit.pmo.player.youtube.Orientation;
import com.sanskrit.pmo.player.youtube.Quality;
import com.sanskrit.pmo.player.youtube.YouTubePlayerActivity;
import com.sanskrit.pmo.player.youtube.YouTubeThumbnail;
import com.sanskrit.pmo.utils.DateUtil;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.fabric.sdk.android.services.network.HttpRequest;

public class MKBAudioAdapter extends RecyclerView.Adapter<MKBAudioAdapter.ItemHolder> {
    private List<MkbAudio> arraylist;
    private int attempts = 0;
    private MKBAudioFragment fragment;
    private MusicStateAdapterListener listener;
    private Activity mContext;
    private MkbVideo mkbVideo;
    private MkbAudio trackModel;

    class C10411 implements OnClickListener {
        C10411() {
        }

        public void onClick(View v) {
            Intent intent = new Intent(MKBAudioAdapter.this.mContext, MKBActivity.class);
            intent.setAction("Video");
            MKBAudioAdapter.this.mContext.startActivity(intent);
        }
    }

    class C10422 implements OnClickListener {
        C10422() {
        }

        public void onClick(View v) {
            MKBAudioAdapter.this.showLanguageFragment();
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements OnClickListener {
        protected TextView allVideos = ((TextView) this.itemView.findViewById(R.id.btn_view_videos));
        protected TextView changeLang = ((TextView) this.itemView.findViewById(R.id.btn_lang_change));
        protected TextView currentLang = ((TextView) this.itemView.findViewById(R.id.current_lang));
        protected TextView read = ((TextView) this.itemView.findViewById(R.id.read_more));
        protected ImageView thubmnail = ((ImageView) this.itemView.findViewById(R.id.thumbnail));
        protected TextView trackDate = ((TextView) this.itemView.findViewById(R.id.track_date));
        protected TextView trackDuration = ((TextView) this.itemView.findViewById(R.id.track_duration));
        protected TextView trackName = ((TextView) this.itemView.findViewById(R.id.track_name));
        protected TextView type = ((TextView) this.itemView.findViewById(R.id.type));

        public ItemHolder(View view) {
            super(view);
            view.setOnClickListener(this);
//            if (this.read != null) {
//                this.read.setOnClickListener(new OnClickListener() {
//                    public void onClick(View v) {
//                        try {
//                            MkbAudio audio = (MkbAudio) MKBAudioAdapter.this.arraylist.get(ItemHolder.this.getAdapterPosition() - 1);
//                            NewsFeed feed = new NewsFeed();
//                            feed.mCOntent = audio.mCOntent;
//                            feed.mDate = audio.mDate;
//                            feed.mTitle = audio.mTitle;
//                            Intent intent = new Intent(MKBAudioAdapter.this.mContext, NewsReaderActivity.class);
//                            intent.putExtra("newsitem", Parcels.wrap(feed));
//                            intent.putExtra("showShare", false);
//                            intent.putExtra("isFrom", true);
//                            MKBAudioAdapter.this.mContext.startActivity(intent);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
        }

        public void onClick(View v) {
            try {
                MkbAudio audio = (MkbAudio) MKBAudioAdapter.this.arraylist.get(ItemHolder.this.getAdapterPosition() - 1);
                NewsFeed feed = new NewsFeed();
                feed.mCOntent = audio.mCOntent;
                feed.mDate = audio.mDate;
                feed.mTitle = audio.mTitle;
                Intent intent = new Intent(MKBAudioAdapter.this.mContext, NewsReaderActivity.class);
                intent.putExtra("newsitem", Parcels.wrap(feed));
                intent.putExtra("showShare", false);
                intent.putExtra("isFrom", true);
                MKBAudioAdapter.this.mContext.startActivity(intent);
//                if (getAdapterPosition() != 0) {
//                    PreferencesUtility.setCurrentMKBTrackName(MKBAudioAdapter.this.mContext, ((MkbAudio) MKBAudioAdapter.this.arraylist.get(getAdapterPosition() - 1)).mTitle);
//                    PreferencesUtility.setCurrentMKBTrackDate(MKBAudioAdapter.this.mContext, ((MkbAudio) MKBAudioAdapter.this.arraylist.get(getAdapterPosition() - 1)).mDate);
//                    PreferencesUtility.setCurrentMKBTrackImage(MKBAudioAdapter.this.mContext, ((MkbAudio) MKBAudioAdapter.this.arraylist.get(getAdapterPosition() - 1)).mImage);
//                    MKBAudioAdapter.this.playStream((MkbAudio) MKBAudioAdapter.this.arraylist.get(getAdapterPosition() - 1));
//                } else if (MKBAudioAdapter.this.mkbVideo != null) {
//                    MKBAudioAdapter.this.startYoutubeActivity(MKBAudioAdapter.this.mkbVideo.getVideo_id());
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class RetrieveStreamUrl extends AsyncTask<String, Void, String> {

        class C10441 implements Runnable {
            C10441() {
            }

            public void run() {
                MKBAudioAdapter.this.attempts = MKBAudioAdapter.this.attempts + 1;
                new RetrieveStreamUrl().execute(new String[]{MKBAudioAdapter.this.trackModel.mSoundCloudLink});
            }
        }

        RetrieveStreamUrl() {
        }

        protected String doInBackground(String... urls) {
            try {
                String aa=urls[0];
                HttpURLConnection ucon = (HttpURLConnection) new URL(aa).openConnection();
                ucon.setInstanceFollowRedirects(false);
                return new URL(ucon.getHeaderField(HttpRequest.HEADER_LOCATION)).toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                if (MKBAudioAdapter.this.attempts >= 2 || MKBAudioAdapter.this.trackModel == null) {
                    return "";
                }
                MKBAudioAdapter.this.mContext.runOnUiThread(new C10441());
                return "";
            } catch (IOException e2) {
                e2.printStackTrace();
                return "";
            }
        }

        protected void onPostExecute(String url) {
            if (url == null || url.equals("")) {
                Toast.makeText(MKBAudioAdapter.this.mContext, "Unable to stream", Toast.LENGTH_LONG).show();

                if (MKBAudioAdapter.this.getService() != null) {
                    //MKBAudioAdapter.this.getService().stopPlayer();
                    fragment.clearPlayingBar();
                    //MKBAudioAdapter.this.getService().stopAndRelease();

                }
                return;
            }
            PreferencesUtility.setCurrentMKBTrackURL(MKBAudioAdapter.this.mContext, url);
            if (MKBAudioAdapter.this.getService() != null) {
                MKBAudioAdapter.this.getService().playSong();
            }
        }
    }

    public MKBAudioAdapter(Activity context, List<MkbAudio> arraylist, MKBAudioFragment fragment) {
        this.mContext = context;
        this.arraylist = arraylist;
        this.fragment = fragment;
    }

    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mkb_header, viewGroup, false));
        }
        return new ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mkb_audio, viewGroup, false));
    }

    public void onBindViewHolder(ItemHolder itemHolder, int i) {
        if (i == 0) {
            try {
                String currentLang = PreferencesUtility.getMkbLanguageName(this.mContext);
                itemHolder.currentLang.setText(currentLang.substring(0, 1).toUpperCase() + currentLang.substring(1));
                if (this.mkbVideo == null) {
                    itemHolder.type.setVisibility(View.GONE);
                    itemHolder.allVideos.setVisibility(View.GONE);
                    itemHolder.trackDate.setVisibility(View.GONE);
                } else if (this.mkbVideo.isLive().equals(ServerProtocol.DIALOG_RETURN_SCOPES_TRUE)) {
                    itemHolder.type.setText("Live");
                    itemHolder.type.setVisibility(View.VISIBLE);
                    itemHolder.trackName.setText(this.mkbVideo.getName());
                    itemHolder.trackDate.setText(this.mkbVideo.getDate());
                    String thumbnailUrl = YouTubeThumbnail.getUrlFromVideoId(this.mkbVideo.getVideo_id(), Quality.FIRST);
                    Log.v("THUMB_URL", "THUMB_URL: " + thumbnailUrl);
                    if (!(thumbnailUrl == null || thumbnailUrl.equals(""))) {
                        Picasso.with(this.mContext).load(thumbnailUrl).into(itemHolder.thubmnail);
                    }
                    itemHolder.allVideos.setOnClickListener(new C10411());
                } else {
                    itemHolder.type.setVisibility(View.GONE);
                    itemHolder.allVideos.setVisibility(View.GONE);
                    itemHolder.thubmnail.setImageResource(R.drawable.mkb_header);
                    itemHolder.trackName.setText("PM's 'Mann Ki Baat");
                    itemHolder.trackDate.setMaxLines(5);
                    itemHolder.trackDate.setVisibility(View.GONE);
                }
                itemHolder.changeLang.setOnClickListener(new C10422());
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        MkbAudio object = (MkbAudio) this.arraylist.get(i - 1);
        try {
            itemHolder.trackName.setText(object.mTitle);
            itemHolder.trackDate.setText(DateUtil.dateToString(DateUtil.stringToDate(object.mDate)));
            if (object.mImage != null && !object.mImage.equals("")) {
                Picasso.with(this.mContext).load(object.mImage).into(itemHolder.thubmnail);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public int getItemCount() {
        return this.arraylist.size() + 1;
    }

    private void playStream(MkbAudio track) {
        this.trackModel = track;
        if (this.listener != null) {
            this.listener.onMetaChanged();
            this.listener.onMusicStreaming();
        }
        if (getService() != null) {
            getService().stopPlayer();
            getService().setCurrentTrack(track);
            this.attempts++;
            new RetrieveStreamUrl().execute(new String[]{track.mSoundCloudLink});
            if (getService() != null) {
                getService().playSong();
            }
        }
    }

    public void setMusicStateLstener(MusicStateAdapterListener listener) {
        this.listener = listener;
    }

    private MusicService getService() {
        return ((BaseActivity) this.mContext).musicSrv;
    }

    public void updateDataSet(List<MkbAudio> list) {
        this.arraylist = list;
        notifyDataSetChanged();
    }

    public void addDataSet(List<MkbAudio> list) {
        this.arraylist.addAll(list);
        notifyDataSetChanged();
    }

    public void updateHeader(MkbVideo video) {
        this.mkbVideo = video;
        notifyItemChanged(0);
    }

    private void startYoutubeActivity(String videoId) {
        Intent intent = new Intent(this.mContext, YouTubePlayerActivity.class);
        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, videoId);
        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, PlayerStyle.DEFAULT);
        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO_START_WITH_LANDSCAPE);
        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
        //intent.setFlags(VCardConfig.FLAG_REFRAIN_QP_TO_NAME_PROPERTIES);
        this.mContext.startActivity(intent);
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    private void showLanguageFragment() {
        MkbLanguageFragment fragment = new MkbLanguageFragment();
        fragment.setOriginalAdapter(this);
        fragment.show(((AppCompatActivity) this.mContext).getSupportFragmentManager(), "Dialog Fragment");
    }

    public void updateLanguage() {
        this.arraylist.clear();
        notifyDataSetChanged();
        if (this.fragment != null) {
            if (this.fragment.mScrollListener != null)
                this.fragment.mScrollListener.resetAllData();
            this.fragment.getTracks(1);
            this.fragment.clearPlayingBar();
        }
    }

    public static Date isoStringToDate(String string) {
        try {
            return new SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ssZ").parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}

