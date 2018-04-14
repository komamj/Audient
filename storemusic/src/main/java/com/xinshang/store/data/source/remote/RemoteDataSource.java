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
package com.xinshang.store.data.source.remote;

import com.xinshang.store.data.AudientApi;
import com.xinshang.store.data.entities.AlbumResponse;
import com.xinshang.store.data.entities.AlbumSongResponse;
import com.xinshang.store.data.entities.ApiResponse;
import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.CommentDataBean;
import com.xinshang.store.data.entities.FavoriteListResult;
import com.xinshang.store.data.entities.FavoritesResult;
import com.xinshang.store.data.entities.FileResult;
import com.xinshang.store.data.entities.LyricResult;
import com.xinshang.store.data.entities.Music;
import com.xinshang.store.data.entities.NowPlayingResponse;
import com.xinshang.store.data.entities.PlayAllRequest;
import com.xinshang.store.data.entities.PlaylistResponse;
import com.xinshang.store.data.entities.PlaylistSongResponse;
import com.xinshang.store.data.entities.SearchResponse;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.data.entities.SongDetailResult;
import com.xinshang.store.data.entities.Store;
import com.xinshang.store.data.entities.StoreKeeperResponse;
import com.xinshang.store.data.entities.StoreSong;
import com.xinshang.store.data.entities.Token;
import com.xinshang.store.data.entities.ToplistDataBean;
import com.xinshang.store.data.entities.ToplistSongResponse;
import com.xinshang.store.data.source.AudientDataSource;
import com.xinshang.store.utils.Constants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

@Singleton
public class RemoteDataSource implements AudientDataSource, IRemoteDataSource {
    private static final String TAG = RemoteDataSource.class.getSimpleName();

    private final AudientApi mAudientApi;

    @Inject
    public RemoteDataSource(AudientApi audientApi) {
        mAudientApi = audientApi;
    }

    @Override
    public Flowable<List<Song>> getAudientTests() {
        return null;
    }

    @Override
    public Flowable<StoreKeeperResponse> getStoreKeeperInfo() {
        return mAudientApi.getStoreKeeperInfo();
    }

    @Override
    public Flowable<ApiResponse<List<ToplistDataBean>>> getToplists() {
        return mAudientApi.getToplists();
    }

    @Override
    public Flowable<ApiResponse<ToplistSongResponse>> getToplistSongs(int topId, String showTime, int page,
                                                         int count) {
        return mAudientApi.getToplistSongs(topId, showTime, page, count);
    }

    @Override
    public Flowable<ApiResponse<SearchResponse>> searchSongs(String keyword, int page, int count) {
        return mAudientApi.searchSongs(keyword, page, count, false);
    }

    @Override
    public Flowable<ApiResponse<PlaylistResponse>> searchPlaylists(String keyword, int page,
                                                                   int size) {
        return mAudientApi.searchPlaylists(keyword, page, size);
    }

    @Override
    public Flowable<ApiResponse<PlaylistSongResponse>> getPlaylistDetails(String id) {
        return mAudientApi.getPlaylistDetails(id);
    }

    @Override
    public Flowable<ApiResponse<AlbumResponse>> searchAlbums(String keyword, int page, int size) {
        return mAudientApi.searchAlbums(keyword, page, size);
    }

    @Override
    public Flowable<ApiResponse<AlbumSongResponse>> getAlbumSongs(String id) {
        return mAudientApi.getAlbumSongs(id);
    }

    @Override
    public Flowable<LyricResult> getLyricResult(String id) {
        return mAudientApi.getLyricResult(id);
    }

    @Override
    public Flowable<SongDetailResult> getSongDetailResult(String id) {
        return mAudientApi.getSongDetailResult(id);
    }

    @Override
    public Flowable<FileResult> getFileResult(String id) {
        return mAudientApi.getFileResult(id);
    }

    @Override
    public Flowable<NowPlayingResponse> getNowPlaying(String storeId) {
        return Flowable.create(new FlowableOnSubscribe<NowPlayingResponse>() {
            @Override
            public void subscribe(FlowableEmitter<NowPlayingResponse> emitter) throws Exception {
                NowPlayingResponse nowPlayingResult = new NowPlayingResponse();
                Song audient = new Song();
                audient.mediaId = "003evjhg3qIe9S";
                audient.duration = 260;
                audient.artistId = "0040D7gK4aI54k";
                audient.artistName = "谭咏麟";
                audient.mediaName = "一生中最爱";
                audient.albumId = "0018tEZm032RCk";
                audient.albumName = "神话1991";
                nowPlayingResult.tencentMusic = audient;

                emitter.onNext(nowPlayingResult);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<ApiResponse<CommentDataBean>> getComments(String mid, String sortord, String storeId, int page, int size) {
        return mAudientApi.getComments(mid, sortord, storeId, page, size);
    }

    @Override
    public Flowable<BaseResponse> addFavorite(String name) {
        return mAudientApi.addFavorite(name);
    }

    @Override
    public Flowable<Token> getToken(String userName, String password) {
        return mAudientApi.getAccessToken(userName, password, Constants.GRANT_TYPE,
                Constants.CLIENT_ID, Constants.CLIENT_SECRET);
    }

    @Override
    public Flowable<BaseResponse> addToPlaylist(Music music) {
        return mAudientApi.addToPlaylist(music);
    }

    @Override
    public Flowable<BaseResponse> deleteSongFromPlaylist(String id) {
        return mAudientApi.deleteSongFromPlaylist(id);
    }

    @Override
    public Flowable<BaseResponse> addAllToPlaylist(String storeId, String favoritesId) {
        return mAudientApi.addAllToPlaylist(storeId, favoritesId);
    }

    @Override
    public Flowable<ApiResponse<List<StoreSong>>> getStorePlaylist(String storeId) {
        return mAudientApi.getStorePlaylist(storeId);
    }

    @Override
    public Flowable<ApiResponse<List<Store>>> getStoreInfo() {
        return mAudientApi.getStoreInfo();
    }

    @Override
    public Flowable<BaseResponse> playAllSongs(String id, PlayAllRequest playAllRequest) {
        return mAudientApi.playAllSongs(id, playAllRequest);
    }

    @Override
    public Flowable<BaseResponse> addToFavorite(String favoriteId, Song audient) {
        return mAudientApi.addToFavorite(favoriteId, audient);
    }

    @Override
    public Flowable<FavoritesResult> getFavoriteResult() {
        return mAudientApi.getFavoriteResult(null);
    }

    @Override
    public Flowable<FavoriteListResult> getFavoriteListResult(String favoriteId) {
        return mAudientApi.getFavoriteListResult(favoriteId);
    }

    @Override
    public Flowable<BaseResponse> modifyFavoritesName(String favoritesId, String name) {
        return mAudientApi.modifyFavoriteName(favoritesId, name);
    }

    @Override
    public Flowable<BaseResponse> deleteFavorite(String id) {
        return mAudientApi.deleteFavorite(id);
    }

    @Override
    public Flowable<BaseResponse> deleteFavoritesSong(String favoritesId) {
        return mAudientApi.deleteFavoritesSong(favoritesId);
    }
}
