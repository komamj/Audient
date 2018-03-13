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
package com.xinshang.audient.nowplaying;

import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.Lyric;
import com.xinshang.common.base.BasePresenter;
import com.xinshang.common.base.BaseView;

public interface NowPlayingContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        void showLoadingError();

        void showVoteInfo(boolean isVoted);

        void showEmpty(boolean forceShow);

        void showProgressBar(boolean forceShow);

        void showNowPlaying(Audient audient);

        void showLyric(Lyric lyric);
    }

    interface Presenter extends BasePresenter {
        void loadNowPlaying();

        void loadVoteInfo(String mediaId, String storeId);

        void thumbUpSong(Audient audient, String storeId);

        void cancelThumbUpSong(String storeId, Audient audient);

        void playNext();
    }
}
