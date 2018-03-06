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
package com.xinshang.audient.model.source;

import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.BaseResponse;
import com.xinshang.audient.model.entities.User;

import java.util.List;

import io.reactivex.Flowable;

public interface AudientDataSource {
    Flowable<List<Audient>> getAudientTests();

    Flowable<Boolean> getLoginStatus();

    Flowable<BaseResponse> getLoginResult(User user);

    Flowable<Boolean> setLoginStatus(boolean loginStatus);
}
