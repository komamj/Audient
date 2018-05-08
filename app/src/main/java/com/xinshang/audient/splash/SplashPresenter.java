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

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.ApiResponse;
import com.xinshang.audient.model.entities.Coupon;
import com.xinshang.audient.model.entities.Store;
import com.xinshang.audient.model.entities.StoreDataBean;
import com.xinshang.audient.model.entities.Token;
import com.xinshang.audient.model.entities.Version;
import com.xinshang.audient.util.WXEntryMessageEvent;
import com.xinshang.common.util.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        LogUtils.i(TAG, "subscribe");

        checkVersion();

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WXEntryMessageEvent messageEvent) {
        this.processWXResponse(messageEvent.getResp());
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        EventBus.getDefault().unregister(this);

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
    public void sendLoginRequest() {
        mRepository.sendLoginRequest();
    }

    @Override
    public void loadAccessToken(String code) {
        Disposable disposable = mRepository.getAccessToken(code)
                .doOnNext(new Consumer<Token>() {
                    @Override
                    public void accept(Token token) {
                        mRepository.persistenceAccessToken(token.accessToken);
                        mRepository.persistenceRefreshToken(token.refreshToken);

                        mRepository.getToReceiverCoupons()
                                .map(new Function<ApiResponse<List<Coupon>>, List<Coupon>>() {
                                    @Override
                                    public List<Coupon> apply(ApiResponse<List<Coupon>> response) {
                                        return response.data;
                                    }
                                })
                                .doOnNext(new Consumer<List<Coupon>>() {
                                    @Override
                                    public void accept(List<Coupon> coupons) {
                                        for (Coupon coupon : coupons) {
                                            LogUtils.i(TAG, "coupon : " + coupon.id);
                                            mRepository.getCoupon(coupon.id)
                                                    .subscribe();
                                        }
                                    }
                                })
                                .subscribe(new Consumer<List<Coupon>>() {
                                    @Override
                                    public void accept(List<Coupon> coupons) {
                                        LogUtils.i(TAG, "hha : " + coupons.size());
                                    }
                                });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Token>() {
                    @Override
                    public void onNext(Token token) {
                        if (mView.isActive()) {
                            mView.showStoresUI(true);

                            loadStores();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadAccessToken error :" + t.toString());
                        mRepository.persistenceLoginStatus(false);
                        if (mView.isActive()) {
                            mView.showLoadingError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.showSuccessfulMessage();
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
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            default:
                break;
        }
    }

    @Override
    public void loadStores() {
        if (mView.isActive()) {
            mView.setLoadingIndicator(true);
        }

        Disposable disposable = mRepository.getStores(true, 0, 20, null)
                .map(new Function<ApiResponse<StoreDataBean>, List<Store>>() {
                    @Override
                    public List<Store> apply(ApiResponse<StoreDataBean> storeDataBeanApiResponse) throws Exception {
                        return storeDataBeanApiResponse.data.stores;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Store>>() {
                    @Override
                    public void onNext(List<Store> stores) {
                        if (mView.isActive()) {
                            mView.showStores(stores);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "getStores error : " + t.getMessage());

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

    @Override
    public void persistenceStore(Store store) {
        mRepository.persistenceStoreId(store.id);
        mRepository.persistenceLoginStatus(true);

        mView.showMainView();
    }

    @Override
    public void checkVersion() {
        Disposable disposable = mRepository.getNewestVersion()
                .map(new Function<Version, Integer>() {
                    @Override
                    public Integer apply(Version version) throws Exception {
                        return version.version;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Integer>() {
                    @Override
                    public void onNext(final Integer newestVersion) {
                        mRepository.getCurrentVersionCode()
                                .subscribe(new Consumer<Integer>() {
                                    @Override
                                    public void accept(Integer currentVersion) throws Exception {
                                        if (currentVersion < newestVersion) {
                                            if (mView.isActive()) {
                                                mView.showUpdateMessage();
                                            }
                                        } else {
                                            if (mRepository.getLoginStatus()) {
                                                delayLaunchMainView();
                                            } else {
                                                mView.showLoginButton(true);
                                            }
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "checkVersion error : " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposables.add(disposable);
    }
}
