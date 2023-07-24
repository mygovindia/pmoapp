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

package com.sanskrit.pmo.twitter.core.internal.scribe;

import com.sanskrit.pmo.twitter.core.Session;
import com.sanskrit.pmo.twitter.core.SessionManager;
import com.sanskrit.pmo.twitter.core.TwitterCore;

import java.util.List;

import io.fabric.sdk.android.services.common.IdManager;

public class TwitterCoreScribeClientHolder {

    private static final String KIT_NAME = "TwitterCore";

    private static DefaultScribeClient instance;

    /**
     * @return instance can be null
     */
    public static DefaultScribeClient getScribeClient() {
        return instance;
    }

    /**
     * Must be called on background thread
     */
    public static void initialize(TwitterCore kit,
                                  List<SessionManager<? extends Session>> sessionManagers, IdManager idManager) {
        instance = new DefaultScribeClient(kit, KIT_NAME, sessionManagers, idManager);
    }
}
