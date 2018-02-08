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
package com.koma.audient.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavoriteListResult {
    @SerializedName("code")
    public int resultCode;
    @SerializedName("data")
    public List<Audient> audients;
    @SerializedName("message")
    public String message;

    public static class DataBean {
        @SerializedName("id")
        public String id;
        @SerializedName("favoriteId")
        public String favoriteId;
        @SerializedName("mediaSource")
        public int mediaSource;
        @SerializedName("mediaId")
        public String mediaId;
        @SerializedName("mediaName")
        public String mediaName;
        @SerializedName("mediaInterval")
        public int mediaInterval;
        @SerializedName("artistId")
        public String artistId;
        @SerializedName("artistName")
        public String artistName;
        @SerializedName("albumId")
        public String albumId;
        @SerializedName("albumName")
        public String albumName;
        @SerializedName("orderNum")
        public String orderNum;
        @SerializedName("addDate")
        public String addDate;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("response with resultCode ");
        builder.append(resultCode);
        builder.append(",data ");
        builder.append(audients.toString());
        builder.append(",message ");
        builder.append(message);
        return builder.toString();
    }
}
