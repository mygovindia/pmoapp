package com.sanskrit.pmo.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.sanskrit.pmo.Activities.BaseActivity;
import com.sanskrit.pmo.Activities.NewsReaderActivity;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.models.NewsFeed;
import com.sanskrit.pmo.network.server.models.MkbAudio;
import com.sanskrit.pmo.network.server.models.MkbVideo;
import com.sanskrit.pmo.player.MusicService;
import com.sanskrit.pmo.player.MusicStateAdapterListener;
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

/**
 * Created by manoj on 28/3/18.
 */

public class SearchMKBAdapter extends RecyclerView.Adapter<SearchMKBAdapter.ItemHolder> {
    private List<MkbAudio> arraylist;
    private int attempts = 0;
    private MusicStateAdapterListener listener;
    private Activity mContext;
    private MkbVideo mkbVideo;
    private MkbAudio trackModel;


    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView read = ((TextView) this.itemView.findViewById(R.id.read_more));
        protected ImageView thubmnail = ((ImageView) this.itemView.findViewById(R.id.thumbnail));
        protected TextView trackDate = ((TextView) this.itemView.findViewById(R.id.track_date));
        protected TextView trackName = ((TextView) this.itemView.findViewById(R.id.track_name));
        protected TextView type = ((TextView) this.itemView.findViewById(R.id.type));

        public ItemHolder(View view) {
            super(view);
            view.setOnClickListener(this);
//            if (this.read != null) {
//                this.read.setOnClickListener(new View.OnClickListener() {
//                    public void onClick(View v) {
//                        try {
//                            MkbAudio audio = (MkbAudio) SearchMKBAdapter.this.arraylist.get(SearchMKBAdapter.ItemHolder.this.getAdapterPosition() - 1);
//                            NewsFeed feed = new NewsFeed();
//                            feed.mCOntent = audio.mCOntent;
//                            feed.mDate = audio.mDate;
//                            feed.mTitle = audio.mTitle;
//                            Intent intent = new Intent(SearchMKBAdapter.this.mContext, NewsReaderActivity.class);
//                            intent.putExtra("newsitem", Parcels.wrap(feed));
//                            intent.putExtra("showShare", false);
//                            intent.putExtra("isFrom", true);
//                            SearchMKBAdapter.this.mContext.startActivity(intent);
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
                MkbAudio audio = (MkbAudio) SearchMKBAdapter.this.arraylist.get(SearchMKBAdapter.ItemHolder.this.getAdapterPosition());
                NewsFeed feed = new NewsFeed();
                feed.mCOntent = audio.mCOntent;
                feed.mDate = audio.mDate;
                feed.mTitle = audio.mTitle;
                Intent intent = new Intent(SearchMKBAdapter.this.mContext, NewsReaderActivity.class);
                intent.putExtra("newsitem", Parcels.wrap(feed));
                intent.putExtra("showShare", false);
                intent.putExtra("isFrom", true);
                SearchMKBAdapter.this.mContext.startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
//            try {
//
//                PreferencesUtility.setCurrentMKBTrackName(SearchMKBAdapter.this.mContext, ((MkbAudio) SearchMKBAdapter.this.arraylist.get(getAdapterPosition())).mTitle);
//                PreferencesUtility.setCurrentMKBTrackDate(SearchMKBAdapter.this.mContext, ((MkbAudio) SearchMKBAdapter.this.arraylist.get(getAdapterPosition())).mDate);
//                PreferencesUtility.setCurrentMKBTrackImage(SearchMKBAdapter.this.mContext, ((MkbAudio) SearchMKBAdapter.this.arraylist.get(getAdapterPosition())).mImage);
//                SearchMKBAdapter.this.playStream((MkbAudio) SearchMKBAdapter.this.arraylist.get(getAdapterPosition()));
//           /*else if (SearchMKBAdapter.this.mkbVideo != null) {
//                SearchMKBAdapter.this.startYoutubeActivity(SearchMKBAdapter.this.mkbVideo.getVideo_id());
//            }*/
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    class RetrieveStreamUrl extends AsyncTask<String, Void, String> {

        class C10441 implements Runnable {
            C10441() {
            }

            public void run() {
                SearchMKBAdapter.this.attempts = SearchMKBAdapter.this.attempts + 1;
                new SearchMKBAdapter.RetrieveStreamUrl().execute(new String[]{SearchMKBAdapter.this.trackModel.mSoundCloudLink});
            }
        }

        RetrieveStreamUrl() {
        }

        protected String doInBackground(String... urls) {
            try {
                HttpURLConnection ucon = (HttpURLConnection) new URL(urls[0]).openConnection();
                ucon.setInstanceFollowRedirects(false);
                return new URL(ucon.getHeaderField(HttpRequest.HEADER_LOCATION)).toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                if (SearchMKBAdapter.this.attempts >= 2 || SearchMKBAdapter.this.trackModel == null) {
                    return "";
                }
                SearchMKBAdapter.this.mContext.runOnUiThread(new SearchMKBAdapter.RetrieveStreamUrl.C10441());
                return "";
            } catch (IOException e2) {
                e2.printStackTrace();
                return "";
            }
        }

        protected void onPostExecute(String url) {
            if (url == null || url.equals("")) {
                Toast.makeText(SearchMKBAdapter.this.mContext, "Unable to stream", Toast.LENGTH_SHORT).show();
                return;
            }
            PreferencesUtility.setCurrentMKBTrackURL(SearchMKBAdapter.this.mContext, url);
            if (SearchMKBAdapter.this.getService() != null) {
                SearchMKBAdapter.this.getService().playSong();
            }
        }
    }

    public SearchMKBAdapter(Activity context, List<MkbAudio> arraylist) {
        this.mContext = context;
        this.arraylist = arraylist;

    }

    public SearchMKBAdapter.ItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       /* if (i == 0) {
            return new SearchMKBAdapter.ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mkb_header, viewGroup, false));
        }*/
        return new SearchMKBAdapter.ItemHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mkb_audio, viewGroup, false));
    }

    public void onBindViewHolder(SearchMKBAdapter.ItemHolder itemHolder, int i) {

        MkbAudio object = (MkbAudio) this.arraylist.get(i);
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
        return this.arraylist.size();
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
            new SearchMKBAdapter.RetrieveStreamUrl().execute(new String[]{track.mSoundCloudLink});
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


    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
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
