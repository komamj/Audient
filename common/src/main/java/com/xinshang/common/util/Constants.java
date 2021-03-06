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
package com.xinshang.common.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Constants {
    public static final String PLAYLIST_STATUS_HOST = "ws://193.112.0.111:80/ws/client";
    public static final String AUDIENT_HOST = "http://193.112.0.111:80/";
    public static final String CLIENT_ID = "USER_CLIENT";
    public static final String CLIENT_SECRET = "187897f409c644d4809eb4fee2658a1e";
    public static final String GRANT_TYPE = "password";
    public static final String STORE_ID = "store_id";

    public static final String LOGIN_STATUS = "login_status";

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "user_password";

    public static final String KEY_FIRST_DEMAND = "key_first_demand";
    public static final String KEY_MY_SHARE_CODE = "key_my_share_code";

    // wechat
    public static final String WECHAT_APP_ID = "wxc80eb13e9ed3e040";

    public static final int PAYLOAD_PLAYING = 0;

    // favorite
    public static final String KEY_FAVORITE = "key_favorite";

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

    // message
    public static final String MESSAGE_MY_FAVORITES_CHANGED = "messge_my_favorites_changed";
    public static final String MESSAGE_COMMENT_CHANGED = "message_comment_changed";
    public static final String MESSAGE_LOGIN_COMPLETED = "message_login_completed";

    private Constants() {
    }
}
