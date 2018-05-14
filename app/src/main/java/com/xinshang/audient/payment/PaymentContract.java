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
package com.xinshang.audient.payment;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.common.base.BasePresenter;
import com.xinshang.common.base.BaseView;

/**
 * Created by koma on 3/1/18.
 */

public interface PaymentContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void setLoadingIndicator(boolean isActive);

        void setFreeIndicator(boolean free);

        void showSuccessfullyMessage();

        void showFailedMessage();
    }

    interface Presenter extends BasePresenter {
        void loadMyCoupons(String type);

        void addToPlaylist(Audient audient);

        void postOrder(Audient audient);

        void processWXPayResponse(BaseResp response);

        void postOrderResult(String tid,String oid);
    }
}
