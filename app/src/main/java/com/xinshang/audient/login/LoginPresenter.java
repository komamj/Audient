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
import com.xinshang.audient.model.entities.BaseResponse;
import com.xinshang.audient.model.entities.Token;
import com.xinshang.audient.model.entities.User;
import com.xinshang.common.util.LogUtils;

import org.reactivestreams.Publisher;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
    public void login(User user) {
        mRepository.getToken("koma_mj", "201124koma")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Token>() {
                    @Override
                    public void accept(Token token) throws Exception {
                        LogUtils.i(TAG, "token :" + token.toString());
                    }
                });

        Disposable disposable = mRepository.getLoginResult(user)
                .flatMap(new Function<BaseResponse, Publisher<Boolean>>() {
                    @Override
                    public Publisher<Boolean> apply(BaseResponse loginResult) throws Exception {
                        if (loginResult.resultCode == 0 || loginResult.message.equals("操作成功")) {
                            return mRepository.setLoginStatus(true);
                        } else {
                            return mRepository.setLoginStatus(true);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean status) {
                        mView.onLoginFinished();
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "login error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }
}
