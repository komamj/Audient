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
package com.xinshang.audient.util;

import com.xinshang.common.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    private Utils() {
    }

    public static String buildUrl(String albumId) {
        StringBuilder builder = new StringBuilder(Constants.AUDIENT_HOST);
        builder.append("api/v1/openmusic/album/");
        builder.append(albumId);
        builder.append("/pic");

        return builder.toString();
    }

    public static String getTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStamp = simpleDateFormat.format(new Date());
        return timeStamp;
    }
}
