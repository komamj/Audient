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
import com.xinshang.store.data.entities.AlbumResponse;
import com.xinshang.store.data.entities.ApiResponse;

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
 * Created by koma on 4/13/18.
 */

public class AlbumsPresenter implements AlbumsContract.Presenter {
    private static final String TAG = AlbumDetailPresenter.class.getSimpleName();

    private final AlbumsContract.View mView;

    private final AudientRepository mRepository;

    private final CompositeDisposable mDisposables;

    private int mPage = 0;

    private String mKeyword;

    @Inject
    public AlbumsPresenter(AlbumsContract.View view, AudientRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposables = new CompositeDisposable();
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
        mDisposables.clear();
    }

    @Override
    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    @Override
    public void loadAlbums(String keyword) {
        mPage = 0;

        if (mView.isActive()) {
            mView.setLoadingIndictor(true);
        }

        mDisposables.clear();

        Disposable disposable = getAlbums(keyword, mPage, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<AlbumResponse.Album>>() {
                    @Override
                    public void onNext(List<AlbumResponse.Album> albums) {
                        if (mView.isActive()) {
                            mView.showAlbums(albums);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView.isActive()) {
                            mView.setLoadingIndictor(false);

                            mView.showLoadingError();
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

    private Flowable<List<AlbumResponse.Album>> getAlbums(String keyword, int page, int size) {
        return mRepository.searchAlbums(keyword, page, size)
                .flatMap(new Function<ApiResponse<AlbumResponse>, Publisher<List<AlbumResponse.Album>>>() {
                    @Override
                    public Publisher<List<AlbumResponse.Album>> apply(ApiResponse<AlbumResponse> response) {
                        return Flowable.just(response.data.albums);
                    }
                });
    }

    @Override
    public void loadNextPageAlbums(String keyword) {
        if (mView.isActive()) {
            mView.setLoadingMoreIndicator(true);
        }

        mDisposables.clear();

        Disposable disposable = getAlbums(keyword, mPage + 1, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<AlbumResponse.Album>>() {
                    @Override
                    public void onNext(List<AlbumResponse.Album> albums) {
                        if (mView.isActive()) {
                            mView.showNextPageAlbums(albums);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView.isActive()) {
                            mView.setLoadingMoreIndicator(false);

                            mView.showLoadingError();
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
