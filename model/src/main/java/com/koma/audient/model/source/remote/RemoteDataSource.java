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
import com.koma.audient.model.entities.Album;
import com.koma.audient.model.entities.AlbumResult;
import com.koma.audient.model.entities.Lyric;
import com.koma.audient.model.entities.LyricResult;
import com.koma.audient.model.entities.Music;
import com.koma.audient.model.entities.SearchResult;
import com.koma.audient.model.entities.TopList;
import com.koma.audient.model.entities.TopSong;
import com.koma.audient.model.source.AudientDataSource;
import com.koma.common.util.Constants;

import java.text.DateFormat;
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
    public Flowable<List<TopList.Billboard>> getTopLists() {
        return mAudientApi.getTopLists(Constants.APP_ID, Constants.ACCESS_TOKEN, getTimeStamp())
                .map(new Function<TopList, List<TopList.Billboard>>() {
                    @Override
                    public List<TopList.Billboard> apply(TopList topList) throws Exception {
                        return topList.billboardListResponse.billboardList.billboards;
                    }
                });
    }

    @Override
    public Flowable<List<Music>> getTopSongs(@NonNull String billboardId, int count, int page) {
        return mAudientApi.getTopSongs(billboardId, count, page, Constants.APP_ID,
                Constants.ACCESS_TOKEN, getTimeStamp())
                .map(new Function<TopSong, List<Music>>() {
                    @Override
                    public List<Music> apply(TopSong topSong) throws Exception {
                        return null;
                    }
                });
    }

    private String getTimeStamp() {
        DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
        String timeStamp = dateFormat.format(new Date());

        return timeStamp;
    }

    @Override
    public Flowable<List<Music>> getSearchReults(String keyword, String musicType, int count,
                                                 int page) {
        return mAudientApi.getMusics(keyword, musicType, count, page, Constants.APP_ID,
                Constants.ACCESS_TOKEN, getTimeStamp())
                .map(new Function<SearchResult, List<Music>>() {
                    @Override
                    public List<Music> apply(SearchResult searchResult) throws Exception {
                        return searchResult.searchSongDataResponse.musics;
                    }
                });
    }

    @Override
    public Flowable<Lyric> getLyric(String id, String idType, String musicName, String actorName, String type) {
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
    public Flowable<Album> getAlbum(String id, String idType, String format, String singer, String song) {
        return mAudientApi.getAlbum(id, idType, format, singer, song, Constants.APP_ID,
                Constants.ACCESS_TOKEN, getTimeStamp())
                .map(new Function<AlbumResult, Album>() {
                    @Override
                    public Album apply(AlbumResult albumResult) throws Exception {
                        return albumResult.picResponse.album;
                    }
                });
    }
}
