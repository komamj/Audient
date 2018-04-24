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
package com.xinshang.audient.splash;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.xinshang.audient.model.entities.Store;
import com.xinshang.common.base.BasePresenter;
import com.xinshang.common.base.BaseView;

import java.util.List;

/**
 * Created by koma on 3/5/18.
 */

public interface SplashContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void showMainView();

        void showLoginButton(boolean forceShow);

        void showLoginDialog();

        void showStoresUI(boolean forceShow);

        void showStores(List<Store> stores);

        void showSuccessfulMessage();

        void showLoadingError();

        void setLoadingIndicator(boolean isActive);
    }

    interface Presenter extends BasePresenter {
        void delayLaunchMainView();

        void sendLoginRequest();

        void loadAccessToken(String code);

        void processWXResponse(BaseResp response);

        void loadStores();

        void persistenceStore(Store store);

        void checkVersion();
    }
}
