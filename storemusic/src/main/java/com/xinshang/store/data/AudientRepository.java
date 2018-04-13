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

import com.xinshang.store.data.entities.ApiResponse;
import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.CommandResponse;
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
import com.xinshang.store.data.entities.SearchResult;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.data.entities.SongDetailResult;
import com.xinshang.store.data.entities.Store;
import com.xinshang.store.data.entities.StoreKeeperResponse;
import com.xinshang.store.data.entities.StoreSong;
import com.xinshang.store.data.entities.Token;
import com.xinshang.store.data.entities.ToplistDataBean;
import com.xinshang.store.data.entities.ToplistDetailResult;
import com.xinshang.store.data.source.AudientDataSource;
import com.xinshang.store.data.source.local.ILocalDataSource;
import com.xinshang.store.data.source.local.LocalDataSource;
import com.xinshang.store.data.source.remote.IRemoteDataSource;
import com.xinshang.store.data.source.remote.RemoteDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class AudientRepository implements AudientDataSource, IRemoteDataSource, ILocalDataSource {
    private final LocalDataSource mLocalDataSource;

    private final RemoteDataSource mRemoteDataSource;

    @Inject
    public AudientRepository(@Local LocalDataSource localDataSource,
                             @Remote RemoteDataSource remoteDataSource) {
        mLocalDataSource = localDataSource;

        mRemoteDataSource = remoteDataSource;
    }

    @Override
    public Flowable<List<Song>> getAudientTests() {
        return mLocalDataSource.getAudientTests();
    }

    @Override
    public Flowable<StoreKeeperResponse> getStoreKeeperInfo() {
        return mRemoteDataSource.getStoreKeeperInfo();
    }

    @Override
    public Flowable<ApiResponse<List<ToplistDataBean>>> getToplists() {
        return mRemoteDataSource.getToplists();
    }

    @Override
    public Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime, int page, int count) {
        return mRemoteDataSource.getToplistDetail(topId, showTime, page, count);
    }

    @Override
    public Flowable<SearchResult> searchSongs(String keyword, int page, int count) {
        return mRemoteDataSource.searchSongs(keyword, page, count);
    }

    @Override
    public Flowable<ApiResponse<PlaylistResponse>> searchPlaylists(String keyword, int page, int size) {
        return mRemoteDataSource.searchPlaylists(keyword, page, size);
    }

    @Override
    public Flowable<ApiResponse<PlaylistSongResponse>> getPlaylistDetails(String id) {
        return mRemoteDataSource.getPlaylistDetails(id);
    }

    @Override
    public Flowable<LyricResult> getLyricResult(String id) {
        return mRemoteDataSource.getLyricResult(id);
    }

    @Override
    public Flowable<SongDetailResult> getSongDetailResult(String id) {
        return mRemoteDataSource.getSongDetailResult(id);
    }

    @Override
    public Flowable<FileResult> getFileResult(String id) {
        return mRemoteDataSource.getFileResult(id);
    }

    @Override
    public Flowable<NowPlayingResponse> getNowPlaying(String storeId) {
        return mRemoteDataSource.getNowPlaying(storeId);
    }

    @Override
    public Flowable<ApiResponse<CommentDataBean>> getComments(String mid, String sortord, String storeId, int page, int size) {
        return mRemoteDataSource.getComments(mid, sortord, storeId, page, size);
    }

    @Override
    public Flowable<BaseResponse> addFavorite(String name) {
        return mRemoteDataSource.addFavorite(name);
    }

    @Override
    public Flowable<Token> getToken(String userName, String password) {
        return mRemoteDataSource.getToken(userName, password);
    }

    @Override
    public Flowable<BaseResponse> addToPlaylist(Music music) {
        return mRemoteDataSource.addToPlaylist(music);
    }

    @Override
    public Flowable<BaseResponse> deleteSongFromPlaylist(String id) {
        return mRemoteDataSource.deleteSongFromPlaylist(id);
    }

    @Override
    public Flowable<BaseResponse> addAllToPlaylist(String storeId, String favoritesId) {
        return mRemoteDataSource.addAllToPlaylist(storeId, favoritesId);
    }

    @Override
    public Flowable<ApiResponse<List<StoreSong>>> getStorePlaylist(String storeId) {
        return mRemoteDataSource.getStorePlaylist(storeId);
    }

    @Override
    public Flowable<ApiResponse<List<Store>>> getStoreInfo() {
        return mRemoteDataSource.getStoreInfo();
    }

    @Override
    public Flowable<BaseResponse> playAllSongs(String id, PlayAllRequest playAllRequest) {
        return mRemoteDataSource.playAllSongs(id, playAllRequest);
    }

    @Override
    public Flowable<BaseResponse> addToFavorite(String favoriteId, Song audient) {
        return mRemoteDataSource.addToFavorite(favoriteId, audient);
    }

    @Override
    public Flowable<FavoritesResult> getFavoriteResult() {
        return mRemoteDataSource.getFavoriteResult();
    }

    @Override
    public Flowable<FavoriteListResult> getFavoriteListResult(String favoriteId) {
        return mRemoteDataSource.getFavoriteListResult(favoriteId);
    }

    @Override
    public Flowable<BaseResponse> modifyFavoritesName(String favoritesId, String name) {
        return mRemoteDataSource.modifyFavoritesName(favoritesId, name);
    }

    @Override
    public Flowable<BaseResponse> deleteFavorite(String id) {
        return mRemoteDataSource.deleteFavorite(id);
    }

    @Override
    public Flowable<BaseResponse> deleteFavoritesSong(String favoritesId) {
        return mRemoteDataSource.deleteFavoritesSong(favoritesId);
    }

    @Override
    public void persistenceUserName(String userName) {
        mLocalDataSource.persistenceUserName(userName);
    }

    @Override
    public String getUserName() {
        return mLocalDataSource.getUserName();
    }

    @Override
    public void persistenceUserPassword(String password) {
        mLocalDataSource.persistenceUserPassword(password);
    }

    @Override
    public String getUserPassword() {
        return mLocalDataSource.getUserPassword();
    }

    @Override
    public void persistenceLoginStatus(boolean loginStatus) {
        mLocalDataSource.persistenceLoginStatus(loginStatus);
    }

    @Override
    public boolean getLoginStatus() {
        return mLocalDataSource.getLoginStatus();
    }

    @Override
    public void persistenceAccessToken(String accessToken) {
        mLocalDataSource.persistenceAccessToken(accessToken);
    }

    @Override
    public String getAccessToken() {
        return mLocalDataSource.getAccessToken();
    }

    @Override
    public void persistenceRefreshToken(String refreshToken) {
        mLocalDataSource.persistenceRefreshToken(refreshToken);
    }

    @Override
    public String getRefreshToken() {
        return mLocalDataSource.getRefreshToken();
    }

    @Override
    public void persistenceStoreId(String storeId) {
        mLocalDataSource.persistenceStoreId(storeId);
    }

    @Override
    public String getStoreId() {
        return mLocalDataSource.getStoreId();
    }

    @Override
    public Flowable<CommandResponse<String>> parsingCommandResponse(String response) {
        return mLocalDataSource.parsingCommandResponse(response);
    }
}
