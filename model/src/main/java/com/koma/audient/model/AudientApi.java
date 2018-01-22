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

import com.koma.audient.model.entities.LyricResult;
import com.koma.audient.model.entities.SearchResult;
import com.koma.audient.model.entities.SongDetailResult;
import com.koma.audient.model.entities.TopListResult;
import com.koma.audient.model.entities.TopSong;
import com.koma.audient.model.entities.ToplistDetailResult;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AudientApi {
    /**
     * 获取榜单列表.
     */
    @GET("toplist")
    Flowable<List<TopListResult>> getTopLists();

    /**
     * 获取榜单详情
     */
    @GET("toplist/{id}")
    Flowable<ToplistDetailResult> getToplistDetail(@Path("id") int topId, @Query("date") String showTime);

    /**
     * 获取对应榜单的歌曲.
     *
     * @param billboardId 榜单ID。通过【产品信息】.【获取榜单列表】接口获取“榜单id”.
     * @param count       单页返回记录数 默认为10
     * @param page        查询记录的开始数, 默认为 1. 例如总数为30的, 当查询开始数为1, count为10的时候,
     *                    返回的记录数为第1到第10条, 如果需要获得11-20的记录, 则可以将page设为11,
     *                    count为10的时候, 则返回的记录数为第11到第20条, 依此类推.
     * @param appID       应用ID
     * @param accessToken 访问令牌
     * @param timeStamp   时间戳，格式为：yyyy-MM-dd HH:mm:ss
     */
    @GET("content/contentbillboardservice/querycontentbillboard")
    Flowable<TopSong> getTopSongs(@Query("billboard_id") String billboardId,
                                  @Query("count") int count, @Query("page") int page,
                                  @Query("app_id") String appID,
                                  @Query("access_token") String accessToken,
                                  @Query("timestamp") String timeStamp);

    /**
     * 获取搜索结果.
     *
     * @param keyword 关键字.
     */
    @GET("search")
    Flowable<SearchResult> getSeachResults(@Query("w") String keyword);

    /**
     * 获取歌词.
     *
     * @param id          内容id.
     * @param idType      类型 0=歌曲ID，1=资源ID
     * @param musicName   歌曲名
     * @param actorName   歌手名
     * @param type        歌词类型 类型值分别为：txt、lrc
     * @param appID       应用ID
     * @param accessToken 访问令牌
     * @param timeStamp   时间戳，格式为：yyyy-MM-dd HH:mm:ss
     */
    @GET("lyric/lyric/querylyric")
    Flowable<LyricResult> getLyric(@Query("id") String id, @Query("id_type") String idType,
                                   @Query("music_name") String musicName,
                                   @Query("actor_name") String actorName,
                                   @Query("type") String type,
                                   @Query("app_id") String appID,
                                   @Query("access_token") String accessToken,
                                   @Query("timestamp") String timeStamp);

    @GET("{id}")
    Flowable<SongDetailResult> getSongDetailResult(@Path("id") String id);
}
