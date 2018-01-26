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
package com.koma.audient.audition;

import com.koma.audient.model.entities.Audient;
import com.koma.common.base.BasePresenter;
import com.koma.common.base.BaseView;
import com.koma.common.util.Constants;

public interface AuditionContract {
    interface View extends BaseView<Presenter> {
        boolean isActive();

        long getLimitedTime();

        String getAudientId();

        void showAudient(Audient audient);

        void setMaxProgress(int progress);

        void showProgress(int progress);

        void showSecondaryProgress(int progress);

        void updateControllView(@Constants.PlayState int playState);
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
