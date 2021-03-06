/*
 * Copyright 2017 Koma
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
 */
package com.xinshang.store.utils;

import android.util.Log;

import com.xinshang.store.BuildConfig;

public class LogUtils {
    private static final boolean IS_DEBUG = BuildConfig.LOG_DEBUG;
    private static final String TAG = "KomaLog";

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(TAG, buildString(tag, msg));
        }
    }

    public static void i(String tag, String msg) {
        if (IS_DEBUG) {
            Log.i(TAG, buildString(tag, msg));
        }
    }

    public static void e(String tag, String msg) {
        if (IS_DEBUG) {
            Log.e(TAG, buildString(tag, msg));
        }
    }

    private static String buildString(String tag, String msg) {
        StringBuilder sb = new StringBuilder(tag);
        sb.append("----");
        sb.append(msg);
        return sb.toString();
    }
}
