/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.sanskrit.pmo.twitter.tweetui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.sanskrit.pmo.R;
import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.Result;
import com.sanskrit.pmo.twitter.core.TwitterException;
import com.sanskrit.pmo.twitter.core.models.Tweet;
import com.sanskrit.pmo.twitter.tweetui.internal.TimelineDelegate;

/**
 * TweetTimelineListAdapter is a ListAdapter which can provide Timeline Tweets to ListViews.
 */
public class TweetTimelineListAdapter extends TimelineListAdapter<Tweet> {
    protected Callback<Tweet> actionCallback;
    final protected int styleResId;

    /**
     * Constructs a TweetTimelineListAdapter for the given Tweet Timeline.
     * @param context the context for row views.
     * @param timeline a Timeline&lt;Tweet&gt; providing access to Tweet data items.
     * @throws IllegalArgumentException if timeline is null
     */
    public TweetTimelineListAdapter(Context context, Timeline<Tweet> timeline) {
        this(context, timeline, R.style.tw__TweetLightStyle, null);
    }

    TweetTimelineListAdapter(Context context, Timeline<Tweet> timeline, int styleResId,
                             Callback<Tweet> cb) {
        this(context, new TimelineDelegate<>(timeline), styleResId, cb);
    }

    TweetTimelineListAdapter(Context context, TimelineDelegate<Tweet> delegate, int styleResId,
            Callback<Tweet> cb) {
        super(context, delegate);
        this.styleResId = styleResId;
        this.actionCallback = new ReplaceTweetCallback(delegate, cb);
    }

    /**
     * Returns a CompactTweetView by default. May be overridden to provide another view for the
     * Tweet item. If Tweet actions are enabled, be sure to call setOnActionCallback(actionCallback)
     * on each new subclass of BaseTweetView to ensure proper success and failure handling
     * for Tweet actions (favorite, unfavorite).
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        final Tweet tweet = getItem(position);
        if (rowView == null) {
            final BaseTweetView tv = new CompactTweetView(context, tweet, styleResId);
            tv.setOnActionCallback(actionCallback);
            rowView = tv;
        } else {
            ((BaseTweetView) rowView).setTweet(tweet);
            ((BaseTweetView) rowView).setOnActionCallback(actionCallback);
        }
        return rowView;
    }

    /*
     * On success, sets the updated Tweet in the TimelineDelegate to replace any old copies
     * of the same Tweet by id.
     */
    static class ReplaceTweetCallback extends Callback<Tweet> {
        TimelineDelegate<Tweet> delegate;
        Callback<Tweet> cb;

        ReplaceTweetCallback(TimelineDelegate<Tweet> delegate, Callback<Tweet> cb) {
            this.delegate = delegate;
            this.cb = cb;
        }

        @Override
        public void success(Result<Tweet> result) {
            delegate.setItemById(result.data);
            if (cb != null) {
                cb.success(result);
            }
        }

        @Override
        public void failure(TwitterException exception) {
            if (cb != null) {
                cb.failure(exception);
            }
        }
    }

    /**
     * TweetTimelineListAdapter Builder
     */
    public static class Builder {
        private Context context;
        private Timeline<Tweet> timeline;
        private Callback<Tweet> actionCallback;
        private int styleResId = R.style.tw__TweetLightStyle;

        /**
         * Constructs a Builder.
         * @param context Context for Tweet views.
         */
        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Sets the Tweet timeline data source.
         * @param timeline Timeline of Tweets
         */
        public Builder setTimeline(Timeline<Tweet> timeline) {
            this.timeline = timeline;
            return this;
        }

        /**
         * Sets the Tweet view style by resource id.
         * @param styleResId resource id of the Tweet view style
         */
        public Builder setViewStyle(int styleResId) {
            this.styleResId = styleResId;
            return this;
        }

        /**
         * Sets the callback to call when a Tweet action is performed on a Tweet view.
         * @param actionCallback called when a Tweet action is performed.
         */
        public Builder setOnActionCallback(Callback<Tweet> actionCallback) {
            this.actionCallback = actionCallback;
            return this;
        }

        /**
         * Builds a TweetTimelineListAdapter from Builder parameters.
         * @return a TweetTimelineListAdpater
         */
        public TweetTimelineListAdapter build() {
            return new TweetTimelineListAdapter(context, timeline, styleResId, actionCallback);
        }
    }
}
