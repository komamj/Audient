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

import android.text.TextUtils;

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("audient with mediaId ");
        builder.append(mediaId);
        builder.append(",mediaName ");
        builder.append(mediaName);
        builder.append(",artistId ");
        builder.append(artistId);
        builder.append(",album ");
        builder.append(albumId);
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StorePlaylist storePlaylist = (StorePlaylist) o;

        return TextUtils.equals(id, storePlaylist.id)
                && TextUtils.equals(storeId, storePlaylist.storeId)
                && TextUtils.equals(userId, storePlaylist.userId)
                && TextUtils.equals(demandId, storePlaylist.demandId)
                && TextUtils.equals(demandTime, storePlaylist.demandTime)
                && TextUtils.equals(mediaSource, storePlaylist.mediaSource)
                && TextUtils.equals(this.mediaId, storePlaylist.mediaId)
                && TextUtils.equals(mediaInterval, storePlaylist.mediaInterval)
                && TextUtils.equals(this.mediaName, storePlaylist.mediaName)
                && TextUtils.equals(this.artistId, storePlaylist.artistId)
                && TextUtils.equals(this.artistName, storePlaylist.artistName)
                && TextUtils.equals(this.albumId, storePlaylist.albumId)
                && TextUtils.equals(this.albumName, storePlaylist.albumName)
                && TextUtils.equals(joinedDate, storePlaylist.joinedDate);
    }
}
