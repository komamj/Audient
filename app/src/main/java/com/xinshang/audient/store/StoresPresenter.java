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
package com.xinshang.audient.store;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.ApiResponse;
import com.xinshang.audient.model.entities.Store;
import com.xinshang.audient.model.entities.StoreDataBean;
import com.xinshang.common.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by koma on 3/26/18.
 */

public class StoresPresenter implements StoresConstract.Presenter {
    private static final String TAG = StoresPresenter.class.getSimpleName();

    private final StoresConstract.View mView;

    private final AudientRepository mRepository;

    private final CompositeDisposable mDisposables;

    @Inject
    public StoresPresenter(StoresConstract.View view, AudientRepository repository) {
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
        loadStores();
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
    public void unSubscribe() {
        mDisposables.clear();
    }
}
