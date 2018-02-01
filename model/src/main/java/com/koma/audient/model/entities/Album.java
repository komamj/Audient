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
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class Album implements Parcelable {
    @ColumnInfo(name = "album_id")
    @SerializedName("id")
    public String id;
    @ColumnInfo(name = "album_name")
    @SerializedName("name")
    public String name;

    public Album() {
    }

    @Ignore
    public Album(String id, String name) {
        this.id = id;

        this.name = name;
    }

    @Ignore
    protected Album(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("album with id :");
        builder.append(id);
        builder.append(",name :");
        builder.append(name);
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

        Album album = (Album) o;

        return TextUtils.equals(this.id, album.id) && TextUtils.equals(this.name, album.name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
    }
}
