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
import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.Music;
import com.xinshang.store.data.entities.SearchResponse;
import com.xinshang.store.data.entities.Song;
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

public class SongsPresenter implements SongsContract.Presenter {
    private static final String TAG = SongsPresenter.class.getSimpleName();

    private SongsContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    private int mPage = 0;

    private String mKeyword;

    @Inject
    public SongsPresenter(SongsContract.View view, AudientRepository repository) {
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
    public void loadSongs(String keyword) {
        LogUtils.i(TAG, "loadSongs : keyword : " + keyword);

        if (!isValid(keyword)) {
            return;
        }

        if (mView.isActive()) {
            mView.setLoadingIndictor(true);
        }

        mPage = 0;

        mDisposables.clear();

        Disposable disposable = getSongs(keyword, mPage, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Song>>() {
                    @Override
                    public void onNext(List<Song> songs) {
                        if (mView.isActive()) {
                            mView.showSongs(songs);
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

    private boolean isValid(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }
        return true;
    }

    private Flowable<List<Song>> getSongs(String keyword, int page, int size) {
        LogUtils.i(TAG, "getSongs page : " + page);

        return mRepository.searchSongs(keyword, page, size)
                .flatMap(new Function<ApiResponse<SearchResponse>, Publisher<List<Song>>>() {
                    @Override
                    public Publisher<List<Song>> apply(ApiResponse<SearchResponse> response) {
                        return Flowable.just(response.data.songs);
                    }
                });
    }

    @Override
    public void loadNextPageSongs(String keyword) {
        LogUtils.i(TAG, "loadNextPageSongs : keyword : " + keyword);

        if (!isValid(keyword)) {
            return;
        }

        mDisposables.clear();

        if (mView.isActive()) {
            mView.setLoadingMoreIndicator(true);
        }

        Disposable disposable = getSongs(keyword, mPage + 1, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Song>>() {
                    @Override
                    public void onNext(List<Song> songs) {
                        LogUtils.i(TAG, "loadNextPageSongs size : " + songs.size());
                        if (mView.isActive()) {
                            if (songs.isEmpty()) {
                                mView.showNoMoreMessage();
                            } else {
                                ++mPage;

                                mView.showNextPageSongs(songs);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView.isActive()) {
                            mView.setLoadingMoreIndicator(false);

                            mView.showLoadingError();

                            mView.showNoMoreMessage();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.setLoadingMoreIndicator(false);
                        }
                    }
                });
        mDisposables.add(disposable);
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
