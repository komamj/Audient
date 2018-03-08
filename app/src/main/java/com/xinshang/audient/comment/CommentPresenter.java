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
package com.xinshang.audient.comment;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.BaseResponse;
import com.xinshang.audient.model.entities.Comment;
import com.xinshang.audient.model.entities.CommentResponse;
import com.xinshang.audient.model.entities.CommentResult;
import com.xinshang.common.util.LogUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class CommentPresenter implements CommentContract.Presenter {
    private static final String TAG = CommentPresenter.class.getSimpleName();

    private final CommentContract.View mView;

    private final AudientRepository mRepository;

    private final CompositeDisposable mDisposable;

    @Inject
    public CommentPresenter(CommentContract.View view, AudientRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposable = new CompositeDisposable();
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        LogUtils.i(TAG, "subscribe");
    }

    @Override
    public void unSubscribe() {
        mDisposable.clear();
    }

    @Override
    public void loadComments(Audient audient) {
        Disposable disposable = mRepository.getCommentResult(audient.mediaId)
                .map(new Function<CommentResult, CommentResponse>() {
                    @Override
                    public CommentResponse apply(CommentResult commentResult) throws Exception {
                        return commentResult.commentResponse;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<CommentResponse>() {
                    @Override
                    public void onNext(CommentResponse commentResponse) {
                        if (mView.isActive()) {
                            mView.showComments(commentResponse);
                            if (commentResponse.inStoreComment == null &&
                                    (commentResponse.othersComment == null
                                            || commentResponse.othersComment.comments.isEmpty())) {
                                mView.showEmpty(true);
                            } else {
                                mView.showEmpty(false);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadComments error :" + t.toString());

                        if (mView.isActive()) {
                            mView.showProgressBar(false);
                            mView.showEmpty(true);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.showProgressBar(false);
                        }
                    }
                });

        mDisposable.add(disposable);
    }

    @Override
    public void thumbUpComment(Comment comment) {
        if (comment.voted) {
            mRepository.cancelThumbUpComment(comment.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                        @Override
                        public void onNext(BaseResponse response) {
                            LogUtils.i(TAG, "cancelThumbUpComment message :" + response.message);
                        }

                        @Override
                        public void onError(Throwable t) {
                            LogUtils.e(TAG, "cancelThumbUpComment error :" + t.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            mRepository.thumbUpComment(comment.id)
                    .subscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSubscriber<BaseResponse>() {
                        @Override
                        public void onNext(BaseResponse response) {
                            LogUtils.i(TAG, "thumbUpComment message :" + response.message);
                        }

                        @Override
                        public void onError(Throwable t) {
                            LogUtils.e(TAG, "thumbUpComment error :" + t.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
