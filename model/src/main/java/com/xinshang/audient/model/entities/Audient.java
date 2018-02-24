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
package com.xinshang.audient.model.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "audient")
public class Audient implements Parcelable {
    @NonNull
    @SerializedName("mediaId")
    @PrimaryKey
    @ColumnInfo(name = "mediaId")
    public String mediaId;
    @ColumnInfo(name = "mediaName")
    @SerializedName("mediaName")
    public String mediaName;
    @ColumnInfo(name = "duration")
    @SerializedName("mediaInterval")
    public long duration;
    @ColumnInfo(name = "title")
    @SerializedName("title")
    public String title;

    @ColumnInfo(name = "artist_id")
    @SerializedName("artistId")
    public String artistId;
    @ColumnInfo(name = "artist_name")
    @SerializedName("artistName")
    public String artistName;

    @ColumnInfo(name = "album_id")
    @SerializedName("albumId")
    public String albumId;
    @ColumnInfo(name = "album_name")
    @SerializedName("albumName")
    public String albumName;


    public Audient() {
    }

    protected Audient(Parcel in) {
        mediaId = in.readString();
        mediaName = in.readString();
        duration = in.readLong();
        title = in.readString();
        artistId = in.readString();
        artistName = in.readString();
        albumId = in.readString();
        albumName = in.readString();
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
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("audient with mediaId ");
        builder.append(mediaId);
        builder.append(",mediaName ");
        builder.append(mediaName);
        builder.append(",artistId ");
        builder.append(artistId);
        builder.append(",album ");
        builder.append(albumId);
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

        return TextUtils.equals(this.mediaId, audient.mediaId) && this.duration == audient.duration
                && TextUtils.equals(this.title, audient.title)
                && TextUtils.equals(mediaName, audient.mediaName)
                && TextUtils.equals(this.artistId, audient.artistId)
                && TextUtils.equals(this.artistName, audient.artistName)
                && TextUtils.equals(this.albumId, audient.albumId)
                && TextUtils.equals(this.albumName, audient.albumName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mediaId);
        parcel.writeString(mediaName);
        parcel.writeLong(duration);
        parcel.writeString(title);
        parcel.writeString(artistId);
        parcel.writeString(artistName);
        parcel.writeString(albumId);
        parcel.writeString(albumName);
    }
}
