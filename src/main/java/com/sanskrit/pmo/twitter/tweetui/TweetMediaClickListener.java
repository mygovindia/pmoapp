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

import com.sanskrit.pmo.twitter.core.models.MediaEntity;
import com.sanskrit.pmo.twitter.core.models.Tweet;

/**
 * Interface to be invoked when media is clicked.
 */
public interface TweetMediaClickListener {
    /**
     * Called when media clicked.
     * @param tweet The Tweet that was clicked.
     * @param entity The entity that was clicked.
     */
    void onMediaEntityClick(Tweet tweet, MediaEntity entity);
}
