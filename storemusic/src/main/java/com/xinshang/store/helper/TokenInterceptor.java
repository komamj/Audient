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
package com.xinshang.store.helper;

import android.content.SharedPreferences;

import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by koma_20 on 2018/3/4.
 */

public class TokenInterceptor implements Interceptor {
    private static final String TAG = TokenInterceptor.class.getSimpleName();

    private SharedPreferences mSharedPreference;

    public TokenInterceptor(SharedPreferences sharedPreferences) {
        mSharedPreference = sharedPreferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String accessToken = mSharedPreference.getString(Constants.ACCESS_TOKEN, "");
        LogUtils.i(TAG, "intercept : accessToken :" + accessToken);
        Request request = chain.request().newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
        return chain.proceed(request);
    }
}
