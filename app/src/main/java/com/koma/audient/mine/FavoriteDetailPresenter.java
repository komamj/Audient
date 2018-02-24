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
import com.koma.audient.model.entities.FavoriteListResult;
import com.koma.common.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class FavoriteDetailPresenter implements FavoriteDetailContract.Presenter {
    public static final String TAG = FavoriteDetailPresenter.class.getSimpleName();

    private FavoriteDetailContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    @Inject
    public FavoriteDetailPresenter(FavoriteDetailContract.View view, AudientRepository repository) {
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
    public void loadData(String favoriteId) {
        Disposable disposable = mRepository.getFavoriteListResult(favoriteId)
                .map(new Function<FavoriteListResult, List<Audient>>() {
                    @Override
                    public List<Audient> apply(FavoriteListResult favoriteListResult) throws Exception {
                        return favoriteListResult.audients;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Audient>>() {
                    @Override
                    public void onNext(List<Audient> audients) {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);

                            mView.showAudients(audients);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadData error :" + t.toString());

                        mView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }
}
