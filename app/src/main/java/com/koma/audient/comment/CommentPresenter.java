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
package com.koma.audient.comment;

import com.koma.audient.model.AudientRepository;
import com.koma.audient.model.entities.Comment;
import com.koma.audient.model.entities.CommentResult;
import com.koma.common.util.LogUtils;

import java.util.List;

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
    public void loadComments(String id) {
        Disposable disposable = mRepository.getCommentResult(id)
                .map(new Function<CommentResult, List<Comment>>() {
                    @Override
                    public List<Comment> apply(CommentResult commentResult) throws Exception {
                        return commentResult.comments;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Comment>>() {
                    @Override
                    public void onNext(List<Comment> comments) {
                        if (mView.isActive()) {
                            mView.showComments(comments);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadComments error :" + t.toString());

                        if (mView.isActive()) {
                            mView.showLoadingError();
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
}
