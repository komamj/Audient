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
package com.koma.audient.model.source.remote;

import android.support.annotation.NonNull;

import com.koma.audient.model.AudientApi;
import com.koma.audient.model.entities.AudientTest;
import com.koma.audient.model.entities.Comment;
import com.koma.audient.model.entities.Lyric;
import com.koma.audient.model.entities.LyricResult;
import com.koma.audient.model.entities.MusicFileItem;
import com.koma.audient.model.entities.SearchResult;
import com.koma.audient.model.entities.SongDetailResult;
import com.koma.audient.model.entities.TopListResult;
import com.koma.audient.model.entities.TopSong;
import com.koma.audient.model.entities.ToplistDetailResult;
import com.koma.audient.model.source.AudientDataSource;
import com.koma.common.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

@Singleton
public class RemoteDataSource implements AudientDataSource {
    private static final String TAG = RemoteDataSource.class.getSimpleName();

    private final AudientApi mAudientApi;

    @Inject
    public RemoteDataSource(AudientApi audientApi) {
        mAudientApi = audientApi;
    }

    @Override
    public Flowable<List<AudientTest>> getAudientTests() {
        return null;
    }

    @Override
    public Flowable<List<TopListResult>> getTopLists() {
        return mAudientApi.getTopLists();
    }

    @Override
    public Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime) {
        return mAudientApi.getToplistDetail(topId, showTime);
    }

    @Override
    public Flowable<List<MusicFileItem>> getTopSongs(@NonNull String billboardId, int count, int page) {
        return mAudientApi.getTopSongs(billboardId, count, page, Constants.APP_ID,
                Constants.ACCESS_TOKEN, getTimeStamp())
                .map(new Function<TopSong, List<MusicFileItem>>() {
                    @Override
                    public List<MusicFileItem> apply(TopSong topSong) throws Exception {
                        return topSong.queryContentBillboardResponse.musicItemList.musics;
                    }
                });
    }

    private String getTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = simpleDateFormat.format(new Date());
        return timeStamp;
    }

    @Override
    public Flowable<SearchResult> getSearchReults(String keyword) {
        return mAudientApi.getSeachResults(keyword);
    }

    @Override
    public Flowable<Lyric> getLyric(String id, String idType, String musicName,
                                    String actorName, String type) {
        return mAudientApi.getLyric(id, idType, musicName, actorName, type, Constants.APP_ID,
                Constants.ACCESS_TOKEN, getTimeStamp())
                .map(new Function<LyricResult, Lyric>() {
                    @Override
                    public Lyric apply(LyricResult lyricResult) throws Exception {
                        return lyricResult.queryLyricResponse.lyric;
                    }
                });
    }

    @Override
    public Flowable<SongDetailResult> getSongDetailResult(String id) {
        return mAudientApi.getSongDetailResult(id);
    }

    @Override
    public Flowable<List<Comment>> getComments(long id) {
        return null;
    }
}
