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
package com.koma.audient.model;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koma.audient.model.source.AudientDataSource;
import com.koma.audient.model.source.local.AudientDao;
import com.koma.audient.model.source.local.AudientDatabase;
import com.koma.audient.model.source.local.LocalDataSource;
import com.koma.audient.model.source.remote.RemoteDataSource;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AudientRepositoryModule {
    private static final String DB_NAME = "audient-db";

    private final String mBaseUrl;

    public AudientRepositoryModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Singleton
    @Provides
    AudientDataSource provideLocalDataSource(Context context, AudientDao audientDao,
                                             SharedPreferences sharedPreferences) {
        return new LocalDataSource(context, audientDao, sharedPreferences);
    }

    @Singleton
    @Provides
    AudientDataSource provideRemoteDataSource(AudientApi audientApi) {
        return new RemoteDataSource(audientApi);
    }

    @Singleton
    @Provides
    SharedPreferences provideSharePreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences;
    }

    @Singleton
    @Provides
    AudientDatabase provideDb(Context context) {
        return Room.databaseBuilder(context, AudientDatabase.class, DB_NAME).build();
    }

    @Singleton
    @Provides
    AudientDao provideAudientDao(AudientDatabase db) {
        return db.audientDao();
    }

    @Singleton
    @Provides
    Cache provideCache(Context context) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(context.getCacheDir(), cacheSize);
        return cache;
    }

    @Singleton
    @Provides
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
        return gsonBuilder.create();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(Gson gson, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    AudientApi provideAudientApi(Retrofit retrofit) {
        return retrofit.create(AudientApi.class);
    }
}
