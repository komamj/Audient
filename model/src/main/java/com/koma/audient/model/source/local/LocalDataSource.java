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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

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
