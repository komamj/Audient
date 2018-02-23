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

import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @SerializedName("code")
    public int resultCode;
    @SerializedName("message")
    public String message;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("response with resultCode ");
        builder.append(resultCode);
        builder.append(",message ");
        builder.append(message);
        return builder.toString();
    }
}
