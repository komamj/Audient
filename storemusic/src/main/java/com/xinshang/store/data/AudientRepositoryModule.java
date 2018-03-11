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
package com.xinshang.store.data;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xinshang.store.BuildConfig;
import com.xinshang.store.data.entities.Token;
import com.xinshang.store.data.source.AudientDataSource;
import com.xinshang.store.data.source.local.AudientDao;
import com.xinshang.store.data.source.local.AudientDatabase;
import com.xinshang.store.data.source.local.LocalDataSource;
import com.xinshang.store.data.source.remote.RemoteDataSource;
import com.xinshang.store.helper.TokenInterceptor;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AudientRepositoryModule {
    private static final String TAG = AudientRepositoryModule.class.getSimpleName();

    private static final String DB_NAME = "store-db";

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
                .setDateFormat("yyyy-MM-dd HH:mm:ss");
        return gsonBuilder.create();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient(Cache cache, final SharedPreferences sharedPreferences,
                                     final Gson gson) {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        return new OkHttpClient.Builder()
                .authenticator(new Authenticator() {
                    @Nullable
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        String refershToken = sharedPreferences.getString(Constants.REFRESH_TOKEN, "");
                        LogUtils.i(TAG, "authenticate :" + refershToken);

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Constants.STORE_MUSIC_HOST)
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .build();
                        AudientApi audientApi = retrofit.create(AudientApi.class);

                        audientApi.getAccessToken(Constants.USER_NAME, Constants.USER_PASSWORD,
                                Constants.GRANT_TYPE, Constants.CLIENT_ID,
                                Constants.CLIENT_SECRET)
                                .subscribeWith(new DisposableSubscriber<Token>() {
                                    @Override
                                    public void onNext(Token token) {
                                        sharedPreferences.edit()
                                                .putString(Constants.ACCESS_TOKEN, token.accessToken)
                                                .commit();
                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                        return response.request().newBuilder()
                                .header("Authorization", "Bearer " + sharedPreferences.getString(Constants.ACCESS_TOKEN, ""))
                                .build();
                    }
                })
                .addInterceptor(new TokenInterceptor(sharedPreferences))
                .addInterceptor(logInterceptor)
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
