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

import com.koma.audient.model.AudientApi;
import com.koma.audient.model.entities.Audient;
import com.koma.audient.model.entities.BaseResponse;
import com.koma.audient.model.entities.CommentResult;
import com.koma.audient.model.entities.FavoriteListResult;
import com.koma.audient.model.entities.FavoritesResult;
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
import com.koma.common.util.Constants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class RemoteDataSource implements AudientDataSource {
    private static final String TAG = RemoteDataSource.class.getSimpleName();

    private String mAccessToken = "Bearer a312c995-25a3-4be9-af97-4b76c0209957";

    private final AudientApi mAudientApi;

    @Inject
    public RemoteDataSource(AudientApi audientApi) {
        mAudientApi = audientApi;
    }

    @Override
    public Flowable<List<Audient>> getAudientTests() {
        return null;
    }

    @Override
    public Flowable<List<ToplistResult>> getTopList() {
        return mAudientApi.getTopLists();
    }

    @Override
    public Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime) {
        return mAudientApi.getToplistDetail(topId, showTime);
    }

    @Override
    public Flowable<SearchResult> getSearchReult(String keyword) {
        return mAudientApi.getSeachResults(keyword);
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
    public Flowable<CommentResult> getCommentResult(String id) {
        return null;
    }

    @Override
    public Flowable<NowPlayingResult> getNowPlayingResult() {
        return null;
    }

    @Override
    public Flowable<Boolean> getLoginStatus() {
        return null;
    }

    @Override
    public Flowable<BaseResponse> getLoginResult(User user) {
        return mAudientApi.getLoginResult(user);
    }

    @Override
    public Flowable<Boolean> setLoginStatus(boolean loginStatus) {
        return null;
    }

    @Override
    public Flowable<BaseResponse> addFavorite(String name) {
        return mAudientApi.addFavorite(mAccessToken, name);
    }

    @Override
    public Flowable<Token> getToken(String userName, String password) {
        return mAudientApi.getAccessToken(userName, password, Constants.GRANT_TYPE,
                Constants.CLIENT_ID, Constants.CLIENT_SECRET);
    }

    @Override
    public Flowable<FavoritesResult> getFavoriteResult() {
        return mAudientApi.getFavoriteResult(mAccessToken, null);
    }

    @Override
    public Flowable<FavoriteListResult> getFavoriteListResult(String favoriteId) {
        return mAudientApi.getFavoriteListResult(mAccessToken, favoriteId);
    }
}
