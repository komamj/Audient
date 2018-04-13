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
package com.xinshang.store.search;

import com.xinshang.store.base.BasePresenter;
import com.xinshang.store.base.BaseView;
import com.xinshang.store.data.entities.AlbumResponse;

import java.util.List;

/**
 * Created by koma on 4/13/18.
 */

public interface AlbumsContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void showLoadingError();

        void showEmpty(boolean forceShow);

        void showAlbums(List<AlbumResponse.Album> albums);

        void setLoadingIndictor(boolean isActive);
    }

    interface Presenter extends BasePresenter {
        void loadAlbums(String keyword);
    }
}
