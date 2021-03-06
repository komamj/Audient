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
package com.xinshang.audient.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.xinshang.audient.model.entities.Store;
import com.xinshang.audient.model.entities.User;
import com.xinshang.common.base.BasePresenter;
import com.xinshang.common.base.BaseView;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        void showUserInfo(User user);

        void showBlurBackground(Drawable drawable);

        void showStoreInfo(Store store);

        void showCoupons(int count);
    }

    interface Presenter extends BasePresenter {
        void loadMyCoupons();

        void loadUserInfo();

        void blurBitmap(Bitmap bitmap, Context context, int inSampleSize);

        void loadStoreInfo();

        String getMyShareCode();
    }
}
