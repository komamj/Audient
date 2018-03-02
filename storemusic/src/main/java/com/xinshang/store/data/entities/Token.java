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
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "token")
public class Token {
    @ColumnInfo(name = "access_token")
    @SerializedName("access_token")
    public String accessToken;
    @ColumnInfo(name = "token_type")
    @SerializedName("token_type")
    public String tokenType;
    @ColumnInfo(name = "refresh_token")
    @SerializedName("refresh_token")
    public String refreshToken;
    @ColumnInfo(name = "expires_in")
    @SerializedName("expires_in")
    public String expiresIn;
    @ColumnInfo(name = "scope")
    @SerializedName("scope")
    public String scope;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("token with accessToken ");
        builder.append(accessToken);
        builder.append(",tokenType ");
        builder.append(tokenType);
        builder.append(",refreshToken ");
        builder.append(refreshToken);
        builder.append(",expiresIn ");
        builder.append(expiresIn);
        builder.append(",scope ");
        builder.append(scope);
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

        Token token = (Token) o;

        return TextUtils.equals(this.accessToken, token.accessToken);
    }
}
