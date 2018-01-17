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
import android.support.annotation.NonNull;

import com.koma.audient.model.entities.Album;
import com.koma.audient.model.entities.Audient;
import com.koma.audient.model.entities.Lyric;
import com.koma.audient.model.entities.MusicFileItem;
import com.koma.audient.model.entities.TopList;
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
    public Flowable<List<Audient>> getAudients() {
        return Flowable.create(new FlowableOnSubscribe<List<Audient>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Audient>> emitter) throws Exception {
                List<Audient> audients = new ArrayList<>();
                Audient audient1 = new Audient();
                audient1.actorName = "周华健";
                audient1.musicName = "海阔天空";
                audient1.albumUrl = "http://4galbum.ctmus.cn/scale/album/a/89/big_ma_3a89_100001059323.jpg?param=200y200";
                audients.add(audient1);
                Audient audient2 = new Audient();
                audient2.actorName = "beyond";
                audient2.musicName = "光辉岁月";
                audient2.albumUrl = "http://4galbum.ctmus.cn/scale/album/f/38/big_ma_3f38_100001026441.jpg?param=200y200";
                audients.add(audient2);
                Audient audient3 = new Audient();
                audient3.actorName = "谢霆锋";
                audient3.musicName = "不可一世(Live 2000年12月版)";
                audient3.albumUrl = "http://4galbum.ctmus.cn/scale/singer/0/78/big_ms_f078_100034494495.jpg?param=200y200";
                audients.add(audient3);
                Audient audient4 = new Audient();
                audient4.actorName = "邓紫棋";
                audient4.musicName = "喜欢你";
                audient4.albumUrl = "http://4galbum.ctmus.cn/scale/singer/e/02/7220fc92-790d-4c93-9813-ac1c5e93584d.jpg?param=200y200";
                audients.add(audient4);
                Audient audient5 = new Audient();
                audient5.actorName = "beyond";
                audient5.musicName = "再见理想(live)";
                audient5.albumUrl = "http://4galbum.ctmus.cn/scale/singer/1/d8/be43f7f4-b570-4a6e-a5af-a564c8a5ab5a.jpg?param=200y200";
                audients.add(audient5);
                Audient audient6 = new Audient();
                audient6.actorName = "谭咏麟";
                audient6.musicName = "一生中最爱";
                audient6.albumUrl = "http://4galbum.ctmus.cn/scale/album/7/23/big_ma_5723_100001033202.jpg?param=200y200";
                audients.add(audient6);

                emitter.onNext(audients);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<List<TopList.BillboardListResponse.Billboard>> getTopLists() {
        return null;
    }

    @Override
    public Flowable<List<MusicFileItem>> getTopSongs(@NonNull String billboardId, int count, int page) {
        return null;
    }

    @Override
    public Flowable<List<MusicFileItem>> getSearchReults(String keyword, String musicType, int count, int page) {
        return null;
    }

    @Override
    public Flowable<Lyric> getLyric(String id, String idType, String musicName, String actorName, String type) {
        return null;
    }

    @Override
    public Flowable<Album> getAlbum(MusicFileItem musicFileItem) {
        return null;
    }
}
