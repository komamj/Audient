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

import java.io.Serializable;
import java.util.List;

/**
 * Created by koma on 1/3/18.
 */

public class SearchResult implements Serializable{
    private static final long serialVersionUID = 7523967970034938901L;
    @SerializedName("searchSongDataResponse")
    public SearchSongDataResponse searchSongDataResponse;

    static class SearchSongDataResponse {
        @SerializedName("musicFileItemLists")
        public List<MusicFileItem> musicFileItems;
        @SerializedName("count")
        public int count;
    }

    static class MusicFileItem {
        @SerializedName("id")
        public int contentId;
        @SerializedName("music_name")
        public String musicName;
        @SerializedName("actor_name")
        public String actorName;
    }
}
