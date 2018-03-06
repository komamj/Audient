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
package com.xinshang.store.data.source.remote;

import com.xinshang.store.data.AudientApi;
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
import com.xinshang.store.utils.Constants;

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
    private final AudientApi mAudientApi;

    @Inject
    public RemoteDataSource(AudientApi audientApi) {
        mAudientApi = audientApi;
    }

    @Override
    public Flowable<List<TencentMusic>> getAudientTests() {
        return null;
    }

    @Override
    public Flowable<StoreKeeper> getStoreKeeperInfo() {
        return mAudientApi.getStoreKeeperInfo();
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
        return mAudientApi.getComments(id, 0, 40, null);
    }

    @Override
    public Flowable<NowPlayingResult> getNowPlayingResult() {
        return Flowable.create(new FlowableOnSubscribe<NowPlayingResult>() {
            @Override
            public void subscribe(FlowableEmitter<NowPlayingResult> emitter) throws Exception {
                NowPlayingResult nowPlayingResult = new NowPlayingResult();
                TencentMusic audient = new TencentMusic();
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
    public Flowable<Token> getToken(String userName, String password) {
        return mAudientApi.getAccessToken(userName, password, Constants.GRANT_TYPE,
                Constants.CLIENT_ID, Constants.CLIENT_SECRET);
    }

    @Override
    public Flowable<BaseResponse> addToFavorite(String favoriteId, TencentMusic audient) {
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
}
