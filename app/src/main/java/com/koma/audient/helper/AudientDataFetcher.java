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
package com.koma.audient.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.Preconditions;
import com.koma.audient.model.AudientApi;
import com.koma.audient.model.entities.Audient;
import com.koma.common.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AudientDataFetcher implements DataFetcher<InputStream>, okhttp3.Callback {
    private static final String TAG = AudientDataFetcher.class.getSimpleName();

    private final Call.Factory client;
    private final AudientApi mAudientApi;
    private InputStream stream;
    private ResponseBody responseBody;
    private DataCallback<? super InputStream> callback;
    // call may be accessed on the main thread while the object is in use on other threads. All other
    // accesses to variables may occur on different threads, but only one at a time.
    private volatile Call call;

    private final Audient audient;

    public AudientDataFetcher(Call.Factory client, AudientApi audientApi, Audient audient) {
        this.client = client;

        this.mAudientApi = audientApi;

        this.audient = audient;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        LogUtils.i(TAG, "loadData :" + audient.id);

        GlideUrl glideUrl = new GlideUrl("");

        Request.Builder requestBuilder = new Request.Builder().url(glideUrl.toStringUrl());
        for (Map.Entry<String, String> headerEntry : glideUrl.getHeaders().entrySet()) {
            String key = headerEntry.getKey();
            requestBuilder.addHeader(key, headerEntry.getValue());
        }
        Request request = requestBuilder.build();
        this.callback = callback;

        call = client.newCall(request);
        call.enqueue(this);
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "OkHttp failed to obtain result", e);
        }

        callback.onLoadFailed(e);
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        responseBody = response.body();
        if (response.isSuccessful()) {
            long contentLength = Preconditions.checkNotNull(responseBody).contentLength();
            stream = ContentLengthInputStream.obtain(responseBody.byteStream(), contentLength);
            callback.onDataReady(stream);
        } else {
            callback.onLoadFailed(new HttpException(response.message(), response.code()));
        }
    }

    @Override
    public void cleanup() {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // Ignored
        }
        if (responseBody != null) {
            responseBody.close();
        }
        callback = null;
    }

    @Override
    public void cancel() {
        Call local = call;
        if (local != null) {
            local.cancel();
        }
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
