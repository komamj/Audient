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
package com.xinshang.store.mine;

import com.xinshang.store.base.BasePresenter;
import com.xinshang.store.base.BaseView;
import com.xinshang.store.data.entities.Favorite;
import com.xinshang.store.data.entities.TencentMusic;

import java.util.List;

public interface MineContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void showLoadingError();

        void showEmpty(boolean forceShow);

        void showFavoriteProgressBar(boolean forceShow);

        void showUserProgressBar(boolean forceShow);

        void showFavorites(List<Favorite> favorites);

        void showDynamics(List<TencentMusic> audients);
    }

    interface Presenter extends BasePresenter {
        void loadFavorites();

        void loadDynamics();

        void deleteMyFavorite(Favorite favorite);
    }
}
