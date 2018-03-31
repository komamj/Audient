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
package com.xinshang.store.data;

import com.xinshang.store.data.entities.ApiResponse;
import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.CommentDataBean;
import com.xinshang.store.data.entities.FavoriteListResult;
import com.xinshang.store.data.entities.FavoritesResult;
import com.xinshang.store.data.entities.FileResult;
import com.xinshang.store.data.entities.LyricResult;
import com.xinshang.store.data.entities.Music;
import com.xinshang.store.data.entities.NowPlayingResponse;
import com.xinshang.store.data.entities.SearchResult;
import com.xinshang.store.data.entities.SongDetailResult;
import com.xinshang.store.data.entities.Store;
import com.xinshang.store.data.entities.StoreKeeperResponse;
import com.xinshang.store.data.entities.StorePlaylist;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.data.entities.Token;
import com.xinshang.store.data.entities.ToplistDataBean;
import com.xinshang.store.data.entities.ToplistDetailResult;

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
    Flowable<ApiResponse<List<ToplistDataBean>>> getToplists();

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

    @GET("api/v1/me")
    Flowable<StoreKeeperResponse> getStoreKeeperInfo();

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
     * 获取门店正在播放的歌曲信息
     */
    @GET("api/v1/nowplaying")
    Flowable<NowPlayingResponse> getNowPlaying(String storeId);

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
    Flowable<BaseResponse> addToFavorite(@Path("id") String favoriteId,
                                         @Body TencentMusic audient);

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
    Flowable<BaseResponse> deleteFavoritesSong(@Path("id") String favoritesId);

    /**
     * 修改歌单名称
     */
    @FormUrlEncoded
    @PATCH("api/v1/favorites/")
    Flowable<BaseResponse> modifyFavoriteName(@Field("id") String favoriteId,
                                              @Field("name") String name);

    /**
     * 获取评论列表
     */
    @GET("api/v1/musiccomment")
    Flowable<ApiResponse<CommentDataBean>> getComments(@Query("mid") String mid, @Query("sort") String sortord,
                                                       @Query("sid") String storeId, @Query("page") int page,
                                                       @Query("size") int size);

    /**
     * 获取店铺的默认播放列表
     */
    @GET("api/v1/storeplaylist/{id}")
    Flowable<ApiResponse<List<StorePlaylist>>> getStorePlaylist(@Path("id") String storeId);

    /**
     * 移除播放列表
     */
    @DELETE("api/v1/storeplaylist/{id}")
    Flowable<BaseResponse> deleteSongFromPlaylist(@Path("id") String id);

    /**
     * 点播歌曲
     */
    @POST("api/v1/storeplaylist/")
    Flowable<BaseResponse> addToPlaylist(@Body Music music);

    @GET("api/v1/store/my")
    Flowable<ApiResponse<List<Store>>> getStoreInfo();

    /**
     * 播放整个歌单
     */
    @FormUrlEncoded
    @POST("api/v1/storeplaylist/{id}/playfav")
    Flowable<BaseResponse> addAllToPlaylist(@Path("id") String storeId,
                                            @Field("favoritesId") String favoritesId);
}
