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

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by koma_20 on 2018/3/17.
 */

public class CommandResponse<T> {
    @SerializedName("code")
    public int code;
    @SerializedName("store")
    public String store;
    @SerializedName("action")
    public String action;
    @SerializedName("data")
    public T data;
    @SerializedName("message")
    public String message;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("command response with code ");
        builder.append(code);
        builder.append(",store ");
        builder.append(store);
        builder.append(",action ");
        builder.append(action);
        builder.append(",message ");
        builder.append(message);
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

        CommandResponse commandResponse = (CommandResponse) o;

        return this.code == commandResponse.code
                && TextUtils.equals(message, commandResponse.message)
                && TextUtils.equals(action, commandResponse.action)
                && TextUtils.equals(store, commandResponse.store);
    }
}
