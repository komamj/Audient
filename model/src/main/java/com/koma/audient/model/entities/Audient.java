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

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "audient")
public class Audient {
    @Ignore
    @SerializedName("album")
    public Album album;
    @NonNull
    @SerializedName("mid")
    @PrimaryKey
    @ColumnInfo(name = "mid")
    public String mid;
    @SerializedName("name")
    public String musicName;
    @SerializedName("interval")
    public long duration;
    @Ignore
    @SerializedName("singer")
    public List<Singer> singer;

    public Audient() {
    }

    public static class Album {
        @SerializedName("mid")
        public String albumId;

        public void setAlbumId(String albumId) {
            this.albumId = albumId;
        }

        public String getAlbumId() {
            return this.albumId;
        }
    }

    public static class Singer {
        @SerializedName("name")
        public String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    @Override
    public String toString() {
        return "Audient with mid " + mid + ",musicName " + musicName + ",singerName "
                + singer.get(0).name + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Audient audient = (Audient) o;

        return this.mid.equals(audient.mid) && TextUtils.equals(musicName, audient.musicName);
    }
}
