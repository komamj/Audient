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
package com.xinshang.store.data.source.local;

import com.xinshang.store.data.entities.CommandResponse;

import io.reactivex.Flowable;

/**
 * Created by koma on 3/6/18.
 */

public interface ILocalDataSource {
    void persistenceUserName(String userName);

    String getUserName();

    void persistenceUserPassword(String password);

    String getUserPassword();

    void persistenceLoginStatus(boolean loginStatus);

    boolean getLoginStatus();

    void persistenceAccessToken(String accessToken);

    String getAccessToken();

    void persistenceRefreshToken(String refreshToken);

    String getRefreshToken();

    void persistenceStoreId(String storeId);

    String getStoreId();

    Flowable<CommandResponse<String>> parsingCommandResponse(String response);
}
