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
package com.xinshang.store.store;

import com.xinshang.store.data.AudientRepository;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by koma_20 on 2018/3/1.
 */

public class StoreInfoPresenter implements StoreInfoContract.Presenter {
    private static final String TAG = StoreInfoPresenter.class.getSimpleName();

    private final StoreInfoContract.View mView;
    private final AudientRepository mRepository;
    private final CompositeDisposable mDisposables;

    public StoreInfoPresenter(StoreInfoContract.View view, AudientRepository repository) {
        mView = view;
        mRepository = repository;
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mDisposables.clear();
    }
}
