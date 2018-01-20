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

public class SearchResult {
    /**
     * code : 0
     * data : {"keyword":"四季","priority":0,"qc":[],"semantic":{"curnum":0,"curpage":1,"list":[],"totalnum":0},"song":{"curnum":28,"curpage":1,"list":[{"action":{"alert":100002,"icons":8060,"msg":16,"switch":17413891},"album":{"id":2106708,"id":"0011gDoK1KiXBI","name":"四季","subtitle":"","title":"四季","title_hilight":"<em>四季<\/em>"},"chinesesinger":0,"desc":"","desc_hilight":"","docid":"7562074659741560533","file":{"media_mid":"004GGiIn3318EZ","size_128":3918563,"size_320":9796114,"size_aac":5884183,"size_ape":0,"size_dts":0,"size_flac":0,"size_ogg":5221991,"size_try":0,"strMediaMid":"004GGiIn3318EZ","try_begin":0,"try_end":0},"fnote":4009,"genre":1,"grp":[],"id":108549992,"index_album":1,"index_cd":0,"interval":244,"isonly":1,"ksong":{"id":2363908,"id":"002rci8S4FnvhB"},"language":1,"lyric":"","lyric_hilight":"","id":"002ex7Ug3EZzVm","mv":{"id":0,"vid":""},"name":"四季","newStatus":2,"nt":10002,"pay":{"pay_down":1,"pay_month":1,"pay_play":0,"pay_status":0,"price_album":0,"price_track":200,"time_free":0},"pure":0,"singer":[{"id":143,"id":"003Nz2So3XXYek","name":"陈奕迅","title":"陈奕迅","title_hilight":"陈奕迅","type":0,"uin":0}],"status":0,"subtitle":"","t":1,"tag":10,"time_public":"2016-09-29","title":"四季","title_hilight":"<em>四季<\/em>","type":0,"url":"http://stream10.qqmusic.qq.com/108549992.wma","ver":0,"volume":{"gain":-4.238999843597412,"lra":9.206000328063965,"peak":0.9570000171661377}}],"totalnum":366},"taglist":[],"totaltime":0,"zhida":{"chinesesinger":0,"type":0}}
     * message :
     * notice :
     * subcode : 0
     * time : 1516279227
     * tips :
     */
    @SerializedName("data")
    public DataBean dataBean;

    public static class DataBean {
        @SerializedName("song")
        public SongBean songBean;

        public static class SongBean {
            @SerializedName("list")
            public List<Audient> audients;
        }
    }
}
