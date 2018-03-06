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
package com.xinshang.store.main;

import com.xinshang.store.data.AudientRepository;
import com.xinshang.store.data.entities.StoreKeeper;
import com.xinshang.store.utils.LogUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class MainPresenter implements MainContract.Presenter {
    public static final String TAG = MainPresenter.class.getSimpleName();

    private MainContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    @Inject
    public MainPresenter(MainContract.View view, AudientRepository repository) {
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
    public void loadStoreKeeperInfo() {
        Disposable disposable = mRepository.getStoreKeeperInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<StoreKeeper>() {
                    @Override
                    public void onNext(StoreKeeper storeKeeper) {
                        if (mView != null) {
                            mView.showStoreKeeper(storeKeeper);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadStoreKeeperInfo error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposables.add(disposable);
    }
}
