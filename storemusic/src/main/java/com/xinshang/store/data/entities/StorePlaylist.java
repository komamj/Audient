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
package com.xinshang.store.data.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by koma_20 on 2018/3/17.
 */

public class StorePlaylist {
    @SerializedName("id")
    public String id;
    @SerializedName("storeId")
    public String storeId;
    @SerializedName("userId")
    public String userId;
    @SerializedName("demandId")
    public String demandId;
    @SerializedName("demandTime")
    public String demandTime;
    @SerializedName("mediaSource")
    public String mediaSource;
    @SerializedName("mediaId")
    public String mediaId;
    @SerializedName("mediaName")
    public String mediaName;
    @SerializedName("mediaInterval")
    public String mediaInterval;
    @SerializedName("artistId")
    public String artistId;
    @SerializedName("artistName")
    public String artistName;
    @SerializedName("albumId")
    public String albumId;
    @SerializedName("albumName")
    public String albumName;
    @SerializedName("joinedDate")
    public String joinedDate;
}
