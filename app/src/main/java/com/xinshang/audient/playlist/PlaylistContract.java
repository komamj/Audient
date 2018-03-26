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
package com.xinshang.audient.playlist;

import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.StoreSong;
import com.xinshang.common.base.BasePresenter;
import com.xinshang.common.base.BaseView;

import java.util.List;

public interface PlaylistContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void showLoadingError();

        void showEmpty(boolean forceShow);

        void setLoadingIndicator(boolean isActive);

        void showNowPlaying(StoreSong storePlaylist);

        void showPlaylist(List<StoreSong> storePlaylists);
    }

    interface Presenter extends BasePresenter {
        void loadNowPlaying(String id);

        void loadStorePlaylist();

        void thumbUpSong(Audient audient);

        void sendCommand(String command);

        void onCommandResponse(String message);
    }
}
