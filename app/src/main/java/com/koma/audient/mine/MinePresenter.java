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
package com.koma.audient.mine;

import com.koma.audient.model.AudientRepository;
import com.koma.audient.model.entities.Audient;
import com.koma.common.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class MinePresenter implements MineContract.Presenter {
    public static final String TAG = MinePresenter.class.getSimpleName();

    private MineContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    @Inject
    public MinePresenter(MineContract.View view, AudientRepository repository) {
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

        loadFavorite();

        loadUser();
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        mDisposables.clear();
    }

    @Override
    public void loadFavorite() {
        mRepository.getAudientTests()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Audient>>() {
                    @Override
                    public void onNext(List<Audient> audients) {
                        if (mView.isActive()) {
                            mView.showFavoriteProgressBar(false);

                            mView.showFavorite(audients);
                        }
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
    public void loadUser() {
        mRepository.getAudientTests()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Audient>>() {
                    @Override
                    public void onNext(List<Audient> audients) {
                        if (mView.isActive()) {
                            mView.showUserProgressBar(false);

                            mView.showUser(audients);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
