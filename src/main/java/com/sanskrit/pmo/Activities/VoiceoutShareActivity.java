package com.sanskrit.pmo.Activities;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.sanskrit.pmo.Models.SMSContact;
import com.sanskrit.pmo.R;
import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.Result;
import com.sanskrit.pmo.twitter.core.TwitterApiClient;
import com.sanskrit.pmo.twitter.core.TwitterCore;
import com.sanskrit.pmo.twitter.core.TwitterException;
import com.sanskrit.pmo.twitter.core.TwitterSession;
import com.sanskrit.pmo.twitter.core.models.Tweet;
import com.sanskrit.pmo.uiwidgets.DividerItemDecoration;
import com.sanskrit.pmo.utils.Constants;
import com.sanskrit.pmo.utils.ShareObject;
import com.sanskrit.pmo.utils.Utils;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed.Builder;
import com.sromku.simple.fb.listeners.OnPublishListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.sanskrit.pmo.Fragments.ShareFragment.LINE_SEPARATOR_UNIX;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VoiceoutShareActivity extends AppCompatActivity {
    private static final String DELIVERED = "com.sanskrit.pmo.voiceout.DELIVERED";
    private static final String SENT = "com.sanskrit.pmo.voiceout.SENT";
    ShareAdapter adapter;
    boolean alreadyInWhatsapp = false;
    private List<SMSContact> contacts;
    ShareStatus facebookStatus;
    private int mMessageSentCount;
    private int mMessageSentParts;
    private int mMessageSentTotalParts;
    OnPublishListener onPublishListener;
    boolean onlyWhatsapp = true;
    RecyclerView recyclerView;
    ShareObject shareObject;
    List<ShareStatus> shareStatusList = new ArrayList();
    String shareString;
    boolean shouldWait = false;
    SimpleFacebook simpleFacebook;
    ShareStatus smsStatus;
    Toolbar toolbar;
    ShareStatus twitterStatus;
    ShareStatus whatsappStatus;

    class Token extends TypeToken<List<SMSContact>> {
        Token() {
        }
    }


    class PublishToFacebook extends OnPublishListener {
        PublishToFacebook() {
        }

        public void onComplete(String postId) {
            VoiceoutShareActivity.this.facebookStatus.setStatus("Shared to Facebook");
            VoiceoutShareActivity.this.facebookStatus.setResId(R.drawable.ic_done_black_18dp);
            VoiceoutShareActivity.this.adapter.notifyDataSetChanged();
        }

        public void onFail(String reason) {
            VoiceoutShareActivity.this.facebookStatus.setStatus("Error - " + reason);
            VoiceoutShareActivity.this.facebookStatus.setResId(-1);
            VoiceoutShareActivity.this.adapter.notifyDataSetChanged();
        }

        public void onException(Throwable throwable) {
            VoiceoutShareActivity.this.facebookStatus.setStatus("Error sharing to Facebook");
            VoiceoutShareActivity.this.facebookStatus.setResId(-1);
            VoiceoutShareActivity.this.adapter.notifyDataSetChanged();
        }
    }

    class SMSSentBroadCast extends BroadcastReceiver {
        SMSSentBroadCast() {
        }

        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode()) {
                case -1:
                    VoiceoutShareActivity.this.mMessageSentParts = VoiceoutShareActivity.this.mMessageSentParts + 1;
                    if (VoiceoutShareActivity.this.mMessageSentParts == VoiceoutShareActivity.this.mMessageSentTotalParts) {
                        VoiceoutShareActivity.this.mMessageSentCount = VoiceoutShareActivity.this.mMessageSentCount + 1;
                        VoiceoutShareActivity.this.sendNextMessage();
                        return;
                    }
                    return;
                case 1:
                    VoiceoutShareActivity.this.smsStatus.setStatus("Error sending messages");
                    VoiceoutShareActivity.this.smsStatus.setResId(-1);
                    VoiceoutShareActivity.this.adapter.notifyDataSetChanged();
                    return;
                case 2:
                    VoiceoutShareActivity.this.smsStatus.setStatus("Error sending messages");
                    VoiceoutShareActivity.this.smsStatus.setResId(-1);
                    VoiceoutShareActivity.this.adapter.notifyDataSetChanged();
                    return;
                case 3:
                    VoiceoutShareActivity.this.smsStatus.setStatus("Error sending messages");
                    VoiceoutShareActivity.this.smsStatus.setResId(-1);
                    VoiceoutShareActivity.this.adapter.notifyDataSetChanged();
                    return;
                case 4:
                    VoiceoutShareActivity.this.smsStatus.setStatus("Error sending messages");
                    VoiceoutShareActivity.this.smsStatus.setResId(-1);
                    VoiceoutShareActivity.this.adapter.notifyDataSetChanged();
                    return;
                default:
                    return;
            }
        }
    }

    class SMSDeliverBroadCast extends BroadcastReceiver {
        SMSDeliverBroadCast() {
        }

        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode()) {
                case -1:
                    VoiceoutShareActivity.this.smsStatus.setStatus("SMS delivered");
                    VoiceoutShareActivity.this.adapter.notifyDataSetChanged();
                    return;
                case 0:
                    VoiceoutShareActivity.this.smsStatus.setStatus("SMS not delivered");
                    VoiceoutShareActivity.this.smsStatus.setResId(-1);
                    VoiceoutShareActivity.this.adapter.notifyDataSetChanged();
                    return;
                default:
                    return;
            }
        }
    }

    private class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ViewHolder> {
        private Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            ProgressBar progressBar;
            TextView status;

            public ViewHolder(View itemView) {
                super(itemView);
                this.status = (TextView) itemView.findViewById(R.id.share_status_text);
                this.image = (ImageView) itemView.findViewById(R.id.share_status_image);
                this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
                itemView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (VoiceoutShareActivity.this.shareStatusList.get(ViewHolder.this.getAdapterPosition()) == VoiceoutShareActivity.this.whatsappStatus) {
                            VoiceoutShareActivity.this.shareToWhatsapp();
                        }
                    }
                });
            }
        }

        public ShareAdapter(Context context, List<ShareStatus> list) {
            this.context = context;
        }

        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_share_status, viewGroup, false));
        }

        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.status.setText(((ShareStatus) VoiceoutShareActivity.this.shareStatusList.get(i)).getStatus());
            viewHolder.progressBar.setVisibility(View.GONE);
            viewHolder.image.setVisibility(View.GONE);
            if (((ShareStatus) VoiceoutShareActivity.this.shareStatusList.get(i)).getResId() == -1) {
                viewHolder.image.setVisibility(View.GONE);
            } else if (((ShareStatus) VoiceoutShareActivity.this.shareStatusList.get(i)).getResId() == -2) {
                viewHolder.image.setVisibility(View.GONE);
                viewHolder.progressBar.setVisibility(View.VISIBLE);
            } else {
                viewHolder.image.setVisibility(View.VISIBLE);
                viewHolder.image.setImageResource(((ShareStatus) VoiceoutShareActivity.this.shareStatusList.get(i)).getResId());
            }
        }

        public int getItemCount() {
            return VoiceoutShareActivity.this.shareStatusList.size();
        }
    }

    private class ShareStatus {
        int resId;
        String status;

        private ShareStatus() {
        }

        public String getStatus() {
            return this.status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getResId() {
            return this.resId;
        }

        public void setResId(int resId) {
            this.resId = resId;
        }
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_voiceout_share);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle((CharSequence) "Voiceout Share");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        this.recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
        this.adapter = new ShareAdapter(this, new ArrayList());
        this.recyclerView.setAdapter(this.adapter);
        this.shareObject = (ShareObject) Parcels.unwrap(getIntent().getParcelableExtra("shareObject"));
        if (this.shareObject == null) {
            finish();
        }
        if (Utils.is15()) {
            this.simpleFacebook = SimpleFacebook.getInstance(this);
        }
        this.shareString = this.shareObject.getTitle() + LINE_SEPARATOR_UNIX + this.shareObject.getUrl();
        if (getIntent().getBooleanExtra("twitter", false)) {
            this.twitterStatus = new ShareStatus();
            this.shareStatusList.add(this.twitterStatus);
            this.shouldWait = true;
            this.onlyWhatsapp = false;
            shareToTwitter(this.shareString);
        }
        if (getIntent().getBooleanExtra("facebook", false)) {
            this.facebookStatus = new ShareStatus();
            this.shareStatusList.add(this.facebookStatus);
            this.shouldWait = true;
            this.onlyWhatsapp = false;
            publishToFacebook();
        }
        if (getIntent().getBooleanExtra("sms", false)) {
            if (!Utils.isMarshmallow()) {
                this.onlyWhatsapp = false;
                this.contacts = new ArrayList();
                try {
                    if (Reservoir.contains(Constants.CACHE_SMS_CONTACTS)) {
                        try {
                            this.contacts = (List) Reservoir.get(Constants.CACHE_SMS_CONTACTS, new Token().getType());
                            if (!(this.contacts == null || this.contacts.size() == 0)) {
                                this.smsStatus = new ShareStatus();
                                this.shareStatusList.add(this.smsStatus);
                                this.shouldWait = true;
                                startSendMessages();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                return;
            }
        }
        if (getIntent().getBooleanExtra("whatsapp", false)) {
            this.whatsappStatus = new ShareStatus();
            this.whatsappStatus.setStatus("Click to Share to WhatsApp");
            this.whatsappStatus.setResId(-1);
            this.adapter.notifyDataSetChanged();
            this.shareStatusList.add(this.whatsappStatus);
            shareToWhatsapp();
            if (this.onlyWhatsapp) {
                finish();
            }
        }
        this.adapter.notifyDataSetChanged();
    }

    private void shareToTwitter(String status) {
        String trimmedStatus;
        this.twitterStatus.setStatus("Sharing to Twitter...");
        this.twitterStatus.setResId(-2);
        this.adapter.notifyDataSetChanged();
        TwitterApiClient client = TwitterCore.getInstance().getApiClient((TwitterSession) TwitterCore.getInstance().getSessionManager().getActiveSession());
        if (status.length() > 139) {
            trimmedStatus = status.substring(0, 139);
        } else {
            trimmedStatus = status;
        }
        client.getStatusesService().update(trimmedStatus, new Callback<Tweet>() {
            @Override
            public void failure(TwitterException twitterException) {
                Log.e("Test", "failure: " + twitterException.getMessage());
                Log.e("Test", "failure: " + twitterException.getLocalizedMessage());

                if (twitterException.getMessage().contains("403 Forbidden")) {
                    VoiceoutShareActivity.this.twitterStatus.setStatus("You have already shared this earlier!");
                    VoiceoutShareActivity.this.twitterStatus.setResId(-1);
                    VoiceoutShareActivity.this.adapter.notifyDataSetChanged();
                    VoiceoutShareActivity.this.shouldWait = false;
                    VoiceoutShareActivity.this.shareToWhatsapp();
                } else {
                    VoiceoutShareActivity.this.twitterStatus.setStatus("Error occurred while sharing");
                    VoiceoutShareActivity.this.twitterStatus.setResId(-1);
                    VoiceoutShareActivity.this.adapter.notifyDataSetChanged();
                    VoiceoutShareActivity.this.shouldWait = false;
                    VoiceoutShareActivity.this.shareToWhatsapp();
                }
            }

            @Override
            public void success(Result<Tweet> result) {
                if (result != null) {
                    try {
                        if (result.data != null) {
                            VoiceoutShareActivity.this.twitterStatus.setStatus(VoiceoutShareActivity.this.getString(R.string.shared_to_twitter));
                            VoiceoutShareActivity.this.twitterStatus.setResId(R.drawable.ic_done_black_18dp);
                            VoiceoutShareActivity.this.adapter.notifyDataSetChanged();
                            VoiceoutShareActivity.this.shouldWait = false;
                            VoiceoutShareActivity.this.shareToWhatsapp();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }


    private void publishToFacebook() {
        this.facebookStatus.setStatus("Sharing to Facebook...");
        this.facebookStatus.setResId(-2);
        this.adapter.notifyDataSetChanged();
        this.onPublishListener = new PublishToFacebook();
        try {
            this.simpleFacebook.publish
                    (new Builder().setName(shareObject.getTitle()).setLink(shareObject.getUrl()).build(), this.onPublishListener);
        } catch (Exception e) {
            this.facebookStatus.setStatus("Error sharing to Facebook");
            this.facebookStatus.setResId(-1);
            this.adapter.notifyDataSetChanged();
            e.printStackTrace();
        }
        this.shouldWait = false;
        shareToWhatsapp();
    }

    private void startSendMessages() {
        registerBroadCastReceivers();
        this.mMessageSentCount = 0;
        sendSMS(((SMSContact) this.contacts.get(this.mMessageSentCount)).getNumber(), ((SMSContact) this.contacts.get(this.mMessageSentCount)).getName(), this.shareString);
    }

    private void sendNextMessage() {
        if (thereAreSmsToSend()) {
            sendSMS(((SMSContact) this.contacts.get(this.mMessageSentCount)).getNumber(), ((SMSContact) this.contacts.get(this.mMessageSentCount)).getName(), this.shareString);
            return;
        }
        this.shouldWait = false;
        shareToWhatsapp();
        this.smsStatus.setStatus("All SMS have been sent");
        this.smsStatus.setResId(R.drawable.ic_done_black_18dp);
        this.adapter.notifyDataSetChanged();
    }

    private boolean thereAreSmsToSend() {
        return this.mMessageSentCount < this.contacts.size();
    }

    private void sendSMS(String phoneNumber, String name, String message) {
        this.smsStatus.setStatus("Sending SMS to " + name);
        this.smsStatus.setResId(-2);
        this.adapter.notifyDataSetChanged();
        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage(message);
        this.mMessageSentTotalParts = parts.size();
        Log.i("Message Count", "Message Count: " + this.mMessageSentTotalParts);
        ArrayList<PendingIntent> deliveryIntents = new ArrayList();
        ArrayList<PendingIntent> sentIntents = new ArrayList();
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
        for (int j = 0; j < this.mMessageSentTotalParts; j++) {
            sentIntents.add(sentPI);
            deliveryIntents.add(deliveredPI);
        }
        this.mMessageSentParts = 0;
        sms.sendMultipartTextMessage(phoneNumber, null, parts, sentIntents, deliveryIntents);
    }

    private void shareToWhatsapp() {
        if (getIntent().getBooleanExtra("whatsapp", false) && !this.alreadyInWhatsapp && !this.shouldWait) {
            boolean packageFound = false;
            try {
                Intent intent = new Intent("android.intent.action.SEND");
                if (this.shareObject.getImageUri() == null) {
                    intent.putExtra("android.intent.extra.TEXT", this.shareString);
                    intent.setType("text/plain");
                } else {
                    intent.putExtra("android.intent.extra.STREAM", this.shareObject.getImageUri());
                    intent.setType("image/*");
                }
                for (ResolveInfo info : getPackageManager().queryIntentActivities(intent, 0)) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.whatsapp")) {
                        intent.setPackage(info.activityInfo.packageName);
                        this.alreadyInWhatsapp = true;
                        startActivity(intent);
                        packageFound = true;
                        break;
                    }
                }
                if (!packageFound) {
                    this.whatsappStatus.setStatus("Whatsapp not installed");
                    this.whatsappStatus.setResId(-1);
                    this.adapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void registerBroadCastReceivers() {
        registerReceiver(new SMSSentBroadCast(), new IntentFilter(SENT));
        registerReceiver(new SMSDeliverBroadCast(), new IntentFilter(DELIVERED));
    }


}
