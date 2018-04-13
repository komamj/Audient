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

public class SongDetailResult {
    /**
     * {"data":{"mediaId":"002pKRoX4Qbafa","mediaName":"光辉岁月","interval":298,"album":{"mediaId":"001C2BRX15iE4B","mediaName":"命运派对"},"artist":{"mediaId":"002pUZT93gF4Cu","mediaName":"BEYOND"},"file":{"size_320mp3":11949657,"size_192ogg":6851084,"size_128mp3":4779985,"size_96aac":3614211,"size_48aac":1875054,"size_24aac":932555,"size_flac":30977024,"size_ape":30680488,"size_dts":0,"size_192acc":null,"media_mid":"002pKRoX4Qbafa"}},"code":0,"message":null}
     */
    @SerializedName("data")
    public Song audient;
}
