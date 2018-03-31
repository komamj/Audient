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

import com.xinshang.store.data.AudientRepository;
import com.xinshang.store.data.entities.ApiResponse;
import com.xinshang.store.data.entities.Comment;
import com.xinshang.store.data.entities.CommentDataBean;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
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
    public void loadComments(TencentMusic audient) {
        String storeId = mRepository.getStoreId();

        Disposable disposable = mRepository.getComments(audient.mediaId, null, storeId,
                0, 300)
                .map(new Function<ApiResponse<CommentDataBean>, CommentDataBean>() {
                    @Override
                    public CommentDataBean apply(ApiResponse<CommentDataBean> commentDataBeanApiResponse) throws Exception {
                        return commentDataBeanApiResponse.data;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<CommentDataBean>() {
                    @Override
                    public void accept(CommentDataBean commentDataBean) throws Exception {
                        if (mView.isActive()) {
                            mView.showCommentDataBean(commentDataBean);
                            if ((commentDataBean.othersComment.comments.isEmpty())) {
                                mView.showEmpty(true);
                            } else {
                                mView.showEmpty(false);
                            }
                        }
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<CommentDataBean, List<Comment>>() {
                    @Override
                    public List<Comment> apply(CommentDataBean commentDataBean) throws Exception {
                        List<Comment> comments = new ArrayList<>();
                        comments.addAll(commentDataBean.inStoreComment.comments);
                        comments.addAll(commentDataBean.othersComment.comments);
                        return comments;
                    }
                })
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
                            mView.setLoadingIncator(false);
                            mView.showEmpty(true);
                            mView.showLoadingError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.setLoadingIncator(false);
                            mView.showSuccessfulMessage();
                        }
                    }
                });

        mDisposable.add(disposable);
    }
}
