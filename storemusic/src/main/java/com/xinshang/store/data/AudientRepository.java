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
import com.xinshang.store.data.entities.CommentResult;
import com.xinshang.store.data.entities.FavoriteListResult;
import com.xinshang.store.data.entities.FavoritesResult;
import com.xinshang.store.data.entities.FileResult;
import com.xinshang.store.data.entities.LyricResult;
import com.xinshang.store.data.entities.Music;
import com.xinshang.store.data.entities.NowPlayingResponse;
import com.xinshang.store.data.entities.SearchResult;
import com.xinshang.store.data.entities.SongDetailResult;
import com.xinshang.store.data.entities.Store;
import com.xinshang.store.data.entities.StoreKeeperResponse;
import com.xinshang.store.data.entities.StorePlaylist;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.data.entities.Token;
import com.xinshang.store.data.entities.ToplistDetailResult;
import com.xinshang.store.data.entities.ToplistResult;
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
    public Flowable<List<TencentMusic>> getAudientTests() {
        return mLocalDataSource.getAudientTests();
    }

    @Override
    public Flowable<StoreKeeperResponse> getStoreKeeperInfo() {
        return mRemoteDataSource.getStoreKeeperInfo();
    }

    @Override
    public Flowable<List<ToplistResult>> getTopList() {
        return mRemoteDataSource.getTopList();
    }

    @Override
    public Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime, int page, int count) {
        return mRemoteDataSource.getToplistDetail(topId, showTime, page, count);
    }

    @Override
    public Flowable<SearchResult> getSearchReult(String keyword, int page, int count) {
        return mRemoteDataSource.getSearchReult(keyword, page, count);
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
    public Flowable<CommentResult> getCommentResult(String id, int page, int count, String sortord) {
        return mRemoteDataSource.getCommentResult(id, page, count, sortord);
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
    public Flowable<ApiResponse<List<StorePlaylist>>> getStorePlaylist(String storeId) {
        return mRemoteDataSource.getStorePlaylist(storeId);
    }

    @Override
    public Flowable<ApiResponse<List<Store>>> getStoreInfo() {
        return mRemoteDataSource.getStoreInfo();
    }

    @Override
    public Flowable<BaseResponse> addToFavorite(String favoriteId, TencentMusic audient) {
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
}
