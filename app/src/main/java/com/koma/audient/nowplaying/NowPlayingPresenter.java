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
package com.koma.audient.nowplaying;

import com.koma.audient.model.AudientRepository;
import com.koma.common.util.LogUtils;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class NowPlayingPresenter implements NowPlayingContract.Presenter {
    public static final String TAG = NowPlayingPresenter.class.getSimpleName();

    private NowPlayingContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    @Inject
    public NowPlayingPresenter(NowPlayingContract.View view, AudientRepository repository) {
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
}