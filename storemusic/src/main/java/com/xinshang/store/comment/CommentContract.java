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
package com.xinshang.store.comment;

import com.xinshang.store.base.BasePresenter;
import com.xinshang.store.base.BaseView;
import com.xinshang.store.data.entities.Comment;
import com.xinshang.store.data.entities.CommentDataBean;
import com.xinshang.store.data.entities.Song;

import java.util.List;

public interface CommentContract {
    interface View extends BaseView<Presenter> {
        void showComments(List<Comment> comments);

        void showCommentDataBean(CommentDataBean commentDataBean);

        boolean isActive();

        void showLoadingError();

        void showSuccessfulMessage();

        void showEmpty(boolean forceShow);

        void setLoadingIncator(boolean isActive);
    }

    interface Presenter extends BasePresenter {
        void loadComments(Song audient);
    }
}
