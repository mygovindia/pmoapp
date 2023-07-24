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

package com.sanskrit.pmo.twitter.tweetui.internal;

import com.sanskrit.pmo.twitter.core.AppSession;
import com.sanskrit.pmo.twitter.core.AuthToken;
import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.Result;
import com.sanskrit.pmo.twitter.core.Session;
import com.sanskrit.pmo.twitter.core.SessionManager;
import com.sanskrit.pmo.twitter.core.TwitterAuthToken;
import com.sanskrit.pmo.twitter.core.internal.SessionProvider;
import com.sanskrit.pmo.twitter.core.TwitterCore;
import com.sanskrit.pmo.twitter.core.TwitterException;
import com.sanskrit.pmo.twitter.core.internal.oauth.GuestAuthToken;

import java.util.List;

/**
 * Mockable class that simplifies code that depends on sessions but is agnostic to session type.
 *
 * Note, the order of the SessionManager list is important if one type is more desirable to use
 * than another.
 */
public class GuestSessionProvider extends SessionProvider {
    private final TwitterCore twitterCore;

    public GuestSessionProvider(TwitterCore twitterCore,
            List<SessionManager<? extends Session>> sessionManagers) {
        super(sessionManagers);
        this.twitterCore = twitterCore;
    }

    @Override
    public Session getActiveSession() {
        final Session session = super.getActiveSession();
        if (session == null) {
            return null;
        }
        final AuthToken token = session.getAuthToken();
        // allow only user auth and guest auth tokens, not old app auth tokens
        if (token instanceof TwitterAuthToken || token instanceof GuestAuthToken) {
            return session;
        }
        return null;
    }

    /*
     * Requests a guest auth session.
     */
    public void requestAuth(Callback<Session> cb) {
        twitterCore.logInGuest(new AppSessionCallback(cb));
    }

    /*
     * Wrapper callback which converts the AppSession to a general Session.
     */
    class AppSessionCallback extends Callback<AppSession> {
        private final Callback<Session> cb;

        /*
         * Constructs an AppSessionCallback
         * @param cb A callback which expects a Session.
         */
        AppSessionCallback(Callback<Session> cb) {
            this.cb = cb;
        }

        @Override
        public void success(Result<AppSession> result) {
            cb.success(new Result<Session>(result.data, result.response));
        }

        @Override
        public void failure(TwitterException exception) {
            cb.failure(exception);
        }
    }
}
