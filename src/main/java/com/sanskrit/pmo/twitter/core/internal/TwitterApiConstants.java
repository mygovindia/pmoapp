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


public class TwitterApiConstants {
    public static final int MAX_TWEET_CHARS = 140;

    public static class Base {
        public static final String PARAM_ID = "id";
        public static final String FIELD_ID = "id";
    }

    public static class Errors extends Base {
        public static final String ERRORS = "errors";

        // error when app auth token not recognized (such as when expired)
        public static final int APP_AUTH_ERROR_CODE = 89;
        // error when a tweet has already been favorited
        public static final int ALREADY_FAVORITED = 139;
        // error when a tweet has already been unfavorited
        public static final int ALREADY_UNFAVORITED = 144;
        // error when guest auth token not recognized (such as when expired)
        public static final int GUEST_AUTH_ERROR_CODE = 239;

        // legacy errors are errors that are returned by the api in a different format, where there
        // is no array of errors.
        public static final int LEGACY_ERROR = 0;
    }
}
