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
package com.koma.audient.model.source.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.koma.audient.model.entities.Audient;
import com.koma.audient.model.entities.BaseResponse;
import com.koma.audient.model.entities.Comment;
import com.koma.audient.model.entities.CommentResult;
import com.koma.audient.model.entities.FileResult;
import com.koma.audient.model.entities.LyricResult;
import com.koma.audient.model.entities.NowPlayingResult;
import com.koma.audient.model.entities.SearchResult;
import com.koma.audient.model.entities.SongDetailResult;
import com.koma.audient.model.entities.Token;
import com.koma.audient.model.entities.ToplistDetailResult;
import com.koma.audient.model.entities.ToplistResult;
import com.koma.audient.model.entities.User;
import com.koma.audient.model.source.AudientDataSource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

@Singleton
public class LocalDataSource implements AudientDataSource {
    private static final String TAG = LocalDataSource.class.getSimpleName();

    private static final String LOGIN_TAG = "is_login";

    private final Context mContext;

    private final SharedPreferences mSharedPreferences;

    private final AudientDao mAudientDao;

    @Inject
    public LocalDataSource(Context context, AudientDao audientDao,
                           SharedPreferences sharedPreferences) {
        mContext = context;

        mAudientDao = audientDao;

        mSharedPreferences = sharedPreferences;
    }

    @Override
    public Flowable<List<Audient>> getAudientTests() {
        return Flowable.create(new FlowableOnSubscribe<List<Audient>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Audient>> emitter) throws Exception {
                List<Audient> audients = new ArrayList<>();
                Audient audient1 = new Audient();
                audient1.mediaId = "001yS0N33yPm1B";
                audient1.duration = 324;
                audient1.artistId = "002pUZT93gF4Cu";
                audient1.artistName = "BEYOND";
                audient1.mediaName = "海阔天空";
                audient1.albumId = "002qcJuX3lO3EZ";
                audient1.albumName = "乐与怒";
                audients.add(audient1);
                Audient audient2 = new Audient();
                audient2.mediaId = "002pKRoX4Qbafa";
                audient2.duration = 298;
                audient2.artistId = "002pUZT93gF4Cu";
                audient2.artistName = "BEYOND";
                audient2.mediaName = "光辉岁月";
                audient2.albumId = "001C2BRX15iE4B";
                audient2.albumName = "命运派对";
                audients.add(audient2);
                Audient audient3 = new Audient();
                audient3.mediaId = "000DIFHv0PjOHH";
                audient3.duration = 250;
                audient3.artistId = "002pUZT93gF4Cu";
                audient3.artistName = "BEYOND";
                audient3.mediaName = "不再犹豫";
                audient3.albumId = "000DWELJ4Y7U3P";
                audient3.albumName = "黄家驹原作精选集";
                audients.add(audient3);
                Audient audient4 = new Audient();
                audient4.mediaId = "004Gyue33FERTT";
                audient4.duration = 275;
                audient4.artistId = "002pUZT93gF4Cu";
                audient4.artistName = "BEYOND";
                audient4.mediaName = "真的爱你";
                audient4.albumId = "000eOgmK1fN8Cs";
                audient4.albumName = "BEYOND IV";
                audients.add(audient4);
                Audient audient5 = new Audient();
                audient1.mediaId = "00247u9f23fivj";
                audient5.duration = 251;
                audient5.artistId = "002pUZT93gF4Cu";
                audient5.artistName = "BEYOND";
                audient5.mediaName = "谁伴我闯荡";
                audient5.albumId = "000DWELJ4Y7U3P";
                audient5.albumName = "黄家驹原作精选集";
                audients.add(audient5);
                Audient audient6 = new Audient();
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
    public Flowable<List<ToplistResult>> getTopList() {
        return null;
    }

    @Override
    public Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime) {
        return null;
    }

    @Override
    public Flowable<SearchResult> getSearchReult(String keyword) {
        return null;
    }

    @Override
    public Flowable<LyricResult> getLyricResult(String id) {
        return null;
    }

    @Override
    public Flowable<SongDetailResult> getSongDetailResult(String id) {
        return null;
    }

    @Override
    public Flowable<FileResult> getFileResult(String id) {
        return null;
    }

    @Override
    public Flowable<CommentResult> getCommentResult(String id) {
        return Flowable.create(new FlowableOnSubscribe<CommentResult>() {
            @Override
            public void subscribe(FlowableEmitter<CommentResult> emitter) throws Exception {
                CommentResult commentResult = new CommentResult();
                List<Comment> comments = new ArrayList<>();
                Comment comment1 = new Comment();
                comment1.time = "2018-01-30 20:10";
                comment1.userName = "流氓";
                comment1.message = "真几把难听.";
                comments.add(comment1);

                Comment comment2 = new Comment();
                comment2.time = "2018-01-31 00:10";
                comment2.userName = "Koma";
                comment2.message = "这首歌旋律感觉还可以.";
                comments.add(comment2);

                commentResult.comments = comments;
                emitter.onNext(commentResult);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<NowPlayingResult> getNowPlayingResult() {
        return Flowable.create(new FlowableOnSubscribe<NowPlayingResult>() {
            @Override
            public void subscribe(FlowableEmitter<NowPlayingResult> emitter) throws Exception {
                NowPlayingResult nowPlayingResult = new NowPlayingResult();
                Audient audient = new Audient();
                audient.mediaId = "003evjhg3qIe9S";
                audient.duration = 260;
                audient.artistId = "0040D7gK4aI54k";
                audient.artistName = "谭咏麟";
                audient.mediaName = "一生中最爱";
                audient.albumId = "0018tEZm032RCk";
                audient.albumName = "神话1991";
                nowPlayingResult.audient = audient;

                emitter.onNext(nowPlayingResult);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<Boolean> getLoginStatus() {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                emitter.onNext(mSharedPreferences.getBoolean(LOGIN_TAG, false));

                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<BaseResponse> getLoginResult(User user) {
        return null;
    }

    @Override
    public Flowable<Boolean> setLoginStatus(final boolean loginStatus) {
        return Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(FlowableEmitter<Boolean> emitter) throws Exception {
                mSharedPreferences.edit()
                        .putBoolean(LOGIN_TAG, loginStatus)
                        .apply();

                emitter.onNext(loginStatus);

                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<BaseResponse> getFavoriteResult(String name) {
        return null;
    }

    @Override
    public Flowable<Token> getToken(String userName, String password) {
        return null;
    }
}
