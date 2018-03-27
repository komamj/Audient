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

import com.xinshang.audient.model.entities.ApiResponse;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.BaseResponse;
import com.xinshang.audient.model.entities.CommentRequest;
import com.xinshang.audient.model.entities.CommentResult;
import com.xinshang.audient.model.entities.FavoriteListResult;
import com.xinshang.audient.model.entities.FavoritesResult;
import com.xinshang.audient.model.entities.FileResult;
import com.xinshang.audient.model.entities.LyricResult;
import com.xinshang.audient.model.entities.Music;
import com.xinshang.audient.model.entities.NowPlayingResponse;
import com.xinshang.audient.model.entities.SearchResult;
import com.xinshang.audient.model.entities.SongDetailResult;
import com.xinshang.audient.model.entities.StoreDataBean;
import com.xinshang.audient.model.entities.StoreSong;
import com.xinshang.audient.model.entities.StoreVoteResponse;
import com.xinshang.audient.model.entities.ThumbUpSongRequest;
import com.xinshang.audient.model.entities.Token;
import com.xinshang.audient.model.entities.ToplistDetailResult;
import com.xinshang.audient.model.entities.ToplistResult;
import com.xinshang.audient.model.entities.UserResponse;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by koma on 3/6/18.
 */

public interface IRemoteDataSource {
    Flowable<UserResponse> getUserInfo();

    Flowable<List<ToplistResult>> getTopList();

    Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime, int page, int size);

    Flowable<SearchResult> getSearchReult(String keyword);

    Flowable<LyricResult> getLyricResult(String id);

    Flowable<SongDetailResult> getSongDetailResult(String id);

    Flowable<FileResult> getFileResult(String id);

    Flowable<CommentResult> getCommentResult(String id);

    Flowable<NowPlayingResponse> getNowPlayingResult();

    Flowable<BaseResponse> addFavorite(String name);

    Flowable<Token> getAccessToken(String code);

    Flowable<BaseResponse> addToFavorite(String favoriteId, Audient audient);

    Flowable<FavoritesResult> getFavoriteResult();

    Flowable<FavoriteListResult> getFavoriteListResult(String favoriteId);

    Flowable<BaseResponse> modifyFavoritesName(String favoritesId, String name);

    Flowable<BaseResponse> deleteFavorite(String id);

    Flowable<BaseResponse> deleteFavoritesSong(String favoritesId);

    Flowable<BaseResponse> addComment(CommentRequest comment);

    Flowable<BaseResponse> thumbUpComment(String commentId);

    Flowable<BaseResponse> cancelThumbUpComment(String commentId);

    Flowable<BaseResponse> thumbUpSong(ThumbUpSongRequest thumbUpSongRequest);

    Flowable<BaseResponse> cancelThumbUpSong(String storeId, String mediaId);

    Flowable<ApiResponse<StoreDataBean>> getStores(boolean isOnline, int page, int size, String sort);

    void sendLoginRequest();

    Flowable<StoreVoteResponse> getVoteInfo(String mediaId, String storeId);

    Flowable<BaseResponse> addToPlaylist(Music music);

    Flowable<ApiResponse<List<StoreSong>>> getStorePlaylist(String storeId);
}
