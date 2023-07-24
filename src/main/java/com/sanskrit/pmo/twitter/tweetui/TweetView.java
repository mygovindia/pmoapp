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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.sanskrit.pmo.twitter.core.models.Tweet;
import com.sanskrit.pmo.twitter.tweetui.BaseTweetView;

public class TweetView extends BaseTweetView {
    private static final String VIEW_TYPE_NAME = "default";

    public TweetView(Context context, Tweet tweet) {
        super(context, tweet);
    }

    public TweetView(Context context, Tweet tweet, int styleResId) {
        super(context, tweet, styleResId);
    }

    TweetView(Context context, Tweet tweet, int styleResId, DependencyProvider dependencyProvider) {
        super(context, tweet, styleResId, dependencyProvider);
    }

    public TweetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public TweetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected int getLayout() {
        return com.sanskrit.pmo.R.layout.tw__tweet;
    }

    /**
     * Render the Tweet by updating the subviews. For any data that is missing from the Tweet,
     * invalidate the subview value (e.g. text views set to empty string) for view recycling.
     * Do not call with render true until inflation has completed.
     * @throws IllegalArgumentException
     */
    @Override
    void render() {
        super.render();
        setVerifiedCheck(tweet);
    }

    /**
     * Sets the verified check if the User is verified. If the User is not verified or if the
     * verification data is unavailable, sets the check visibility to gone.
     */
    private void setVerifiedCheck(Tweet tweet) {
        if (tweet != null && tweet.user != null && tweet.user.verified) {
            verifiedCheckView.setVisibility(ImageView.VISIBLE);
        } else {
            verifiedCheckView.setVisibility(ImageView.GONE);
        }
    }

    @Override
    String getViewTypeName() {
        return VIEW_TYPE_NAME;
    }
}
