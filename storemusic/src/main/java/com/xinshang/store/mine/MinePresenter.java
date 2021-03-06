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
package com.xinshang.store.mine;

import com.xinshang.store.data.AudientRepository;
import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.Favorite;
import com.xinshang.store.data.entities.FavoritesResult;
import com.xinshang.store.data.entities.MessageEvent;
import com.xinshang.store.data.entities.Music;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
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

        loadFavorites();
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        mDisposables.clear();
    }

    @Override
    public void loadFavorites() {
        LogUtils.i(TAG, "loadFavorites");

        if (mView.isActive()) {
            mView.setLoadingIndicator(true);
        }

        mDisposables.clear();

        Disposable disposable = mRepository.getFavoriteResult()
                .map(new Function<FavoritesResult, List<Favorite>>() {
                    @Override
                    public List<Favorite> apply(FavoritesResult favoriteResult) throws Exception {
                        return favoriteResult.favorites;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Favorite>>() {
                    @Override
                    public void onNext(List<Favorite> favorites) {
                        if (mView.isActive()) {
                            mView.showFavorites(favorites);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadFavorites error " + t.toString());

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
    public void deleteMyFavorite(Favorite favorite) {
        mRepository.deleteFavorite(favorite.favoritesId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.resultCode == 0) {
                            LogUtils.i(TAG, "deleteMyFavorite :" + response.message);

                            EventBus.getDefault().post(new MessageEvent(
                                    Constants.MESSAGE_MY_FAVORITES_CHANGED));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "deleteMyFavorite error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void addToPlaylist(Song tencentMusic) {
        Music music = new Music();
        music.storeId = mRepository.getStoreId();
        music.albumId = tencentMusic.albumId;
        music.albumName = tencentMusic.albumName;
        music.artistId = tencentMusic.artistId;
        music.artistName = tencentMusic.artistName;
        music.mediaInterval = String.valueOf(tencentMusic.duration);
        music.mediaId = tencentMusic.mediaId;
        music.mediaName = tencentMusic.mediaName;

        mRepository.addToPlaylist(music)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse baseResponse) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "addToPlaylist completed");
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.i(TAG, "addToPlaylist completed");
                    }
                });
    }
}
