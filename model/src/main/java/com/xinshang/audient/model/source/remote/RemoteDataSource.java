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

import android.support.annotation.NonNull;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.xinshang.audient.model.AudientApi;
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
import com.xinshang.audient.model.entities.ToplistDataBean;
import com.xinshang.audient.model.entities.ToplistDetailResult;
import com.xinshang.audient.model.entities.UserResponse;
import com.xinshang.audient.model.source.AudientDataSource;
import com.xinshang.common.util.Constants;

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
    private static final String SCOPE = "snsapi_userinfo";

    private final AudientApi mAudientApi;

    private final IWXAPI mWeChatApi;

    @Inject
    public RemoteDataSource(AudientApi audientApi, IWXAPI weChatApi) {
        mAudientApi = audientApi;
        mWeChatApi = weChatApi;
    }

    @Override
    public Flowable<List<Audient>> getAudientTests() {
        return null;
    }

    @Override
    public Flowable<UserResponse> getUserInfo() {
        return mAudientApi.getUserInfo();
    }

    @Override
    public Flowable<ApiResponse<List<ToplistDataBean>>> getTopList() {
        return mAudientApi.getTopLists();
    }

    @Override
    public Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime, int page,
                                                          int size) {
        return mAudientApi.getToplistDetail(topId, showTime, page, size);
    }

    @Override
    public Flowable<SearchResult> getSearchReult(String keyword) {
        return mAudientApi.getSeachResults(keyword, 0, 40, true);
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
        return mAudientApi.getComments(id, null, null, 0, 40);
    }

    @Override
    public Flowable<NowPlayingResponse> getNowPlayingResult() {
        return Flowable.create(new FlowableOnSubscribe<NowPlayingResponse>() {
            @Override
            public void subscribe(FlowableEmitter<NowPlayingResponse> emitter) throws Exception {
                NowPlayingResponse nowPlayingResult = new NowPlayingResponse();
                Audient audient = new Audient();
                audient.mediaId = "003evjhg3qIe9S";
                audient.duration = 260;
                audient.artistId = "0040D7gK4aI54k";
                audient.artistName = "谭咏麟";
                audient.mediaName = "一生中最爱";
                audient.albumId = "0018tEZm032RCk";
                audient.albumName = "神话1991";
                nowPlayingResult.audient = audient;

                emitter.onNext(nowPlayingResult);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Flowable<BaseResponse> addFavorite(String name) {
        return mAudientApi.addFavorite(name);
    }

    @Override
    public Flowable<Token> getAccessToken(String code) {
        return mAudientApi.getAccessToken(code, Constants.GRANT_TYPE,
                Constants.CLIENT_ID, Constants.CLIENT_SECRET);
    }

    @Override
    public Flowable<BaseResponse> addToFavorite(String favoriteId, Audient audient) {
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

    @Override
    public Flowable<BaseResponse> addComment(CommentRequest comment) {
        return mAudientApi.postCommentRequest(comment);
    }

    @Override
    public void sendLoginRequest() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = SCOPE;
        req.state = TAG;
        mWeChatApi.sendReq(req);
    }

    @Override
    public Flowable<StoreVoteResponse> getVoteInfo(String mediaId, String storeId) {
        return mAudientApi.getVoteInfo(mediaId, storeId);
    }

    @Override
    public Flowable<BaseResponse> addToPlaylist(Music music) {
        return mAudientApi.addToPlaylist(music);
    }

    @Override
    public Flowable<ApiResponse<List<StoreSong>>> getStorePlaylist(@NonNull String storeId) {
        return mAudientApi.getStorePlaylist(storeId);
    }

    @Override
    public Flowable<ApiResponse> sendFeedback(String title, String content) {
        return mAudientApi.postFeedback(title, content);
    }

    @Override
    public Flowable<BaseResponse> thumbUpComment(String commentId) {
        return mAudientApi.thumbUpComment(commentId);
    }

    @Override
    public Flowable<BaseResponse> cancelThumbUpComment(String commentId) {
        return mAudientApi.cancelThumbUpComment(commentId);
    }

    @Override
    public Flowable<BaseResponse> thumbUpSong(ThumbUpSongRequest thumbUpSongRequest) {
        return mAudientApi.thumbupSong(thumbUpSongRequest);
    }

    @Override
    public Flowable<BaseResponse> cancelThumbUpSong(String storeId, String mediaId) {
        return mAudientApi.cancelThumbUpSong(storeId, mediaId);
    }

    @Override
    public Flowable<ApiResponse<StoreDataBean>> getStores(boolean isOnline, int page, int size, String sort) {
        return mAudientApi.getStores(isOnline, page, size, sort);
    }
}
