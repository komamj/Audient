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
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "audient")
public class Audient implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "content_id")
    public long contentId;
    @ColumnInfo(name = "music_name")
    public String musicName;
    @ColumnInfo(name = "actor_name")
    public String actorName;
    @ColumnInfo(name = "albumUrl")
    public String albumUrl;

    public Audient() {
    }

    protected Audient(Parcel in) {
        contentId = in.readLong();
        musicName = in.readString();
        actorName = in.readString();
        albumUrl = in.readString();
    }

    public static final Creator<Audient> CREATOR = new Creator<Audient>() {
        @Override
        public Audient createFromParcel(Parcel in) {
            return new Audient(in);
        }

        @Override
        public Audient[] newArray(int size) {
            return new Audient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(contentId);
        parcel.writeString(musicName);
        parcel.writeString(actorName);
        parcel.writeString(albumUrl);
    }
}
