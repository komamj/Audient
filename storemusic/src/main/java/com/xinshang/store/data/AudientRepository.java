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

import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.CommentResult;
import com.xinshang.store.data.entities.FavoriteListResult;
import com.xinshang.store.data.entities.FavoritesResult;
import com.xinshang.store.data.entities.FileResult;
import com.xinshang.store.data.entities.LyricResult;
import com.xinshang.store.data.entities.NowPlayingResult;
import com.xinshang.store.data.entities.SearchResult;
import com.xinshang.store.data.entities.SongDetailResult;
import com.xinshang.store.data.entities.StoreKeeper;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.data.entities.Token;
import com.xinshang.store.data.entities.ToplistDetailResult;
import com.xinshang.store.data.entities.ToplistResult;
import com.xinshang.store.data.source.AudientDataSource;
import com.xinshang.store.data.source.local.LocalDataSource;
import com.xinshang.store.data.source.remote.IRemoteDataSource;
import com.xinshang.store.data.source.remote.RemoteDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class AudientRepository implements AudientDataSource, IRemoteDataSource {
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
    public Flowable<StoreKeeper> getStoreKeeperInfo() {
        return mRemoteDataSource.getStoreKeeperInfo();
    }

    @Override
    public Flowable<List<ToplistResult>> getTopList() {
        return mRemoteDataSource.getTopList();
    }

    @Override
    public Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime) {
        return mRemoteDataSource.getToplistDetail(topId, showTime);
    }

    @Override
    public Flowable<SearchResult> getSearchReult(String keyword) {
        return mRemoteDataSource.getSearchReult(keyword);
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
    public Flowable<CommentResult> getCommentResult(String id) {
        return mRemoteDataSource.getCommentResult(id);
    }

    @Override
    public Flowable<NowPlayingResult> getNowPlayingResult() {
        return mRemoteDataSource.getNowPlayingResult();
    }

    @Override
    public Flowable<BaseResponse> getLoginResult(StoreKeeper storeKeeper) {
        return mRemoteDataSource.getLoginResult(storeKeeper);
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
}
