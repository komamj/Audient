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
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by koma_20 on 2018/3/5.
 */

@Entity(tableName = "store")
public class Store {
    @NonNull
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "name")
    @SerializedName("name")
    public String name;
    @ColumnInfo(name = "ownerId")
    @SerializedName("ownerId")
    public String ownerId;
    @ColumnInfo(name = "address")
    @SerializedName("address")
    public String address;
    @Embedded
    @SerializedName("location")
    public Location location;
    @ColumnInfo(name = "registedDate")
    @SerializedName("registedDate")
    public String registedDate;
    @ColumnInfo(name = "online")
    @SerializedName("online")
    public boolean online;
    @ColumnInfo(name = "lastOnlineDate")
    @SerializedName("lastOnlineDate")
    public String lastOnlineDate;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("store with id ");
        builder.append(id);
        builder.append(",name ");
        builder.append(name);
        builder.append(",address ");
        builder.append(address);
        builder.append(",registedDate ");
        builder.append(registedDate);
        builder.append(",online ");
        builder.append(online);
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

        Store store = (Store) o;

        return TextUtils.equals(this.id, store.id)
                && TextUtils.equals(this.name, store.name)
                && TextUtils.equals(this.address, store.address)
                && TextUtils.equals(this.ownerId,store.ownerId)
                && TextUtils.equals(this.registedDate,store.registedDate);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{id, name, address, ownerId, registedDate, online});
    }
}
