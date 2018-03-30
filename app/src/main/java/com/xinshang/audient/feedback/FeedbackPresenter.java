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
package com.xinshang.audient.feedback;

import android.text.TextUtils;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.ApiResponse;
import com.xinshang.audient.model.entities.Feedback;
import com.xinshang.common.util.LogUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by koma on 3/28/18.
 */

public class FeedbackPresenter implements FeedbackContract.Presenter {
    private static final String TAG = FeedbackPresenter.class.getSimpleName();

    private final FeedbackContract.View mView;

    private final CompositeDisposable mDisposables;

    private final AudientRepository mRepository;

    @Inject
    public FeedbackPresenter(FeedbackContract.View view, AudientRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposables = new CompositeDisposable();
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mDisposables.clear();
    }

    @Override
    public void sendFeedback(String title, String content) {
        if (isInValid(title, content)) {
            return;
        }

        if (mView.isActive()) {
            mView.setLoadingIndicator(true);
        }

        Feedback feedback = new Feedback();
        feedback.title = title;
        feedback.content = content;

        Disposable disposable = mRepository.sendFeedback(feedback)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<ApiResponse>() {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.resultCode == 0) {
                            mView.showSuccessfulMessage();
                        } else {
                            mView.showFailedMessage();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "sendFeedback error : " + t.getMessage());

                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                        }
                    }
                });

        mDisposables.add(disposable);
    }

    private boolean isInValid(String title, String content) {
        if (TextUtils.isEmpty(title)) {
            mView.showErrorTitle();

            return true;
        } else if (TextUtils.isEmpty(content)) {
            mView.showErrorContent();

            return true;
        }
        return false;
    }
}
