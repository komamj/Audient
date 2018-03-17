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
package com.xinshang.store.splash;

import com.xinshang.store.data.AudientRepository;
import com.xinshang.store.data.entities.ApiResponse;
import com.xinshang.store.data.entities.Store;
import com.xinshang.store.data.entities.Token;
import com.xinshang.store.utils.LogUtils;

import org.reactivestreams.Publisher;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
        if (mRepository.getLoginStatus()) {
            delayLaunchMainView();
        } else {
            mView.showLoginView(true);
        }
    }

    @Override
    public void unSubscribe() {
        mDisposables.clear();
    }

    @Override
    public void delayLaunchMainView() {
        Disposable disposable = Flowable.just("")
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<String>() {
                    @Override
                    public void onNext(String aBoolean) {
                        if (mView.isActive()) {
                            mView.showMainView();
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

    @Override
    public void login(String userName, String password) {
        if (mView.isActive()) {
            mView.setLoadingIndicatorView(true);
        }

        mRepository.getToken(userName, password)
                .doOnNext(new Consumer<Token>() {
                    @Override
                    public void accept(Token token) throws Exception {
                        mRepository.persistenceAccessToken(token.accessToken);
                        mRepository.persistenceRefreshToken(token.refreshToken);
                        mRepository.persistenceLoginStatus(true);
                    }
                })
                .flatMap(new Function<Token, Publisher<Store>>() {
                    @Override
                    public Publisher<Store> apply(Token token) throws Exception {
                        return mRepository.getStoreInfo()
                                .map(new Function<ApiResponse<List<Store>>, Store>() {
                                    @Override
                                    public Store apply(ApiResponse<List<Store>> listApiResponse) throws Exception {
                                        return listApiResponse.data.get(0);
                                    }
                                });
                    }
                })
                .doOnNext(new Consumer<Store>() {
                    @Override
                    public void accept(Store store) throws Exception {
                        mRepository.persistenceStoreId(store.id);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Store>() {
                    @Override
                    public void onNext(Store store) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "login error : " + t.getMessage());
                        if (mView.isActive()) {
                            mView.setLoadingIndicatorView(false);

                            mView.showLoginError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.setLoadingIndicatorView(false);

                            mView.showSuccessfullyLoginedMessage();

                            mView.showLoginView(false);
                        }

                        delayLaunchMainView();
                    }
                });
    }
}
