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

import com.koma.audient.model.entities.FileResult;
import com.koma.audient.model.entities.LyricResult;
import com.koma.audient.model.entities.SearchResult;
import com.koma.audient.model.entities.SongDetailResult;
import com.koma.audient.model.entities.TopListResult;
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
     * 获取搜索结果.
     *
     * @param keyword 关键字.
     */
    @GET("search")
    Flowable<SearchResult> getSeachResults(@Query("w") String keyword);

    /**
     * 获取歌词.
     *
     * @param id
     */
    @GET("{id}/lyric")
    Flowable<LyricResult> getLyricResult(@Path("id") String id);

    @GET("{id}")
    Flowable<SongDetailResult> getSongDetailResult(@Path("id") String id);

    @GET("{id}/url")
    Flowable<FileResult> getFileResult(@Path("id") String id);
}
