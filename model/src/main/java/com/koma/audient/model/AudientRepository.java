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

import android.support.annotation.NonNull;

import com.koma.audient.model.entities.Album;
import com.koma.audient.model.entities.AudientTest;
import com.koma.audient.model.entities.Comment;
import com.koma.audient.model.entities.Lyric;
import com.koma.audient.model.entities.MusicFileItem;
import com.koma.audient.model.entities.SearchResult;
import com.koma.audient.model.entities.TopListResult;
import com.koma.audient.model.source.AudientDataSource;
import com.koma.audient.model.source.local.LocalDataSource;
import com.koma.audient.model.source.remote.RemoteDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class AudientRepository implements AudientDataSource {
    private final LocalDataSource mLocalDataSource;

    private final RemoteDataSource mRemoteDataSource;

    @Inject
    public AudientRepository(@Local LocalDataSource localDataSource,
                             @Remote RemoteDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;

        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Flowable<List<AudientTest>> getAudientTests() {
        return mLocalDataSource.getAudientTests();
    }

    @Override
    public Flowable<List<TopListResult>> getTopLists() {
        return mRemoteDataSource.getTopLists();
    }

    @Override
    public Flowable<List<MusicFileItem>> getTopSongs(@NonNull String billboardId, int count, int page) {
        return mRemoteDataSource.getTopSongs(billboardId, count, page);
    }

    @Override
    public Flowable<SearchResult> getSearchReults(String keyword) {
        return mRemoteDataSource.getSearchReults(keyword);
    }

    @Override
    public Flowable<Lyric> getLyric(String id, String idType, String musicName, String actorName,
                                    String type) {
        return mRemoteDataSource.getLyric(id, idType, musicName, actorName, type);
    }

    @Override
    public Flowable<Album> getAlbum(MusicFileItem musicFileItem) {
        return mRemoteDataSource.getAlbum(musicFileItem);
    }

    @Override
    public Flowable<List<Comment>> getComments(long id) {
        return null;
    }
}
