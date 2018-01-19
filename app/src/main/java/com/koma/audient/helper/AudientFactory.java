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

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.google.gson.GsonBuilder;
import com.koma.audient.model.AudientApi;
import com.koma.audient.model.BuildConfig;
import com.koma.audient.model.entities.Audient;
import com.koma.common.util.Constants;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AudientFactory implements ModelLoaderFactory<Audient, InputStream> {
    private static volatile OkHttpClient mInternalClient;

    private static volatile AudientApi mAudientApi;

    /**
     * Constructor for a new Factory that runs requests using a static singleton mClient.
     */
    public AudientFactory() {
        if (mInternalClient == null) {
            synchronized (AudientFactory.class) {
                if (mInternalClient == null) {
                    HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                    if (BuildConfig.DEBUG) {
                        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    } else {
                        logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
                    }

                    mInternalClient = new OkHttpClient.Builder()
                            .addInterceptor(logInterceptor)
                            .build();

                    mAudientApi = new Retrofit.Builder()
                            .baseUrl(Constants.AUDIENT_HOST)
                            .client(mInternalClient)
                            .addConverterFactory(GsonConverterFactory.create(
                                    new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                                            .create()))
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build().create(AudientApi.class);
                }
            }
        }
    }

    @NonNull
    @Override
    public ModelLoader<Audient, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
        return new AudientModelLoader(mInternalClient, mAudientApi);
    }

    @Override
    public void teardown() {

    }
}
