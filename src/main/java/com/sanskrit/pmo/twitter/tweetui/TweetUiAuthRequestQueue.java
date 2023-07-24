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

import com.sanskrit.pmo.twitter.core.internal.AuthRequestQueue;
import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.Result;
import com.sanskrit.pmo.twitter.core.Session;
import com.sanskrit.pmo.twitter.core.internal.SessionProvider;
import com.sanskrit.pmo.twitter.core.TwitterApiClient;
import com.sanskrit.pmo.twitter.core.TwitterCore;
import com.sanskrit.pmo.twitter.core.TwitterException;

/*
 * Queues requests until a TwitterApiClient with a session is ready. Gets an active session from
 * the sessionProvider or requests sessionProvider perform authentication.
 */
class TweetUiAuthRequestQueue extends AuthRequestQueue {
    private final TwitterCore twitterCore;

    TweetUiAuthRequestQueue(TwitterCore twitterCore, SessionProvider sessionProvider) {
        super(sessionProvider);
        this.twitterCore = twitterCore;
    }

    protected synchronized boolean addClientRequest(final Callback<TwitterApiClient> callback) {
        return addRequest(new Callback<Session>() {
            @Override
            public void success(Result<Session> result) {
                callback.success(new Result<>(twitterCore.getApiClient(result.data), null));
            }

            @Override
            public void failure(TwitterException exception) {
                callback.failure(exception);
            }
        });
    }
}
