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
package com.xinshang.audient.splash;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.common.util.LogUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by koma on 3/5/18.
 */

public class SplashPresenter implements SplashContract.Presenter {
    private static final String TAG = SplashPresenter.class.getSimpleName();

    private final SplashContract.View mView;
    private final AudientRepository mRepository;
    private final CompositeDisposable mDisposables;

    @Inject
    public SplashPresenter(SplashContract.View view, AudientRepository repository) {
        mView = view;
        mRepository = repository;
        mDisposables = new CompositeDisposable();
    }

    @Inject
    void setUpListeners() {
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
    public void loadLoginStatus() {
        Disposable disposable = mRepository.getLoginStatus()
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (mView.isActive()) {
                            mView.showNextView(aBoolean);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadLoginStatus error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }
}
