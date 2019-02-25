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

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

@Entity(tableName = "song")
public class Song implements Parcelable {
    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
    @SuppressWarnings("NullableProblems")
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

    public Song() {
    }

    protected Song(Parcel in) {
        mediaId = in.readString();
        mediaName = in.readString();
        duration = in.readLong();
        title = in.readString();
        artistId = in.readString();
        artistName = in.readString();
        albumId = in.readString();
        albumName = in.readString();
    }

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

        Song song = (Song) o;

        return TextUtils.equals(this.mediaId, song.mediaId) && this.duration == song.duration
                && TextUtils.equals(this.title, song.title)
                && TextUtils.equals(mediaName, song.mediaName)
                && TextUtils.equals(this.artistId, song.artistId)
                && TextUtils.equals(this.artistName, song.artistName)
                && TextUtils.equals(this.albumId, song.albumId)
                && TextUtils.equals(this.albumName, song.albumName);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{mediaId, mediaName, artistId, artistName, albumId,
                albumName});
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
