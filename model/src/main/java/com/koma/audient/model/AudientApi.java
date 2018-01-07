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

import com.koma.audient.model.entities.SearchResult;
import com.koma.audient.model.entities.TopList;
import com.koma.audient.model.entities.TopSong;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by koma on 1/5/18.
 */

public interface AudientApi {
    @GET("content/contentbillboardservice/querybillboardlist")
    Flowable<TopList> getTopLists(@Query("app_id") String appID,
                                  @Query("access_token") String acessToken,
                                  @Query("timestamp") String timeStamp);

    @GET("content/contentbillboardservice/querycontentbillboard")
    Flowable<TopSong> getTopSongs(@Query("billboard_id") String billboardId,
                                  @Query("count") int count, @Query("page") int page,
                                  @Query("app_id") String appID,
                                  @Query("access_token") String acessToken,
                                  @Query("timestamp") String timeStamp);

    @GET("searchmusic/search/searchmusic")
    Flowable<SearchResult> getMusics(@Query("key_word") String keyWord, @Query("type") String musicType,
                                     @Query("count") int count, @Query("page") int page,
                                     @Query("app_id") String appID, @Query("access_token") String acessToken,
                                     @Query("timestamp") String timeStamp);
}
