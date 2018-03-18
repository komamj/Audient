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
package com.xinshang.store.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.xinshang.store.data.entities.CommandResponse;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.data.source.AudientDataSource;
import com.xinshang.store.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

@Singleton
public class LocalDataSource implements AudientDataSource, ILocalDataSource {
    private static final String TAG = LocalDataSource.class.getSimpleName();

    private static final String LOGIN_TAG = "is_login";

    private final Context mContext;

    private final SharedPreferences mSharedPreferences;

    private final AudientDao mAudientDao;

    private final Gson mGson;

    @Inject
    public LocalDataSource(Context context, AudientDao audientDao,
                           SharedPreferences sharedPreferences, Gson gson) {
        mContext = context;

        mAudientDao = audientDao;

        mSharedPreferences = sharedPreferences;

        mGson = gson;
    }

    @Override
    public Flowable<List<TencentMusic>> getAudientTests() {
        return Flowable.create(new FlowableOnSubscribe<List<TencentMusic>>() {
            @Override
            public void subscribe(FlowableEmitter<List<TencentMusic>> emitter) throws Exception {
                List<TencentMusic> audients = new ArrayList<>();
                TencentMusic audient1 = new TencentMusic();
                audient1.mediaId = "001yS0N33yPm1B";
                audient1.duration = 324;
                audient1.artistId = "002pUZT93gF4Cu";
                audient1.artistName = "BEYOND";
                audient1.mediaName = "海阔天空";
                audient1.albumId = "002qcJuX3lO3EZ";
                audient1.albumName = "乐与怒";
                audients.add(audient1);
                TencentMusic audient2 = new TencentMusic();
                audient2.mediaId = "002pKRoX4Qbafa";
                audient2.duration = 298;
                audient2.artistId = "002pUZT93gF4Cu";
                audient2.artistName = "BEYOND";
                audient2.mediaName = "光辉岁月";
                audient2.albumId = "001C2BRX15iE4B";
                audient2.albumName = "命运派对";
                audients.add(audient2);
                TencentMusic audient3 = new TencentMusic();
                audient3.mediaId = "000DIFHv0PjOHH";
                audient3.duration = 250;
                audient3.artistId = "002pUZT93gF4Cu";
                audient3.artistName = "BEYOND";
                audient3.mediaName = "不再犹豫";
                audient3.albumId = "000DWELJ4Y7U3P";
                audient3.albumName = "黄家驹原作精选集";
                audients.add(audient3);
                TencentMusic audient4 = new TencentMusic();
                audient4.mediaId = "004Gyue33FERTT";
                audient4.duration = 275;
                audient4.artistId = "002pUZT93gF4Cu";
                audient4.artistName = "BEYOND";
                audient4.mediaName = "真的爱你";
                audient4.albumId = "000eOgmK1fN8Cs";
                audient4.albumName = "BEYOND IV";
                audients.add(audient4);
                TencentMusic audient5 = new TencentMusic();
                audient5.mediaId = "00247u9f23fivj";
                audient5.duration = 251;
                audient5.artistId = "002pUZT93gF4Cu";
                audient5.artistName = "BEYOND";
                audient5.mediaName = "谁伴我闯荡";
                audient5.albumId = "000DWELJ4Y7U3P";
                audient5.albumName = "黄家驹原作精选集";
                audients.add(audient5);
                TencentMusic audient6 = new TencentMusic();
                audient6.mediaId = "001mbYZr3QR68r";
                audient6.duration = 273;
                audient6.artistId = "002pUZT93gF4Cu";
                audient6.artistName = "BEYOND";
                audient6.mediaName = "喜欢你";
                audient6.albumId = "001oHxZZ1pAQn4";
                audient6.albumName = "秘密警察";
                audients.add(audient6);

                emitter.onNext(audients);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public void persistenceLoginStatus(boolean loginStatus) {
        mSharedPreferences.edit()
                .putBoolean(Constants.LOGIN_STATUS, loginStatus)
                .apply();
    }

    @Override
    public boolean getLoginStatus() {
        return mSharedPreferences.getBoolean(Constants.LOGIN_STATUS, false);
    }

    @Override
    public void persistenceAccessToken(String accessToken) {
        mSharedPreferences.edit()
                .putString(Constants.ACCESS_TOKEN, accessToken)
                .apply();
    }

    @Override
    public String getAccessToken() {
        return mSharedPreferences.getString(Constants.ACCESS_TOKEN, "");
    }

    @Override
    public void persistenceRefreshToken(String refreshToken) {
        mSharedPreferences.edit()
                .putString(Constants.REFRESH_TOKEN, refreshToken)
                .apply();
    }

    @Override
    public String getRefreshToken() {
        return mSharedPreferences.getString(Constants.REFRESH_TOKEN, "");
    }

    @Override
    public void persistenceStoreId(String storeId) {
        mSharedPreferences.edit()
                .putString(Constants.STORE_ID, storeId)
                .apply();
    }

    @Override
    public String getStoreId() {
        return mSharedPreferences.getString(Constants.STORE_ID, "");
    }

    @Override
    public Flowable<CommandResponse> parsingCommandResponse(final String response) {
        return Flowable.create(new FlowableOnSubscribe<CommandResponse>() {
            @Override
            public void subscribe(FlowableEmitter<CommandResponse> emitter) throws Exception {
                CommandResponse commandResponse = mGson.fromJson(response, CommandResponse.class);
                emitter.onNext(commandResponse);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }
}
