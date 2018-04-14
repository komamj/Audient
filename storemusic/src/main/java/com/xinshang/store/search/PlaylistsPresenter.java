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
import com.xinshang.store.data.entities.ApiResponse;
import com.xinshang.store.data.entities.Playlist;
import com.xinshang.store.data.entities.PlaylistResponse;
import com.xinshang.store.utils.LogUtils;

import org.reactivestreams.Publisher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by koma on 4/11/18.
 */

public class PlaylistsPresenter implements PlaylistsContract.Presenter {
    private static final String TAG = PlaylistsPresenter.class.getSimpleName();

    private PlaylistsContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    private String mKeyword;

    private int mPage = 0;

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
    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    @Override
    public void loadPlaylists(String keyword) {
        mPage = 0;

        if (mView.isActive()) {
            mView.setLoadingIndictor(true);
        }

        mDisposables.clear();

        Disposable disposable = getPlaylists(keyword, mPage, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Playlist>>() {
                    @Override
                    public void onNext(List<Playlist> playlists) {
                        if (mView.isActive()) {
                            mView.showPlaylists(playlists);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.i(TAG, "searchPlaylists error : " + t.getMessage());

                        if (mView.isActive()) {
                            mView.setLoadingIndictor(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.setLoadingIndictor(false);
                        }
                    }
                });
        mDisposables.add(disposable);
    }

    private Flowable<List<Playlist>> getPlaylists(String keyword, int page, int size) {
        return mRepository.searchPlaylists(keyword, page, size)
                .flatMap(new Function<ApiResponse<PlaylistResponse>, Publisher<List<Playlist>>>() {
                    @Override
                    public Publisher<List<Playlist>> apply(ApiResponse<PlaylistResponse> response) {
                        return Flowable.just(response.data.playlists);
                    }
                });
    }


    @Override
    public void loadNextPagePlaylists(String keyword) {
        if (mView.isActive()) {
            mView.setLoadingMoreIndicator(true);
        }

        mDisposables.clear();

        Disposable disposable = getPlaylists(keyword, mPage + 1, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Playlist>>() {
                    @Override
                    public void onNext(List<Playlist> playlists) {
                        if (mView.isActive()) {
                            mView.showPlaylists(playlists);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.i(TAG, "searchPlaylists error : " + t.getMessage());

                        if (mView.isActive()) {
                            mView.setLoadingMoreIndicator(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        ++mPage;

                        if (mView.isActive()) {
                            mView.setLoadingMoreIndicator(false);
                        }
                    }
                });
        mDisposables.add(disposable);
    }
}
