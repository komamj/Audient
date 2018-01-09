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

public class SearchResult extends BaseResponse {
    /**
     * :{"res_message":"成功","res_code":0,"SearchSongDataJTResponse":{"musicFileItemLists":{"musicFileItem":[{"musicname":"海阔天空","actorname":"Robynn+Kendy","id":2747731},{"musicname":"海阔天空(免费版)","actorname":"周华健","id":3528217},{"musicname":"海阔天空","actorname":"林忆莲","id":2740420},{"musicname":"海阔天空(央视全球中文音乐榜上榜)","actorname":"信乐团","id":3534529},{"musicname":"海阔天空","actorname":"周华健","id":48475},{"musicname":"海阔天空(中国新声代3)","actorname":"汤晶锦","id":3561282},{"musicname":"海阔天空(蒙面唱将猜猜猜)","actorname":"尖耳朵的阿凡达妹妹","id":3601525},{"musicname":"海阔天空(央视星光璀璨新民歌演唱会)","actorname":"信","id":3543821},{"musicname":"海阔天空(最美和声)","actorname":"丁泽强+卢望","id":2739024},{"musicname":"海阔天空(2014最美和声)","actorname":"孙楠+九妹+祁夏竹","id":3144207}]}}}
     */
    @SerializedName("SearchSongDataJTResponse")
    public SearchSongDataResponse searchSongDataResponse;

    public static class SearchSongDataResponse {
        @SerializedName("musicFileItemLists")
        public MusicFielItemLists musicFielItemLists;
        @SerializedName("count")
        public int count;

        public static class MusicFielItemLists {
            @SerializedName("musicFileItem")
            public List<MusicFileItem> musics;
        }
    }

    @Override
    public String toString() {
        return "SearchResult  with resultCode " + resultCode + ",resultMessage :" + resultMessage
                + ",musicFileItemLists :" + searchSongDataResponse.count;
    }
}
