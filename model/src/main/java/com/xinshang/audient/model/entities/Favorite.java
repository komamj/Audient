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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class Favorite implements Parcelable {
    @SerializedName("id")
    public String favoritesId;
    @SerializedName("name")
    public String favoriteName;
    @SerializedName("def")
    public boolean isDefault;
    @SerializedName("userId")
    public String userId;
    @SerializedName("itemCount")
    public int itemCount;
    @SerializedName("coverImage")
    public String coverImageUrl;
    @SerializedName("createDate")
    public String createDate;
    @SerializedName("modifyDate")
    public String modifyDate;

    protected Favorite(Parcel in) {
        favoritesId = in.readString();
        favoriteName = in.readString();
        isDefault = in.readByte() != 0;
        userId = in.readString();
        itemCount = in.readInt();
        coverImageUrl = in.readString();
        createDate = in.readString();
        modifyDate = in.readString();
    }

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("favorite with favoritesId ");
        builder.append(favoritesId);
        builder.append(",favoriteName ");
        builder.append(favoriteName);
        builder.append(",userId ");
        builder.append(userId);
        builder.append(",coverImageUrl ");
        builder.append(coverImageUrl);
        builder.append(",createDate ");
        builder.append(createDate);
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

        Favorite favorite = (Favorite) o;

        return TextUtils.equals(this.favoritesId, favorite.favoritesId)
                && TextUtils.equals(userId, favorite.userId)
                && TextUtils.equals(this.favoriteName, favorite.favoriteName)
                && TextUtils.equals(this.createDate, favorite.createDate)
                && TextUtils.equals(this.modifyDate, favorite.modifyDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(favoritesId);
        parcel.writeString(favoriteName);
        parcel.writeByte((byte) (isDefault ? 1 : 0));
        parcel.writeString(userId);
        parcel.writeInt(itemCount);
        parcel.writeString(coverImageUrl);
        parcel.writeString(createDate);
        parcel.writeString(modifyDate);
    }

    @Keep
    public static class FavoritesSong {

        /**
         * favoritesId :
         * mediaId :
         * mediaName :
         * mediaInterval : 0
         * artistId :
         * artistName :
         * albumId :
         * albumName :
         */

        public String id;
        public String favoritesId;
        public String mediaId;
        public String mediaName;
        public int mediaInterval;
        public String artistId;
        public String artistName;
        public String albumId;
        public String albumName;
    }
}
