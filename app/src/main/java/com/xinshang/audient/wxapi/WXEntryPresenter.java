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
package com.xinshang.audient.wxapi;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.Token;
import com.xinshang.common.util.LogUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by koma on 3/9/18.
 */

public class WXEntryPresenter implements WXEntryContract.Presenter {
    private static final String TAG = WXEntryPresenter.class.getSimpleName();

    private final WXEntryContract.View mView;

    private final AudientRepository mRepository;

    private final CompositeDisposable mDisposables;

    @Inject
    public WXEntryPresenter(WXEntryContract.View view, AudientRepository repository) {
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
    public void loadAccessToken(String code) {
        mView.setLoadIndicator(true);

        Disposable disposable = mRepository.getAccessToken(code)
                .doOnNext(new Consumer<Token>() {
                    @Override
                    public void accept(Token token) throws Exception {
                        LogUtils.i(TAG, "doOnNext thread name :" + Thread.currentThread().getName());
                        mRepository.persistenceAccessToken(token.accessToken);
                        mRepository.persistenceRefreshToken(token.refreshToken);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Token>() {
                    @Override
                    public void onNext(Token token) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadAccessToken error :" + t.toString());
                        mRepository.persistenceLoginStatus(false);
                        if (mView.isActive()) {
                            mView.setLoadIndicator(false);
                            mView.showLoadingError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        mRepository.persistenceLoginStatus(true);
                        if (mView.isActive()) {
                            mView.showSuccessfulMessage();
                            mView.setLoadIndicator(false);
                        }
                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void processWXResponse(BaseResp response) {
        LogUtils.i(TAG, "onResp " + response.errCode);
        switch (response.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                SendAuth.Resp newResp = (SendAuth.Resp) response;
                String code = newResp.code;

                // load token
                this.loadAccessToken(code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                break;
        }
    }
}
