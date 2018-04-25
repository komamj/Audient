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

import com.xinshang.audient.model.entities.ApiResponse;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.BaseResponse;
import com.xinshang.audient.model.entities.CommandResponse;
import com.xinshang.audient.model.entities.CommentDataBean;
import com.xinshang.audient.model.entities.CommentRequest;
import com.xinshang.audient.model.entities.FavoriteListResult;
import com.xinshang.audient.model.entities.FavoritesResult;
import com.xinshang.audient.model.entities.Feedback;
import com.xinshang.audient.model.entities.FileResult;
import com.xinshang.audient.model.entities.LyricResult;
import com.xinshang.audient.model.entities.Music;
import com.xinshang.audient.model.entities.NowPlayingResponse;
import com.xinshang.audient.model.entities.OrderResponse;
import com.xinshang.audient.model.entities.PayRequestInfo;
import com.xinshang.audient.model.entities.PlaylistResponse;
import com.xinshang.audient.model.entities.SearchResult;
import com.xinshang.audient.model.entities.SongDetailResult;
import com.xinshang.audient.model.entities.Store;
import com.xinshang.audient.model.entities.StoreDataBean;
import com.xinshang.audient.model.entities.StoreSong;
import com.xinshang.audient.model.entities.StoreVoteResponse;
import com.xinshang.audient.model.entities.ThumbUpSongRequest;
import com.xinshang.audient.model.entities.Token;
import com.xinshang.audient.model.entities.ToplistDataBean;
import com.xinshang.audient.model.entities.ToplistDetailResult;
import com.xinshang.audient.model.entities.UserResponse;
import com.xinshang.audient.model.entities.Version;
import com.xinshang.audient.model.entities.WXPayRequest;
import com.xinshang.audient.model.source.AudientDataSource;
import com.xinshang.audient.model.source.local.ILocalDataSource;
import com.xinshang.audient.model.source.local.LocalDataSource;
import com.xinshang.audient.model.source.remote.IRemoteDataSource;
import com.xinshang.audient.model.source.remote.RemoteDataSource;

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
    public Flowable<List<Audient>> getAudientTests() {
        return mLocalDataSource.getAudientTests();
    }

    @Override
    public Flowable<UserResponse> getUserInfo() {
        return mRemoteDataSource.getUserInfo();
    }

    @Override
    public Flowable<ApiResponse<Store>> getStoreInfo(String storeId) {
        return mRemoteDataSource.getStoreInfo(storeId);
    }

    @Override
    public Flowable<ApiResponse<List<ToplistDataBean>>> getToplists() {
        return mRemoteDataSource.getToplists();
    }

    @Override
    public Flowable<ToplistDetailResult> getToplistDetail(int topId, String showTime, int page,
                                                          int size) {
        return mRemoteDataSource.getToplistDetail(topId, showTime, page, size);
    }

    @Override
    public Flowable<SearchResult> searchSongs(String keyword, int page, int size) {
        return mRemoteDataSource.searchSongs(keyword, page, size);
    }

    @Override
    public Flowable<ApiResponse<PlaylistResponse>> getSearchPlaylists(String keyword, int page, int size) {
        return mRemoteDataSource.getSearchPlaylists(keyword, page, size);
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
    public Flowable<ApiResponse<CommentDataBean>> getComments(String mid, String sortord, String storeId, int page,
                                                              int size) {
        return mRemoteDataSource.getComments(mid, sortord, storeId, page, size);
    }

    @Override
    public Flowable<NowPlayingResponse> getNowPlayingResult() {
        return mRemoteDataSource.getNowPlayingResult();
    }

    @Override
    public Flowable<BaseResponse> addFavorite(String name) {
        return mRemoteDataSource.addFavorite(name);
    }

    @Override
    public Flowable<Token> getAccessToken(String code) {
        return mRemoteDataSource.getAccessToken(code);
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
    public Flowable<BaseResponse> addComment(CommentRequest comment) {
        return mRemoteDataSource.addComment(comment);
    }

    @Override
    public void sendLoginRequest() {
        mRemoteDataSource.sendLoginRequest();
    }

    @Override
    public void sendWXPayRequest(PayRequestInfo payRequestInfo) {
        mRemoteDataSource.sendWXPayRequest(payRequestInfo);
    }

    @Override
    public Flowable<StoreVoteResponse> getVoteInfo(String mediaId, String storeId) {
        return mRemoteDataSource.getVoteInfo(mediaId, storeId);
    }

    @Override
    public Flowable<BaseResponse> addToPlaylist(Music music) {
        return mRemoteDataSource.addToPlaylist(music);
    }

    @Override
    public Flowable<ApiResponse<List<StoreSong>>> getStorePlaylist(String storeId) {
        return mRemoteDataSource.getStorePlaylist(storeId);
    }

    @Override
    public Flowable<ApiResponse> sendFeedback(Feedback feedback) {
        return mRemoteDataSource.sendFeedback(feedback);
    }

    @Override
    public Flowable<ApiResponse<OrderResponse>> postOrder(WXPayRequest wxPayRequest) {
        return mRemoteDataSource.postOrder(wxPayRequest);
    }

    @Override
    public Flowable<BaseResponse> getOrderResult(String tid, String oid) {
        return mRemoteDataSource.getOrderResult(tid,oid);
    }

    @Override
    public Flowable<Version> getNewestVersion() {
        return mRemoteDataSource.getNewestVersion();
    }

    @Override
    public Flowable<BaseResponse> thumbUpComment(String commentId) {
        return mRemoteDataSource.thumbUpComment(commentId);
    }

    @Override
    public Flowable<BaseResponse> cancelThumbUpComment(String commentId) {
        return mRemoteDataSource.cancelThumbUpComment(commentId);
    }

    @Override
    public Flowable<BaseResponse> thumbUpSong(ThumbUpSongRequest thumbUpSongRequest) {
        return mRemoteDataSource.thumbUpSong(thumbUpSongRequest);
    }

    @Override
    public Flowable<BaseResponse> cancelThumbUpSong(String storeId, String mediaId) {
        return mRemoteDataSource.cancelThumbUpSong(storeId, mediaId);
    }

    @Override
    public Flowable<ApiResponse<StoreDataBean>> getStores(boolean isOnline, int page, int size, String sort) {
        return mRemoteDataSource.getStores(isOnline, page, size, sort);
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

    @Override
    public boolean isFirstDemand() {
        return mLocalDataSource.isFirstDemand();
    }

    @Override
    public void persistenceDemandStatus(boolean demandStatus) {
        mLocalDataSource.persistenceDemandStatus(demandStatus);
    }

    @Override
    public Flowable<Integer> getCurrentVersionCode() {
        return mLocalDataSource.getCurrentVersionCode();
    }
}
