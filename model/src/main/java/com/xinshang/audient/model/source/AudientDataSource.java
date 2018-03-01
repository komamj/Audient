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
package com.xinshang.audient.model.source;

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

import java.util.List;

import io.reactivex.Flowable;

public interface AudientDataSource {
    Flowable<List<Audient>> getAudientTests();

    Flowable<List<ToplistResult>> getTopList();

    Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime);

    Flowable<SearchResult> getSearchReult(String keyword);

    Flowable<LyricResult> getLyricResult(String id);

    Flowable<SongDetailResult> getSongDetailResult(String id);

    Flowable<FileResult> getFileResult(String id);

    Flowable<CommentResult> getCommentResult(String id);

    Flowable<NowPlayingResult> getNowPlayingResult();

    Flowable<Boolean> getLoginStatus();

    Flowable<BaseResponse> getLoginResult(User user);

    Flowable<Boolean> setLoginStatus(boolean loginStatus);

    Flowable<BaseResponse> addFavorite(String name);

    Flowable<Token> getToken(String userName, String password);

    Flowable<BaseResponse> addToFavorite(String favoriteId, Audient audient);

    Flowable<FavoritesResult> getFavoriteResult();

    Flowable<FavoriteListResult> getFavoriteListResult(String favoriteId);

    Flowable<BaseResponse> modifyFavoritesName(String favoritesId, String name);

    Flowable<BaseResponse> deleteFavorite(String id);

    Flowable<BaseResponse> deleteFavoritesSong(String favoritesId);
}