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
package com.xinshang.store.audition;

import com.xinshang.store.base.BasePresenter;
import com.xinshang.store.base.BaseView;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.utils.Constants;

public interface AuditionContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        String getAudientId();

        void showAudient(Song audient);

        void setMaxProgress(int progress);

        void showProgress(int progress);

        void showSecondaryProgress(int progress);

        void updateControllView(@Constants.PlayState int playState);

        void dismissAuditionDialog();
    }

    interface Presenter extends BasePresenter {
        void loadAudient(String id);

        void loadMedia(String url);

        void loadFile(String id);

        void pause();

        void play();

        void stop();

        void doPauseOrPlay();

        void seekTo(int position);

        void replay();
    }
}
