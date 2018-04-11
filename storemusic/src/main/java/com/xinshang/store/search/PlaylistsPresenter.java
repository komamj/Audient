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
package com.xinshang.store.search;

import com.xinshang.store.data.AudientRepository;
import com.xinshang.store.utils.LogUtils;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by koma on 4/11/18.
 */

public class PlaylistsPresenter implements PlaylistsContract.Presenter {
    private static final String TAG = PlaylistsPresenter.class.getSimpleName();

    private PlaylistsContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    @Inject
    public PlaylistsPresenter(PlaylistsContract.View view, AudientRepository repository) {
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
    public void loadPlaylists(String keyword) {
        if (mView.isActive()) {
            mView.setLoadingIndictor(true);
        }
    }
}
