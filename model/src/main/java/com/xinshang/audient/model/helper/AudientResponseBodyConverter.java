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
package com.xinshang.audient.model.helper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static okhttp3.internal.Util.UTF_8;

public final class AudientResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;

    private final TypeAdapter<T> adapter;

    private final JsonParser jsonParser;

    AudientResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;

        this.adapter = adapter;

        jsonParser = new JsonParser();
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        LogUtils.i("", "convert response :" + response);
        JsonElement jsonElement = jsonParser.parse(response);
        String resCode = jsonElement.getAsJsonObject().get(Constants.RESPONSE_RES_CODE).getAsString();

        if (TextUtils.equals(resCode, "0")) {
            MediaType contentType = value.contentType();
            Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;

            InputStream inputStream = new ByteArrayInputStream(response.getBytes());
            Reader reader = new InputStreamReader(inputStream, charset);
            JsonReader jsonReader = gson.newJsonReader(reader);

            LogUtils.i("","------"+ jsonReader.toString());
            try {
                return adapter.read(jsonReader);
            } finally {
                value.close();
            }
        } else {
            value.close();
            throw new IOException("解析失败");
        }
    }
}
