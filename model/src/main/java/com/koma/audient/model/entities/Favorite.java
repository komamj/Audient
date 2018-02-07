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

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class Favorite {
    @SerializedName("id")
    public String favoriteId;
    @SerializedName("name")
    public String favoriteName;
    @SerializedName("def")
    public boolean isDefault;
    @SerializedName("userId")
    public String userId;
    @SerializedName("itemCount")
    public int itemCount;
    @SerializedName("createDate")
    public String createDate;
    @SerializedName("modifyDate")
    public String modifyDate;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("favorite with favoriteId ");
        builder.append(favoriteId);
        builder.append(",favoriteName ");
        builder.append(favoriteName);
        builder.append(",userId ");
        builder.append(userId);
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

        return TextUtils.equals(this.favoriteId, favorite.favoriteId)
                && TextUtils.equals(userId, favorite.userId)
                && TextUtils.equals(this.favoriteName, favorite.favoriteName)
                && TextUtils.equals(this.createDate, favorite.createDate)
                && TextUtils.equals(this.modifyDate, favorite.modifyDate);
    }
}
