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

import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.models.Tweet;
import com.sanskrit.pmo.twitter.tweetui.*;
import com.sanskrit.pmo.twitter.tweetui.Timeline;
import com.sanskrit.pmo.twitter.tweetui.TimelineCursor;
import com.sanskrit.pmo.twitter.tweetui.TimelineResult;
import com.sanskrit.pmo.twitter.tweetui.TweetUi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FixedTweetTimeline extends BaseTimeline implements Timeline<Tweet> {
    private static final String SCRIBE_SECTION = "fixed";
    List<Tweet> tweets;

    FixedTweetTimeline(TweetUi tweetUi, List<Tweet> tweets) {
        super(tweetUi);
        this.tweets = tweets == null ? new ArrayList<Tweet>() : tweets;
    }

    @Override
    public void next(Long minPosition, Callback<TimelineResult<Tweet>> cb) {
        // always return the same fixed set of 'latest' Tweets
        final TimelineResult<Tweet> timelineResult
                = new TimelineResult<>(new TimelineCursor(tweets), tweets);
        cb.success(timelineResult, null);
    }

    @Override
    public void previous(Long maxPosition, Callback<TimelineResult<Tweet>> cb) {
        final List<Tweet> empty = Collections.emptyList();
        final TimelineResult<Tweet> timelineResult = new TimelineResult<>(new TimelineCursor(empty),
                empty);
        cb.success(timelineResult, null);
    }

    @Override
    String getTimelineType() {
        return SCRIBE_SECTION;
    }

    /**
     * FixedTweetTimeline Builder.
     */
    public static class Builder {
        private final TweetUi tweetUi;
        private List<Tweet> tweets;

        /**
         * Constructs a Builder.
         */
        public Builder() {
            this(TweetUi.getInstance());
        }

        /**
         * Constructs a Builder.
         * @param tweetUi A TweetUi instance.
         */
        public Builder(TweetUi tweetUi) {
            if (tweetUi == null) {
                throw new IllegalArgumentException("TweetUi instance must not be null");
            }
            this.tweetUi = tweetUi;
        }

        /**
         * Sets the Tweets to be returned by the timeline.
         * @param tweets fixed set of Tweets provided by the timeline.
         */
        public Builder setTweets(List<Tweet> tweets) {
            this.tweets = tweets;
            return this;
        }

        /**
         * Builds a FixedTweetTimeline from the Builder parameters.
         * @return a FixedTweetTimeline.
         */
        public FixedTweetTimeline build() {
            return new FixedTweetTimeline(tweetUi, tweets);
        }
    }
}
