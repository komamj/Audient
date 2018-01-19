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

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopListResult {
    @SerializedName("List")
    public List<TopList> topLists;

    /**
     * {"ListName":"安利XS·巅峰榜·流行指数","MacDetailPicUrl":"http://y.gtimg.cn/music/common/upload/t_order_channel_hitlist_conf/64072.png","MacListPicUrl":"http://y.gtimg.cn/music/common/upload/t_order_channel_hitlist_conf/64073.png","headPic_v12":"http://y.gtimg.cn/music/common/upload/t_order_channel_hitlist_conf/139426.jpg","listennum":19700000,"pic":"http://y.gtimg.cn/music/common/upload/t_order_channel_hitlist_conf/64070.png","pic_Detail":"http://y.gtimg.cn/music/common/upload/t_order_channel_hitlist_conf/139424.jpg","pic_h5":"http://y.gtimg.cn/music/common/upload/t_order_channel_hitlist_conf/64065.png","pic_v11":"http://y.gtimg.cn/music/common/upload/t_order_channel_hitlist_conf/64071.png","pic_v12":"http://y.gtimg.cn/music/common/upload/iphone_order_channel/toplist_4_300_212877900.jpg","showtime":"2018-01-18","songlist":[{"singerid":4558,"singername":"周杰伦","songid":212877900,"songname":"等你下课(with 杨瑞代)"},{"singerid":1121441,"singername":"GAI","songid":212846372,"songname":"沧海一声笑 (Live)"},{"singerid":1121441,"singername":"GAI/双笙","songid":212846661,"songname":"都市惊奇夜"},{"singerid":4286,"singername":"林俊杰","songid":212846879,"songname":"我们的爱 (Live)"}],"topID":4,"type":0,"update_key":"2018-01-18"}
     */
    public static class TopList {
        @SerializedName("ListName")
        public String listName;
        @SerializedName("pic_v12")
        public String picUrl;
        @SerializedName("listennum")
        public long listenedCount;
        @SerializedName("songlist")
        public List<SongBean> songBeans;
        @SerializedName("topid")
        public int topId;
        @SerializedName("update_key")
        public String updateTime;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TopList topList = (TopList) o;

            return this.topId == topList.topId && TextUtils.equals(listName, topList.listName)
                    && TextUtils.equals(picUrl, topList.picUrl);
        }

        public static class SongBean {
            @SerializedName("singername")
            public String actorName;
            @SerializedName("songid")
            public long songId;
            @SerializedName("songname")
            public String musicName;
        }

    }
}
