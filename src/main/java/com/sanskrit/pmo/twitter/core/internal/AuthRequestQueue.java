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

package com.sanskrit.pmo.twitter.core.internal;

import com.sanskrit.pmo.twitter.core.Callback;
import com.sanskrit.pmo.twitter.core.Result;
import com.sanskrit.pmo.twitter.core.Session;
import com.sanskrit.pmo.twitter.core.TwitterException;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * Queues requests until a session is ready. Gets an active session from the sessionProvider or
 * requests sessionProvider perform authentication.
 *
 * In order to solve concurrent access problems we have put synchronized around the public methods
 * in order to lock queue access so that we can avoid orphaning requests in the queue
 */
public class AuthRequestQueue {
    final Queue<Callback<Session>> queue;
    // We use this to flag to mark that a session is either being restored from file or
    // requested from the server
    final AtomicBoolean awaitingSession;

    private final SessionProvider sessionProvider;

    public AuthRequestQueue(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
        queue = new ConcurrentLinkedQueue<>();
        awaitingSession = new AtomicBoolean(true);
    }

    /*
     * addRequest has 3 different branches
     * 1: if we are not waiting for a session (from file restoration or from network request)
     *    and there is an active session and an authConfig it will simply pass the request
     *    off to the network layer
     * 2: if we have already kicked off a request to get a session or otherwise just
     *    don't have an auth config in the form of an oauth2service (provided once TweetUi
     *    has been given a client id and secret we will queue the request
     * 3: otherwise we queue the request and start a request to the twitter api to get a session.
     *    We set the request flag to be active so that we don't end up kicking off duplicate
     *    requests.
     */
    public synchronized boolean addRequest(Callback<Session> callback) {
        if (callback == null) return false;

        // awaitingSession will be true until session restoration completes in the background.
        if (!awaitingSession.get()) {
            final Session session = getValidSession();
            if (session != null) {
                callback.success(new Result<>(session, null));
            } else {
                queue.add(callback);
                awaitingSession.set(true);
                requestAuth();
            }
        } else {
            queue.add(callback);
        }
        return true;
    }

    /*
     * We have 3 different outcomes:
     * 1. Valid session is restored, we need to flush requests
     * 2. No valid session and, there are pending requests, we need to initiate AuthRequest
     * 3. No valid session and nothing awaiting in the queue. We only need to remove the flag,
     * first request will trigger AuthRequest.
     */
    public synchronized void sessionRestored(Session session) {
        if (session != null) {
            flushQueueOnSuccess(session);
        } else if (queue.size() > 0) {
            requestAuth();
        } else {
            // We can not find any session on the disk, future requests for Session
            awaitingSession.set(false);
        }
    }

    void requestAuth() {
        sessionProvider.requestAuth(new Callback<Session>() {
            @Override
            public void success(Result<Session> result) {
                flushQueueOnSuccess(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                flushQueueOnError(exception);
            }
        });
    }

    /*
     * Clears the request queue using the given session.
     */
    synchronized void flushQueueOnSuccess(Session session) {
        awaitingSession.set(false);

        while (!queue.isEmpty()) {
            final Callback<Session> request = queue.poll();
            request.success(new Result<>(session, null));
        }
    }

    /*
     * If we weren't able to get a session we can't make these requests so we just call back to the
     * configured listener with an error.
     */
    synchronized void flushQueueOnError(TwitterException error) {
        awaitingSession.set(false);

        while (!queue.isEmpty()) {
            final Callback request = queue.poll();
            request.failure(error);
        }
    }

    // not synchronized, only package protected for testing
    Session getValidSession() {
        final Session session = sessionProvider.getActiveSession();
        // Only use session if it has auth token.
        if (session != null && session.getAuthToken() != null &&
                !session.getAuthToken().isExpired()) {
            return session;
        } else {
            return null;
        }
    }
}
