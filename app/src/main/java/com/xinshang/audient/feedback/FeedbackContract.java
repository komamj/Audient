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
package com.xinshang.audient.feedback;

import com.xinshang.common.base.BasePresenter;
import com.xinshang.common.base.BaseView;

/**
 * Created by koma on 3/28/18.
 */

public interface FeedbackContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void setLoadingIndicator(boolean isActive);

        void showSuccessfulMessage();

        void showFailedMessage();

        void showErrorTitle();

        void showErrorContent();
    }

    interface Presenter extends BasePresenter {
        void sendFeedback(String title, String content);
    }
}