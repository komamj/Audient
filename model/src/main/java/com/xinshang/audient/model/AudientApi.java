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
import com.xinshang.audient.model.entities.CommentRequest;
import com.xinshang.audient.model.entities.CommentResult;
import com.xinshang.audient.model.entities.FavoriteListResult;
import com.xinshang.audient.model.entities.FavoritesResult;
import com.xinshang.audient.model.entities.FileResult;
import com.xinshang.audient.model.entities.LyricResult;
import com.xinshang.audient.model.entities.Music;
import com.xinshang.audient.model.entities.NowPlayingResult;
import com.xinshang.audient.model.entities.SearchResult;
import com.xinshang.audient.model.entities.SongDetailResult;
import com.xinshang.audient.model.entities.StoreResponse;
import com.xinshang.audient.model.entities.Token;
import com.xinshang.audient.model.entities.ToplistDetailResult;
import com.xinshang.audient.model.entities.ToplistResult;
import com.xinshang.audient.model.entities.User;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.Call;
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
    Flowable<List<ToplistResult>> getTopLists();

    /**
     * 获取榜单详情
     */
    @GET("api/v1/openmusic/toplist/{mediaId}")
    Flowable<ToplistDetailResult> getToplistDetail(@Path("mediaId") int topId,
                                                   @Query("date") String updateKey);

    /**
     * 获取搜索结果.
     *
     * @param keyword 关键字.
     */
    @GET("api/v1/openmusic/search")
    Flowable<SearchResult> getSeachResults(@Query("w") String keyword);

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

    @GET("nowplaying")
    Flowable<NowPlayingResult> getNowPlayingResult();

    /**
     * 登录
     */
    @POST("account/register/user")
    Flowable<BaseResponse> getLoginResult(@Body User user);

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
    Call<Token> getToken(@Field("username") String userName,
                         @Field("password") String password,
                         @Field("grant_type") String grantType,
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
     * 删除歌单里的歌曲
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
     * 获取评论列表
     */
    @GET("api/v1/musiccomment")
    Flowable<CommentResult> getComments(@Query("mid") String mid, @Query("page") int page,
                                        @Query("size") int size,
                                        @Query("sort") String sortord);

    /**
     * 发表评论
     */
    @POST("api/v1/musiccomment")
    Flowable<BaseResponse> postCommentRequest(@Body CommentRequest comment);

    /**
     * 点播歌曲
     */
    @POST("api/v1/mod")
    Flowable<BaseResponse> addToPlaylist(@Body Music music);

    @GET("api/v1/mod/mywaitingcout")
    Flowable<BaseResponse> getCount(@Query("storeId") String storeId);

    /**
     * 获取所有店铺
     */
    @GET("store")
    Flowable<StoreResponse> getStores(@Query("ol") String ol, @Query("page") int page,
                                      @Query("size") int size, @Query("sort") String sort);

    /**
     * 获取店铺的默认播放列表
     */
    @GET("api/v1/storeplaylist")
    Flowable<BaseResponse> getPlaylist(@Query("sid") String storeId);
}
