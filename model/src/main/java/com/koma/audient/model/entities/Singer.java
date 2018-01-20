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

public class Singer {
    @SerializedName("mid")
    public String id;
    @SerializedName("name")
    public String name;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("singer has id :");
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

        Singer singer = (Singer) o;

        return TextUtils.equals(this.id, singer.id) && TextUtils.equals(this.name, singer.name);
    }
}
