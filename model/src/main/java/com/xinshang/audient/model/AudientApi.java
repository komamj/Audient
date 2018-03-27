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
import com.xinshang.audient.model.entities.CommentRequest;
import com.xinshang.audient.model.entities.CommentResult;
import com.xinshang.audient.model.entities.FavoriteListResult;
import com.xinshang.audient.model.entities.FavoritesResult;
import com.xinshang.audient.model.entities.FileResult;
import com.xinshang.audient.model.entities.LyricResult;
import com.xinshang.audient.model.entities.Music;
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
import com.xinshang.audient.model.entities.User;
import com.xinshang.audient.model.entities.UserResponse;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AudientApi {
    /**
     * 获取榜单列表.
     */
    @GET("api/v1/openmusic/toplist")
    Flowable<ApiResponse<List<ToplistDataBean>>> getTopLists();

    /**
     * 获取榜单详情
     */
    @GET("api/v1/openmusic/toplist/{id}")
    Flowable<ToplistDetailResult> getToplistDetail(@Path("id") long topId,
                                                   @Query("k") String updateKey,
                                                   @Query("p") int page,
                                                   @Query("n") int pageCount);

    /**
     * 获取搜索结果.
     *
     * @param keyword 关键字.
     */
    @GET("api/v1/openmusic/search")
    Flowable<SearchResult> getSeachResults(@Query("w") String keyword, @Query("p") int page,
                                           @Query("n") int pageCount, @Query("f") boolean filter);

    /**
     * 获取歌词.
     *
     * @param id
     */
    @GET("api/v1/openmusic/{mediaId}/lyric")
    Flowable<LyricResult> getLyricResult(@Path("mediaId") String id);

    @GET("api/v1/openmusic/{mediaId}")
    Flowable<SongDetailResult> getSongDetailResult(@Path("mediaId") String id);

    @GET("api/v1/openmusic/{mediaId}/url")
    Flowable<FileResult> getFileResult(@Path("mediaId") String id);

    /**
     * 登录
     */
    @POST("account/register/user")
    Flowable<BaseResponse> getLoginResult(@Body User user);

    /**
     * 获取用户详情
     */
    @GET("api/v1/me")
    Flowable<UserResponse> getUserInfo();

    /**
     * 获取access_token
     */
    @FormUrlEncoded
    @POST("oauth/token")
    Flowable<Token> getAccessToken(@Field("username") String userName,
                                   @Field("password") String password,
                                   @Field("grant_type") String grantType,
                                   @Field("client_id") String clientId,
                                   @Field("client_secret") String clientSecret);

    /**
     * 获取access_token
     */
    @FormUrlEncoded
    @POST("oauth/token")
    Flowable<Token> refreshAccessToken(@Field("grant_type") String grantType,
                                       @Field("refresh_token") String refreshToken,
                                       @Field("client_id") String clientId,
                                       @Field("client_secret") String clientSecret);

    /**
     * 获取access_token
     */
    @FormUrlEncoded
    @POST("oauth/token")
    Flowable<Token> getAccessToken(@Field("code") String code,
                                   @Field("grant_type") String grantType,
                                   @Field("client_id") String clientId,
                                   @Field("client_secret") String clientSecret);

    /**
     * 添加歌单
     */
    @FormUrlEncoded
    @POST("api/v1/favorites")
    Flowable<BaseResponse> addFavorite(@Field("name") String name);

    /**
     * 获取我的所有歌单
     */
    @GET("api/v1/favorites/my")
    Flowable<FavoritesResult> getFavoriteResult(@Query("sort") String sortord);

    /**
     * 添加歌曲到相应歌单
     */
    @POST("api/v1/favorites/{id}/items")
    Flowable<BaseResponse> addToFavorite(@Path("id") String favoriteId, @Body Audient audient);

    /**
     * 获取歌单下的所有歌曲
     */
    @GET("api/v1/favorites/{id}/items")
    Flowable<FavoriteListResult> getFavoriteListResult(@Path("id") String favoriteId);

    /**
     * 删除歌单
     */
    @DELETE("api/v1/favorites/{id}")
    Flowable<BaseResponse> deleteFavorite(@Path("id") String id);

    /**
     * 删除歌单里的歌曲
     */
    @DELETE("api/v1/favorites/items/{id}")
    Flowable<BaseResponse> deleteFavoritesSong(@Path("id") String id);

    /**
     * 修改歌单名称
     */
    @FormUrlEncoded
    @PATCH("api/v1/favorites/")
    Flowable<BaseResponse> modifyFavoriteName(@Field("id") String favoriteId,
                                              @Field("name") String name);

    /**
     * 点赞评论
     */
    @FormUrlEncoded
    @POST("api/v1/musiccomment/upvote")
    Flowable<BaseResponse> thumbUpComment(@Field("commentId") String commentId);

    /**
     * 点赞评论
     */
    @FormUrlEncoded
    @POST("api/v1/musiccomment/cancelVote")
    Flowable<BaseResponse> cancelThumbUpComment(@Field("commentId") String commentId);

    /**
     * 获取评论列表
     */
    @GET("api/v1/musiccomment")
    Flowable<CommentResult> getComments(@Query("mid") String mid, @Query("sort") String sortord,
                                        @Query("sid") String storeId, @Query("page") int page,
                                        @Query("size") int size);

    /**
     * 发表评论
     */
    @POST("api/v1/musiccomment")
    Flowable<BaseResponse> postCommentRequest(@Body CommentRequest comment);

    @GET("api/v1/mod/mywaitingcout")
    Flowable<BaseResponse> getCount(@Query("storeId") String storeId);

    /**
     * 获取所有店铺
     */
    @GET("api/v1/store")
    Flowable<ApiResponse<StoreDataBean>> getStores(@Query("ol") boolean isOnline, @Query("page") int page,
                                                   @Query("size") int size, @Query("sort") String sort);

    /**
     * 获取店铺的默认播放列表
     */
    @GET("api/v1/storeplaylist/{id}")
    Flowable<BaseResponse> getPlaylist(@Path("id") String storeId);

    /**
     * 点播歌曲
     */
    @POST("api/v1/mod")
    Flowable<BaseResponse> addToPlaylist(@Body Music music);

    /**
     * 获取点赞信息
     */
    @GET("api/v1/storemusic/{id}/stat")
    Flowable<StoreVoteResponse> getVoteInfo(@Path("id") String mediaId,
                                            @Query("sid") String storeId);

    /**
     * 歌曲点赞
     */
    @POST("api/v1/storemusic/upvote")
    Flowable<BaseResponse> thumbupSong(@Body ThumbUpSongRequest thumbUpSongRequest);

    /**
     * 取消歌曲点赞
     */
    @FormUrlEncoded
    @PATCH("api/v1/storemusic/cancelVote")
    Flowable<BaseResponse> cancelThumbUpSong(@Field("storeId") String storeId,
                                             @Field("mediaId") String mediaId);

    @GET("api/v1/store/{id}/online")
    Flowable<ApiResponse> getStoreStatus(@Path("id") String storeId);

    /**
     * 获取店铺的默认播放列表
     */
    @GET("api/v1/storeplaylist/{id}")
    Flowable<ApiResponse<List<StoreSong>>> getStorePlaylist(@Path("id") String storeId);

    @GET("api/v1/store/{id}")
    Flowable<ApiResponse<Store>> getStore(@Path("id") String id);

    /**
     * 反馈
     */
    @POST("api/v1/feedback")
    Flowable<ApiResponse> postFeedback(@Field("title") String title, @Field("content") String content);
}
