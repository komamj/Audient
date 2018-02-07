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
package com.koma.common.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Constants {
    public static final String AUDIENT_HOST = "http://101.132.122.196:8080/";
    public static final String CLIENT_ID = "USER_CLIENT";
    public static final String CLIENT_SECRET = "187897f409c644d4809eb4fee2658a1e";
    public static final String GRANT_TYPE = "password";

    public static final String KEY_AUDIENT = "key_auident";

    // album format
    public static final String ALBUM_FORMAT = "jpg";

    // audition dialog
    public static final String AUDITION_TAG = "audition_tag";

    public static final int UNKNOWN = 0;
    public static final int PLAYING = 1;
    public static final int PAUSED = 2;
    public static final int COMPLETED = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({UNKNOWN, PLAYING, PAUSED, COMPLETED})
    public @interface PlayState {
    }

    // toplist detail
    public static final String KEY_TOP_ID = "key_top_id";
    public static final String KEY_top_list_name = "key_top_list_name";
    public static final String KEY_UPDATE = "key_update";
    public static final String KEY_PIC_URL = "key_pic_url";

    // response
    public static final String RESPONSE_RES_CODE = "res_code";
    public static final String RESPONSE_RES_MESSAGE = "res_message";

    // comment
    public static final String KEY_NAME = "name";

    private Constants() {
    }
}
