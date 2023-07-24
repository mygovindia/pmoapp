package com.sanskrit.pmo.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anupcowkur.reservoir.Reservoir;
import com.google.android.material.snackbar.Snackbar;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.Session.PreferencesUtility;
import com.sanskrit.pmo.network.mygov.MyGovBlogsClient;
import com.sanskrit.pmo.network.mygov.callbacks.GenericCallback;
import com.sanskrit.pmo.network.mygov.models.polls.PollChoice;
import com.sanskrit.pmo.network.mygov.models.polls.PollImagesUnd;
import com.sanskrit.pmo.network.mygov.models.polls.PollModel;
import com.sanskrit.pmo.network.mygov.models.polls.PollQuestionResponse;
import com.sanskrit.pmo.network.mygov.models.polls.PollQuestionsUnd;
import com.sanskrit.pmo.network.mygov.models.polls.PollSubmission;
import com.sanskrit.pmo.network.mygov.models.polls.PollTimeUnd;
import com.sanskrit.pmo.network.server.SanskritCallbackClient;
import com.sanskrit.pmo.network.server.models.MyGovToken;
import com.sanskrit.pmo.uiwidgets.DividerItemDecoration;
import com.sanskrit.pmo.uiwidgets.QuestionView;
import com.sanskrit.pmo.utils.DateUtil;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class PollDetailActivity extends BaseActivity {
    private static final String CACHE_POLl_QUESTIONS = "poll_questions";
    PollQuestionsAdapter adapter;
    boolean alreadySubmitted;
    View container;
    ImageView featureImage;
    PollModel poll;
    boolean pollExpired;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    TextView textTimeRemaining;
    TextView textTotalVotes;
    TextView title;
    Toolbar toolbar;
    float totalVotes;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_poll_detail);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.polls));
        this.poll = (PollModel) Parcels.unwrap(getIntent().getParcelableExtra("poll"));
        this.featureImage = (ImageView) findViewById(R.id.feature_image);
        this.container = findViewById(R.id.container);
        this.title = (TextView) findViewById(R.id.title);
        this.textTotalVotes = (TextView) findViewById(R.id.text_total_votes);
        this.textTimeRemaining = (TextView) findViewById(R.id.text_time_remaining);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.title.setText(this.poll.getTitle());
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.adapter = new PollQuestionsAdapter(this, new ArrayList());
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        if (new Date().after(DateUtil.stringToDate(((PollTimeUnd) this.poll.getField_deadline().getUnd().get(0)).getValue()))) {
            this.pollExpired = true;
            this.textTimeRemaining.setText(R.string.poll_ended);
        } else {
            this.textTimeRemaining.setText(getString(R.string.poll_ends) + DateUtil.dateToPastTenseString(DateUtil.stringToDate(((PollTimeUnd) this.poll.getField_deadline().getUnd().get(0)).getValue())));
        }
        Picasso.with(this).load(((PollImagesUnd) this.poll.getField_theme_image().getUnd().get(0)).getFull_url()).into(this.featureImage);
        try {
            if (Reservoir.contains(generateCacheKey())) {
                setAdapter((PollQuestionResponse) Reservoir.get(generateCacheKey(), PollQuestionResponse.class));
            } else {
                fetchQuestions();
            }
        } catch (Exception e) {
            fetchQuestions();
        }
    }

    private void fetchQuestions() {
        this.progressBar.setVisibility(View.VISIBLE);
        MyGovBlogsClient.getInstance((Context) this, true).getPollQuestionById(((PollQuestionsUnd) this.poll.getField_questions().getUnd().get(0)).getTarget_id(), new GenericCallback() {
            @Override
            public void failure() {
                PollDetailActivity.this.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void success(final Object response) {
                PollDetailActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        PollQuestionResponse question = (PollQuestionResponse) response;
                        try {
                            Reservoir.put(PollDetailActivity.this.generateCacheKey(), question);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        PollDetailActivity.this.setAdapter(question);
                    }
                });
            }
        });

    }

    private void setAdapter(PollQuestionResponse response) {
        this.progressBar.setVisibility(View.GONE);
        this.totalVotes = 0.0f;
        List<PollChoice> choices = new ArrayList(response.getChoice().values());
        for (int a = 0; a < choices.size(); a++) {
            this.totalVotes = ((float) Integer.parseInt(((PollChoice) choices.get(a)).getChvotes())) + this.totalVotes;
        }
        this.textTotalVotes.setText(String.valueOf(Math.round(this.totalVotes)) + getString(R.string.votes));
        this.adapter.updateDataSet(choices);
    }

    private void submitPollAction(String choiceId) {
        Snackbar.make(this.container, (int) R.string.poll_submitting_opinion, -1).show();
        final PollSubmission pollSubmission = new PollSubmission();
        pollSubmission.setId(((PollQuestionsUnd) this.poll.getField_questions().getUnd().get(0)).getTarget_id());
        pollSubmission.setChoice(choiceId);
        SanskritCallbackClient.getInstance(this).getMygovToken(PreferencesUtility.getMygovOauthRefreshToken(this), new GenericCallback() {

            class C10151 implements GenericCallback {
                C10151() {
                }

                public void success(Object res) {
                    Log.e("pollsubmission", (String) res);
                    Snackbar.make(PollDetailActivity.this.container, (int) R.string.poll_submitted, Snackbar.LENGTH_INDEFINITE).show();
                }

                public void failure() {
                    Toast.makeText(PollDetailActivity.this, R.string.poll_error_submitting, Toast.LENGTH_SHORT).show();
                }
            }

            public void success(Object response) {
                MyGovToken myGovToken = (MyGovToken) response;
                if (myGovToken != null) {
                    PreferencesUtility.setMygovOauthAccessToken(PollDetailActivity.this, myGovToken.getAccess_token());
                    PreferencesUtility.setMygovOauthRefreshToken(PollDetailActivity.this, myGovToken.getRefresh_token());
                    MyGovBlogsClient.getInstance(PollDetailActivity.this, new C10151()).submitPoll(PollDetailActivity.this, PreferencesUtility.getMygovOauthToken(PollDetailActivity.this), pollSubmission);
                }
            }

            public void failure() {
            }
        });
    }

    private String generateCacheKey() {
        return CACHE_POLl_QUESTIONS + ((PollQuestionsUnd) this.poll.getField_questions().getUnd().get(0)).getTarget_id() + this.poll.getNid();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private class PollQuestionsAdapter extends RecyclerView.Adapter<PollQuestionsAdapter.ViewHolder> {
        private Context context;
        private List<PollChoice> questions;

        public class ViewHolder extends RecyclerView.ViewHolder {
            QuestionView questionView;
            TextView title;
            TextView votes;

            public ViewHolder(View itemView) {
                super(itemView);
                this.title = (TextView) itemView.findViewById(R.id.question_title);
                this.votes = (TextView) itemView.findViewById(R.id.question_votes);
                this.questionView = (QuestionView) itemView.findViewById(R.id.question_bar);
                itemView.setOnClickListener(new OnClickListener() {

                    class C10171 implements OnClickListener {
                        C10171() {
                        }

                        public void onClick(View v) {
                            PollDetailActivity.this.startActivity(new Intent(PollQuestionsAdapter.this.context, LoginActivity.class).setAction("MyGov"));
                        }
                    }

                    public void onClick(View v) {
                        if (PollDetailActivity.this.pollExpired) {
                            Snackbar.make(PollDetailActivity.this.container, (int) R.string.poll_deadline_expired, Snackbar.LENGTH_INDEFINITE).show();
                        } else if (PreferencesUtility.getMygovLoggedIn(PollQuestionsAdapter.this.context)) {
                            PollDetailActivity.this.submitPollAction(((PollChoice) PollQuestionsAdapter.this.questions.get(ViewHolder.this.getAdapterPosition())).chid);
                        } else {
                            Snackbar.make(PollDetailActivity.this.container, (int) R.string.poll_error_login, -1).setAction((int) R.string.login, new C10171()).show();
                        }
                    }
                });
            }
        }

        public PollQuestionsAdapter(Context context, List<PollChoice> choices) {
            this.context = context;
            this.questions = choices;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_poll_questions, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.title.setText(((PollChoice) this.questions.get(i)).getChtext());
            float progress = (Float.parseFloat(((PollChoice) this.questions.get(i)).getChvotes()) / PollDetailActivity.this.totalVotes) * 100.0f;
            viewHolder.votes.setText(String.valueOf(Math.round(progress)) + " %");
            viewHolder.questionView.setProgress(progress);
        }

        public int getItemCount() {
            return this.questions.size();
        }

        public void updateDataSet(List<PollChoice> choices) {
            this.questions = choices;
            Collections.sort(this.questions);
            notifyDataSetChanged();
        }
    }

}
