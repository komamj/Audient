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

import com.koma.audient.model.entities.Audient;
import com.koma.audient.model.entities.BaseResponse;
import com.koma.audient.model.entities.CommentResult;
import com.koma.audient.model.entities.FileResult;
import com.koma.audient.model.entities.LyricResult;
import com.koma.audient.model.entities.NowPlayingResult;
import com.koma.audient.model.entities.SearchResult;
import com.koma.audient.model.entities.SongDetailResult;
import com.koma.audient.model.entities.Token;
import com.koma.audient.model.entities.ToplistDetailResult;
import com.koma.audient.model.entities.ToplistResult;
import com.koma.audient.model.entities.User;
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
    public Flowable<BaseResponse> getFavoriteResult(String name) {
        return mRemoteDataSource.getFavoriteResult(name);
    }

    @Override
    public Flowable<Token> getToken(String userName, String password) {
        return mRemoteDataSource.getToken("koma_mj", "201124koma");
    }
}
