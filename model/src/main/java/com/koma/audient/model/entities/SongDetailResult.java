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

public class SongDetailResult {
    /**
     * {"code":0,"data":[{"action":{"alert":100002,"icons":8060,"msgdown":0,"msgfav":0,"msgid":14,"msgpay":6,"msgshare":0,"switch":636675},"album":{"id":65149,"mid":"004T759T1Tetzx","name":"像我这样的朋友","subtitle":"《至尊无上》电影主题曲","time_public":"1989-09-25","title":"像我这样的朋友"},"bpm":0,"data_type":0,"file":{"media_mid":"000M3RJ63KGlnf","size_128mp3":5263570,"size_192aac":7883682,"size_192ogg":7173278,"size_24aac":1014746,"size_320mp3":13158610,"size_48aac":1994699,"size_96aac":3989564,"size_ape":0,"size_dts":0,"size_flac":0,"size_try":0,"try_begin":0,"try_end":0},"fnote":4009,"genre":1,"id":772637,"index_album":1,"index_cd":0,"interval":328,"isonly":1,"ksong":{"id":1101,"mid":"002Sxtpo4LVyq3"},"label":"4611686153718857729","language":0,"mid":"002Ulqxf44DAKg","modify_stamp":0,"mv":{"id":0,"name":"","title":"","vid":""},"name":"像我这样的朋友","pay":{"pay_down":1,"pay_month":1,"pay_play":0,"pay_status":0,"price_album":0,"price_track":200,"time_free":0},"singer":[{"id":149,"mid":"0040D7gK4aI54k","name":"谭咏麟","title":"谭咏麟","type":0,"uin":0}],"status":0,"subtitle":"《至尊无上》电影主题曲","time_public":"1989-09-25","title":"像我这样的朋友","trace":"","type":0,"url":"http://stream9.qqmusic.qq.com/12772637.wma","version":0,"volume":{"gain":0.541,"lra":9.111,"peak":0.984}}],"url":{"772637":"ws.stream.qqmusic.qq.com/C100002Ulqxf44DAKg.m4a?fromtag=38"},"url1":{},"extra_data":[],"joox":0,"joox_login":0,"msgid":0}
     */
    @SerializedName("data")
    public List<Audient> audients;
}
