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
package com.koma.audient.model;

import com.koma.audient.model.entities.BaseResponse;
import com.koma.audient.model.entities.FileResult;
import com.koma.audient.model.entities.LyricResult;
import com.koma.audient.model.entities.NowPlayingResult;
import com.koma.audient.model.entities.SearchResult;
import com.koma.audient.model.entities.SongDetailResult;
import com.koma.audient.model.entities.Token;
import com.koma.audient.model.entities.ToplistDetailResult;
import com.koma.audient.model.entities.ToplistResult;
import com.koma.audient.model.entities.User;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
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

    @POST("account/register/user")
    Flowable<BaseResponse> getLoginResult(@Body User user);

    @FormUrlEncoded
    @POST("oauth/token")
    Flowable<Token> getAccessToken(@Field("username") String userName, @Field("password") String password,
                                   @Field("grant_type") String grantType, @Field("client_id") String clientId,
                                   @Field("client_secret") String clientSecret);

    @POST("api/v1/favorites")
    Flowable<BaseResponse> getFavoriteResult(@Header("Authorization") String access_token,
                                             @Body String name);
}
