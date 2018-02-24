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
package com.xinshang.audient.helper;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import com.xinshang.audient.model.AudientApi;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.common.util.LogUtils;

import java.io.InputStream;

import okhttp3.Call;

public class AudientModelLoader implements ModelLoader<Audient, InputStream> {
    private static final String TAG = AudientModelLoader.class.getSimpleName();

    private final Call.Factory mClient;

    private final AudientApi mAudientApi;

    public AudientModelLoader(@NonNull Call.Factory client, AudientApi audientApi) {
        this.mClient = client;

        this.mAudientApi = audientApi;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(@NonNull Audient audient, int width, int height,
                                               @NonNull Options options) {
        LogUtils.i(TAG, "loadData :" + audient.mediaId + "width :" + width + ",height:" + height);

        return new LoadData<>(new ObjectKey(audient),
                new AudientDataFetcher(mClient, mAudientApi, audient));
    }

    @Override
    public boolean handles(@NonNull Audient audient) {
        return false;
    }
}
