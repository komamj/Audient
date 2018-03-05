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
package com.xinshang.audient.login;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.Token;
import com.xinshang.audient.util.WeChatMessageEvent;
import com.xinshang.common.util.LogUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class LoginPresenter implements LoginContract.Presenter {
    public static final String TAG = LoginPresenter.class.getSimpleName();

    private LoginContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    @Inject
    public LoginPresenter(LoginContract.View view, AudientRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposables = new CompositeDisposable();
    }

    @Inject
    void setUpListener() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        LogUtils.i(TAG, "subscribe");
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        mDisposables.clear();
    }

    @Override
    public void login() {
        mRepository.sendLoginRequest();

        mRepository.getToken("koma_mj", "201124jun")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Token>() {
                    @Override
                    public void accept(Token token) throws Exception {
                        if (token != null) {
                            mRepository.persistenceLoginInfo("", token.accessToken, token.accessToken);
                        }
                    }
                })
                .subscribeWith(new DisposableSubscriber<Token>() {
                    @Override
                    public void onNext(Token token) {
                        if (mView.isActive()) {
                            mView.onLoginFinished();
                        }
                        mRepository.setLoginStatus(true);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getAccessToken(WeChatMessageEvent messageEvent) {
        final String code = messageEvent.getCode();
        mRepository.getAccessToken(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Token>() {
                    @Override
                    public void accept(Token token) throws Exception {
                        mRepository.persistenceLoginInfo(code, token.accessToken, token.refreshToken);
                    }
                })
                .subscribeWith(new DisposableSubscriber<Token>() {
                    @Override
                    public void onNext(Token token) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "getAcceesToken error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.onLoginFinished();
                        }
                    }
                });
    }
}
