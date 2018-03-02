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
package com.xinshang.store.account;


import com.xinshang.store.data.AudientRepository;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class AccountPresenter implements AccountContract.Presenter {
    private static final String TAG = AccountPresenter.class.getSimpleName();

    private final AccountContract.View mView;

    private final AudientRepository mRepository;

    private final CompositeDisposable mDisposable;

    @Inject
    public AccountPresenter(AccountContract.View view, AudientRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposable = new CompositeDisposable();
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
        mDisposable.clear();
    }
}
