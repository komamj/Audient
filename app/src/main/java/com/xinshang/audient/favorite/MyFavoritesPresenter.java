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
package com.xinshang.audient.favorite;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.BaseResponse;
import com.xinshang.audient.model.entities.Favorite;
import com.xinshang.audient.model.entities.FavoritesResult;
import com.xinshang.audient.model.entities.MessageEvent;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class MyFavoritesPresenter implements MyFavoritesContract.Presenter {
    private static final String TAG = MyFavoritesPresenter.class.getSimpleName();

    private final MyFavoritesContract.View mView;

    private final AudientRepository mRepository;

    private final CompositeDisposable mDisposables;

    @Inject
    public MyFavoritesPresenter(MyFavoritesContract.View view, AudientRepository repository) {
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

    }

    @Override
    public void unSubscribe() {
        mDisposables.clear();
    }

    @Override
    public void loadMyFavorites() {
        Disposable disposable = mRepository.getFavoriteResult()
                .map(new Function<FavoritesResult, List<Favorite>>() {
                    @Override
                    public List<Favorite> apply(FavoritesResult favoriteResult) throws Exception {
                        return favoriteResult.favorites;
                    }
                })
                .doOnNext(new Consumer<List<Favorite>>() {
                    @Override
                    public void accept(List<Favorite> favorites) throws Exception {
                        LogUtils.i(TAG, "loadFavorites thread name :" +
                                Thread.currentThread().getName() + "," + favorites.isEmpty());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Favorite>>() {
                    @Override
                    public void onNext(List<Favorite> favorites) {
                        LogUtils.i(TAG, "loadfavorites " + favorites.toString());

                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);

                            mView.showFavorites(favorites);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadFavorites error " + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void addToFavorite(String favoritesId, Audient audient) {
        mRepository.addToFavorite(favoritesId, audient)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        LogUtils.i(TAG, "addToFavorite " + response.message);
                        if (mView.isActive()) {
                            if (response.resultCode == 0) {
                                EventBus.getDefault().post(new MessageEvent(Constants.MESSAGE_MY_FAVORITES_CHANGED));

                                mView.showSuccessfullyAddedMessage();
                            } else {
                                mView.showFailedMessage();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "addToFavorite error " + t.toString());
                        if (mView.isActive()) {
                            mView.showFailedMessage();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
