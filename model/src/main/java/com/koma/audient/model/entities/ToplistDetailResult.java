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

public class ToplistDetailResult {
    @SerializedName("songlist")
    public List<ToplistDetail> toplistDetails;

    public static class ToplistDetail {
        @SerializedName("data")
        public DataBean dataBean;

        public static class DataBean {
            @SerializedName("songmid")
            public String id;
            @SerializedName("songname")
            public String name;
            @SerializedName("albummid")
            public String albumId;
            @SerializedName("albumname")
            public String albumName;
            @SerializedName("singer")
            public Singer singer;
        }
    }
}
