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

import java.util.List;

/**
 * Created by koma on 3/28/18.
 */

public class Toplist {
    @SerializedName("id")
    public int id;
    @SerializedName("key")
    public String key;
    @SerializedName("name")
    public String name;
    @SerializedName("title")
    public String title;
    @SerializedName("listenNum")
    public int listenNum;
    @SerializedName("coverImage")
    public String coverImage;
    @SerializedName("preItems")
    public List<ToplistSong> toplistSongs;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Toplist topList = (Toplist) o;

        return this.id == topList.id && TextUtils.equals(this.name, topList.name)
                && TextUtils.equals(this.coverImage, topList.coverImage)
                && TextUtils.equals(this.key, topList.key)
                && TextUtils.equals(this.title, topList.title)
                && this.listenNum == topList.listenNum;
    }
}
