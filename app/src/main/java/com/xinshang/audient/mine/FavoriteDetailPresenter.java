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
package com.xinshang.audient.mine;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.BaseResponse;
import com.xinshang.audient.model.entities.Favorite;
import com.xinshang.audient.model.entities.FavoriteListResult;
import com.xinshang.common.util.LogUtils;

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
                .map(new Function<FavoriteListResult, List<Favorite.FavoritesSong>>() {
                    @Override
                    public List<Favorite.FavoritesSong> apply(FavoriteListResult favoriteListResult) throws Exception {
                        return favoriteListResult.favoritesSongs;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Favorite.FavoritesSong>>() {
                    @Override
                    public void onNext(List<Favorite.FavoritesSong> favoritesSongs) {
                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);

                            mView.showFavoritesSong(favoritesSongs);
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

    @Override
    public void deleteFavoriteSong(Favorite.FavoritesSong favoritesSong) {
        mRepository.deleteFavoritesSong(favoritesSong.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (mView.isActive()) {
                            if (response.resultCode == 0) {
                                LogUtils.i(TAG, "deleteFavoriteSong " + response.message);

                                loadData(mView.getFavoritesId());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "deleteFavoriteSong error " + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void demand(Audient audient) {
        if (mView.isActive()) {
            if (mRepository.isFirstDemand()) {
                mView.showHintDialog(audient);
            } else {
                mView.showPaymentDialog(audient);
            }
        }
    }
}