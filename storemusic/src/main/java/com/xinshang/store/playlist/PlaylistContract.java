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
package com.xinshang.store.playlist;

import com.xinshang.store.base.BasePresenter;
import com.xinshang.store.base.BaseView;
import com.xinshang.store.data.entities.StoreSong;

import java.util.List;

public interface PlaylistContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void setNextActive(boolean active);

        void setStopActive(boolean active);

        void setPauseActive(boolean active);

        void showLoadingError();

        void showEmpty(boolean forceShow);

        void setLoadingIndicator(boolean isActive);

        void showNowPlaying(StoreSong storePlaylist);

        void showPlaylist(List<StoreSong> storePlaylists);

        void updatePlayIcon(boolean isPlaying);
    }

    interface Presenter extends BasePresenter {
        void loadNowPlaying(String id);

        void loadStorePlaylist();

        void doPauseOrPlay();

        void stop();

        void next();

        void sendCommand(String command);

        boolean isPlaying();

        void deleteStoreSong(StoreSong storeSong, String reason);
    }
}
