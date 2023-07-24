
package com.sanskrit.pmo.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.sanskrit.pmo.Activities.BaseActivity;
import com.sanskrit.pmo.Activities.MKBActivity;
import com.sanskrit.pmo.Adapters.MKBAudioAdapter;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.MygovClient;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.server.SanskritClient;
import com.sanskrit.pmo.network.server.models.MkbAudio;
import com.sanskrit.pmo.network.server.models.MkbVideo;
import com.sanskrit.pmo.network.server.models.MkbVideoResponse;
import com.sanskrit.pmo.player.MusicService;
import com.sanskrit.pmo.player.MusicStateAdapterListener;
import com.sanskrit.pmo.player.MusicStateListener;
import com.sanskrit.pmo.uiwidgets.DividerItemDecoration;
import com.sanskrit.pmo.uiwidgets.fabbutton.FabButton;
import com.sanskrit.pmo.uiwidgets.fabbutton.PlayPauseDrawable;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.DateUtil;
import com.sanskrit.pmo.utils.EndlessRecyclerOnScrollListener;
import com.sanskrit.pmo.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MKBAudioFragment extends Fragment {
    MKBAudioAdapter adapter;
    TextView currentTrackDate;
    ImageView currentTrackImage;
    TextView currentTrackTitle;
    private boolean isBuffering = false;
    public Runnable mUpdateCircularProgress = new Runnable() {
        public void run() {
            if (!MKBAudioFragment.this.isBuffering && MKBAudioFragment.this.getActivity() != null && MKBAudioFragment.this.getService() != null) {
                if (MKBAudioFragment.this.playpause != null) {
                    MKBAudioFragment.this.playpause.setProgress((float) MKBAudioFragment.this.getService().getPosition());
                }
                if (MKBAudioFragment.this.getService().isPlaying()) {
                    MKBAudioFragment.this.playpause.postDelayed(MKBAudioFragment.this.mUpdateCircularProgress, 50);
                }
            }
        }
    };
    PlayPauseDrawable playPauseDrawable;
    View playingBar;
    FabButton playpause;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefresh;
    public EndlessRecyclerOnScrollListener mScrollListener = null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mankibaat_audio, container, false);
        this.recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        this.playpause = (FabButton) rootView.findViewById(R.id.playpause);
        this.playingBar = rootView.findViewById(R.id.playingBar);
        this.swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        this.currentTrackTitle = (TextView) rootView.findViewById(R.id.track_title);
        this.currentTrackDate = (TextView) rootView.findViewById(R.id.track_date);
        this.currentTrackImage = (ImageView) rootView.findViewById(R.id.album_art_nowplayingcard);
        this.playPauseDrawable = new PlayPauseDrawable(getResources(), 0.8f);
        this.playPauseDrawable.setColor(getResources().getColor(17170443));
        this.playPauseDrawable.setShape(2);
        this.playPauseDrawable.setYOffset(0);
        this.playpause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MKBAudioFragment.this.playPauseDrawable.getCurrentShape() == 2) {
                    MKBAudioFragment.this.playPauseDrawable.setShape(0);
                    MKBAudioFragment.this.playpause.showProgress(true);
                    MKBAudioFragment.this.playpause.setIndeterminate(true);
                } else {
                    MKBAudioFragment.this.playPauseDrawable.setShape(2);
                }
                if (MKBAudioFragment.this.getService() != null) {
                    MKBAudioFragment.this.getService().playOrPause();
                }
            }
        });
        this.swipeRefresh.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mScrollListener != null) {
                    mScrollListener.resetAllData();
                }
                MKBAudioFragment.this.getTracks(1);
            }
        });
        this.playpause.setIconDrawable(this.playPauseDrawable);
        return rootView;
    }

    private void showSwipeRefresh() {
        this.swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                MKBAudioFragment.this.swipeRefresh.setRefreshing(true);

            }
        });
    }

    private void hideSwipeRefresh() {
        this.swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                MKBAudioFragment.this.swipeRefresh.setRefreshing(false);

            }
        });
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPlayingBarDetails();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        this.recyclerView.setLayoutManager(llm);
     /*   this.recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            public void onLoadMore(int current_page) {
                MKBAudioFragment.this.getTracks(current_page);
            }
        });*/

        mScrollListener = new EndlessRecyclerOnScrollListener(this.recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                MKBAudioFragment.this.getTracks(current_page);
            }
        };

        recyclerView.addOnScrollListener(mScrollListener);

        setUpAdapter();
        try {
            if (Reservoir.contains(Constants.CACHE_MKB_LISTINGS)) {
                try {
                    this.adapter.updateDataSet((List) Reservoir.get(Constants.CACHE_MKB_LISTINGS, new Token().getType()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (Utils.isOnline(getActivity())) {
            getTracks(1);
        }
        setHeader();
        updatePlayPauseState();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MKBAudioFragment.this.getActivity() == null || MKBAudioFragment.this.getService() == null) {
                    Log.d("service", "getservice is null");
                } else {
                    MKBAudioFragment.this.getService().setMusicStateLstener(new MusicStateListener() {
                        @Override
                        public void onMusicBufferingStarted() {
                            MKBAudioFragment.this.isBuffering = true;
                            MKBAudioFragment.this.playpause.showProgress(true);
                            MKBAudioFragment.this.playpause.setIndeterminate(true);
                        }

                        @Override
                        public void onMusicBufferingEnded() {
                            MKBAudioFragment.this.isBuffering = false;
                            MKBAudioFragment.this.playpause.setIndeterminate(false);
                            MKBAudioFragment.this.startProgressRunnable();
                        }

                        @Override
                        public void onMusicStreaming() {
                            MKBAudioFragment.this.playpause.showProgress(true);
                            MKBAudioFragment.this.playpause.setIndeterminate(true);
                            MKBAudioFragment.this.playPauseDrawable.setShape(0);
                        }

                        @Override
                        public void onMusicStarted() {
                            MKBAudioFragment.this.isBuffering = false;
                            MKBAudioFragment.this.playpause.setIndeterminate(false);
                            MKBAudioFragment.this.startProgressRunnable();
                            MKBAudioFragment.this.updatePlayPauseState();
                        }

                        @Override
                        public void onMetaChanged() {
                            MKBAudioFragment.this.setPlayingBarDetails();
                        }

                        @Override
                        public void onStateChanged() {
                            Log.d("service", "onstatechanged");
                            MKBAudioFragment.this.updatePlayPauseState();
                        }
                    });
                }
            }
        }, 1000);
        this.playpause.showProgress(false);
    }

    private void setHeader() {
        try {
            if (Reservoir.contains(Constants.CACHE_MKB_VIDEOS_HEADER)) {
                try {
                    this.adapter.updateHeader((MkbVideo) Reservoir.get(Constants.CACHE_MKB_VIDEOS_HEADER, MkbVideo.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        SanskritClient.getInstance(getActivity()).getMKBVideo(new GenericCallback() {
            @Override
            public void failure() {

            }

            @Override
            public void success(Object response) {
                MkbVideoResponse videoResponse = (MkbVideoResponse) response;
                try {
                    Reservoir.put(Constants.CACHE_MKB_VIDEOS, videoResponse.getResults());
                    Reservoir.put(Constants.CACHE_MKB_VIDEOS_HEADER, videoResponse.getResults().get(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MKBAudioFragment.this.adapter.updateHeader((MkbVideo) videoResponse.getResults().get(0));

            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_mkb_audio, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_mkb_changeto_video) {
            Intent intent = new Intent(getActivity(), MKBActivity.class);
            intent.setAction("Video");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpAdapter() {
        this.adapter = new MKBAudioAdapter(getActivity(), new ArrayList(), this);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                MKBAudioFragment.this.swipeRefresh.setEnabled(((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        if (getActivity() != null) {
            this.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        }
        if (getActivity() != null) {
            this.recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 1));
        }
        this.adapter.setMusicStateLstener(new MusicStateAdapterListener() {
            public void onMusicBufferingStarted() {
            }

            public void onMusicBufferingEnded() {
            }

            public void onMusicStreaming() {
                MKBAudioFragment.this.isBuffering = true;
                MKBAudioFragment.this.playpause.showProgress(true);
                MKBAudioFragment.this.playpause.setIndeterminate(true);
                MKBAudioFragment.this.playPauseDrawable.setShape(0);
            }

            public void onMusicStarted() {
            }

            public void onMetaChanged() {
                MKBAudioFragment.this.setPlayingBarDetails();
            }

            public void onStateChanged() {
                MKBAudioFragment.this.updatePlayPauseState();
            }
        });
    }

    public void getTracks(final int page) {
        showSwipeRefresh();
        MygovClient.getInstance(getActivity()).getMkbEpisodes(PreferencesUtility.getMkbLanguageCode(getActivity()), String.valueOf(page), new GenericCallback() {
            public void success(Object response) {
                List<MkbAudio> audioList = (List) response;
                if (!(audioList == null || audioList.size() == 0)) {
                    if (page == 1) {
                        MKBAudioFragment.this.adapter.updateDataSet(audioList);
                        try {
                            Reservoir.put(Constants.CACHE_MKB_LISTINGS, audioList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        MKBAudioFragment.this.adapter.addDataSet(audioList);
                    }
                }
                MKBAudioFragment.this.hideSwipeRefresh();
            }

            public void failure() {
                MKBAudioFragment.this.hideSwipeRefresh();
            }
        });
    }

    private MusicService getService() {
        return ((BaseActivity) getActivity()).musicSrv;
    }

    public void onResume() {
        super.onResume();
        updatePlayPauseState();
    }

    private void startProgressRunnable() {
        if (this.playpause != null && getActivity() != null && getService() != null && !this.isBuffering) {
            this.playpause.showProgress(true);
            this.playpause.setIndeterminate(false);
            this.playpause.setProgress((float) getService().getTotalDuration());
            if (this.mUpdateCircularProgress != null) {
                this.playpause.removeCallbacks(this.mUpdateCircularProgress);
            }
            this.playpause.postDelayed(this.mUpdateCircularProgress, 10);
        }
    }

    private void setPlayingBarDetails() {
        if (getActivity() == null) {
            return;
        }
        if (PreferencesUtility.getMkbCurrentTrack(getActivity()) == null) {
            this.playingBar.setVisibility(View.GONE);
            return;
        }
        this.playingBar.setVisibility(View.VISIBLE);
        this.currentTrackTitle.setText(PreferencesUtility.getMkbCurrentTrack(getActivity()));
        this.currentTrackDate.setText(DateUtil.dateToString(DateUtil.stringToDate(PreferencesUtility.getMkbCurrentTrackdate(getActivity()))));

        Log.v("AUDIO_TRACK_IMAGE", "IMAGE: " + PreferencesUtility.getMkbCurrentTrackImage(getActivity()));
        if (getActivity() != null && PreferencesUtility.getMkbCurrentTrackImage(getActivity()) != null) {
            Picasso.with(getActivity()).load(PreferencesUtility.getMkbCurrentTrackImage(getActivity())).into(this.currentTrackImage);
        }
    }

    public void clearPlayingBar() {
        if (getActivity() != null) {
            PreferencesUtility.setCurrentMKBTrackImage(getActivity(), null);
            PreferencesUtility.setCurrentMKBTrackDate(getActivity(), null);
            PreferencesUtility.setCurrentMKBTrackID(getActivity(), null);
            PreferencesUtility.setCurrentMKBTrackName(getActivity(), null);
            PreferencesUtility.setCurrentMKBTrackURL(getActivity(), null);
        }
        if (getService() != null) {
            getService().stopAndRelease();
        }
        this.playingBar.setVisibility(View.GONE);
    }

    private void updatePlayPauseState() {
        if (getActivity() != null && getService() != null) {
            if (getService().isPlaying()) {
                this.playpause.showProgress(true);
                startProgressRunnable();
                this.playPauseDrawable.setShape(0);
                return;
            }
            this.playpause.showProgress(false);
            this.playPauseDrawable.setShape(2);
            if (getService().isPrepared) {
                startProgressRunnable();
            }
        }
    }


    class Token extends TypeToken<List<MkbAudio>> {
        Token() {
        }
    }


}

