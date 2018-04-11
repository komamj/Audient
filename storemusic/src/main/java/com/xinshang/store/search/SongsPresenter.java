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
import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.Music;
import com.xinshang.store.data.entities.SearchResult;
import com.xinshang.store.data.entities.TencentMusic;
import com.xinshang.store.utils.LogUtils;

import java.util.List;

import javax.inject.Inject;

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
    public void loadSongs(String keyword) {
        if (mView.isActive()) {
            mView.setLoadingIndictor(true);
        }

        Disposable disposable = mRepository.searchSongs(keyword, 0, 300)
                .map(new Function<SearchResult, List<TencentMusic>>() {
                    @Override
                    public List<TencentMusic> apply(SearchResult searchResult) throws Exception {
                        return searchResult.dataBean.audients;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<TencentMusic>>() {
                    @Override
                    public void onNext(List<TencentMusic> audients) {
                        if (mView.isActive()) {
                            mView.setLoadingIndictor(false);

                            mView.showAudients(audients);

                            // mView.showEmpty(audients.isEmpty());
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadSearchResults error :" + t.toString());

                        if (mView.isActive()) {
                            mView.setLoadingIndictor(false);

                            mView.showLoadingError();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void addToPlaylist(TencentMusic tencentMusic) {
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
