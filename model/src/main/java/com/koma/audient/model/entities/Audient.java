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
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "audient")
public class Audient {
    @NonNull
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "name")
    @SerializedName("name")
    public String name;
    @ColumnInfo(name = "duration")
    @SerializedName("interval")
    public long duration;
    @Embedded
    @SerializedName("artist")
    public Artist artist;
    @Embedded
    @SerializedName("album")
    public Album album;

    public Audient() {
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Audient with id ");
        builder.append(id);
        builder.append(",name ");
        builder.append(name);
        builder.append(",artist ");
        builder.append(artist.toString());
        builder.append(",album ");
        builder.append(album.toString());
        return builder.toString();
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

        return TextUtils.equals(this.id, audient.id) && this.duration == audient.duration
                && TextUtils.equals(name, audient.name) && this.artist.equals(audient.artist)
                && this.album.equals(audient.album);
    }
}
