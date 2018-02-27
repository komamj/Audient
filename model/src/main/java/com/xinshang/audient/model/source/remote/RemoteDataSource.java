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
package com.xinshang.audient.model.source.remote;

import com.xinshang.audient.model.AudientApi;
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
import com.xinshang.common.util.Constants;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

@Singleton
public class RemoteDataSource implements AudientDataSource {
    private static final String TAG = RemoteDataSource.class.getSimpleName();

    private String mAccessToken = "Bearer f00c3aca-09c1-47f3-b434-403cd87bc1bd";

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
    public Flowable<BaseResponse> addToFavorite(String favoriteId, Audient audient) {
        return mAudientApi.addToFavorite(mAccessToken, favoriteId, audient);
    }

    @Override
    public Flowable<FavoritesResult> getFavoriteResult() {
        return mAudientApi.getFavoriteResult(mAccessToken, null);
    }

    @Override
    public Flowable<FavoriteListResult> getFavoriteListResult(String favoriteId) {
        return mAudientApi.getFavoriteListResult(mAccessToken, favoriteId);
    }

    @Override
    public Flowable<BaseResponse> modifyFavoritesName(String favoritesId, String name) {
        return mAudientApi.modifyFavoriteName(mAccessToken, favoritesId, name);
    }

    @Override
    public Flowable<BaseResponse> deleteFavorite(String id) {
        return mAudientApi.deleteFavorite(mAccessToken, id);
    }

    @Override
    public Flowable<BaseResponse> deleteFavoritesSong(String favoritesId) {
        return mAudientApi.deleteFavoritesSong(mAccessToken, favoritesId);
    }
}
