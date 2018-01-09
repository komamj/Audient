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
import android.support.annotation.WorkerThread;

import com.koma.audient.model.AudientApi;
import com.koma.audient.model.entities.Album;
import com.koma.audient.model.entities.AlbumResult;
import com.koma.audient.model.entities.Audient;
import com.koma.audient.model.entities.Lyric;
import com.koma.audient.model.entities.LyricResult;
import com.koma.audient.model.entities.MusicFileItem;
import com.koma.audient.model.entities.SearchResult;
import com.koma.audient.model.entities.TopList;
import com.koma.audient.model.entities.TopSong;
import com.koma.audient.model.source.AudientDataSource;
import com.koma.common.util.Constants;
import com.koma.common.util.LogUtils;

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
    public Flowable<List<TopList.BillboardListResponse.Billboard>> getTopLists() {
        return mAudientApi.getTopLists(Constants.APP_ID, Constants.ACCESS_TOKEN, getTimeStamp())
                .map(new Function<TopList, List<TopList.BillboardListResponse.Billboard>>() {
                    @Override
                    public List<TopList.BillboardListResponse.Billboard> apply(TopList topList) throws Exception {
                        LogUtils.i(TAG, "getTopLists topList :" + topList.resultMessage + "," + topList.resultCode);

                        return topList.billboardListResponse.billboardList.billboards;
                    }
                });
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
    public Flowable<List<MusicFileItem>> getSearchReults(String keyword, String musicType, int count,
                                                         int page) {
        return mAudientApi.getMusics(keyword, musicType, count, page, Constants.APP_ID,
                Constants.ACCESS_TOKEN, getTimeStamp())
                .map(new Function<SearchResult, List<MusicFileItem>>() {
                    @Override
                    public List<MusicFileItem> apply(SearchResult searchResult) throws Exception {
                        return searchResult.searchSongDataResponse.musicFielItemLists.musics;
                    }
                });
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
    public Flowable<Album> getAlbum(MusicFileItem musicFileItem) {
        LogUtils.i(TAG, "getAlbum contentId :" + musicFileItem.contentId);
        return mAudientApi.getAlbum(String.valueOf(musicFileItem.contentId), String.valueOf(5),
                Constants.ALBUM_FORMAT, musicFileItem.actorName, musicFileItem.musicName,
                Constants.APP_ID, Constants.ACCESS_TOKEN, getTimeStamp())
                .map(new Function<AlbumResult, Album>() {
                    @Override
                    public Album apply(AlbumResult albumResult) throws Exception {
                        return albumResult.picResponse.album;
                    }
                });
    }
}
