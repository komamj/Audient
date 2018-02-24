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
package com.xinshang.audient.model;

import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.BaseResponse;
import com.xinshang.audient.model.entities.CommentResult;
import com.xinshang.audient.model.entities.FavoriteListResult;
import com.xinshang.audient.model.entities.FavoritesResult;
import com.xinshang.audient.model.entities.FileResult;
import com.xinshang.audient.model.entities.LyricResult;
import com.xinshang.audient.model.entities.NowPlayingResult;
import com.xinshang.audient.model.entities.SearchResult;
import com.xinshang.audient.model.entities.SongDetailResult;
import com.xinshang.audient.model.entities.Token;
import com.xinshang.audient.model.entities.ToplistDetailResult;
import com.xinshang.audient.model.entities.ToplistResult;
import com.xinshang.audient.model.entities.User;
import com.xinshang.audient.model.source.AudientDataSource;
import com.xinshang.audient.model.source.local.LocalDataSource;
import com.xinshang.audient.model.source.remote.RemoteDataSource;

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
    public Flowable<List<Audient>> getAudientTests() {
        return mLocalDataSource.getAudientTests();
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
        return mLocalDataSource.getCommentResult(id);
    }

    @Override
    public Flowable<NowPlayingResult> getNowPlayingResult() {
        return mLocalDataSource.getNowPlayingResult();
    }

    @Override
    public Flowable<Boolean> getLoginStatus() {
        return mLocalDataSource.getLoginStatus();
    }

    @Override
    public Flowable<BaseResponse> getLoginResult(User user) {
        return mRemoteDataSource.getLoginResult(user);
    }

    @Override
    public Flowable<Boolean> setLoginStatus(boolean loginStatus) {
        return mLocalDataSource.setLoginStatus(loginStatus);
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
    public Flowable<BaseResponse> addToFavorite(String favoriteId, Audient audient) {
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
}
