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
package com.xinshang.store.toplist;

import com.xinshang.store.data.AudientRepository;
import com.xinshang.store.data.entities.ApiResponse;
import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.Music;
import com.xinshang.store.data.entities.PlayAllRequest;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.data.entities.ToplistSongResponse;
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

public class ToplistDetailPresenter implements ToplistDetailContract.Presenter {
    private static final String TAG = ToplistDetailPresenter.class.getSimpleName();

    private final ToplistDetailContract.View mView;

    private final AudientRepository mRepository;

    private final CompositeDisposable mDisposables;

    private int mPage = 0;

    private List<Song> mSongs;

    @Inject
    public ToplistDetailPresenter(ToplistDetailContract.View view, AudientRepository repository) {
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
        LogUtils.i(TAG, "subscribe");
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        mDisposables.clear();
    }

    @Override
    public void loadToplistSongs(int topId, String showTime) {
        mPage = 0;

        mDisposables.clear();

        Disposable disposable = loadToplistSongs(topId, showTime, mPage, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Song>>() {
                    @Override
                    public void onNext(List<Song> songs) {
                        mSongs = songs;

                        if (mView.isActive()) {
                            mView.showToplistSongs(songs);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "getToplistSongs error :" + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposables.add(disposable);
    }

    private Flowable<List<Song>> loadToplistSongs(final int topId, final String showTime,
                                                  final int page, final int size) {
        LogUtils.i(TAG, "loadToplistSongs page : " + page);

        return mRepository.getToplistSongs(topId, showTime, page, size)
                .flatMap(new Function<ApiResponse<ToplistSongResponse>, Publisher<List<Song>>>() {
                    @Override
                    public Publisher<List<Song>> apply(ApiResponse<ToplistSongResponse> response) {
                        return Flowable.just(response.data.songs);
                    }
                });
    }

    @Override
    public void loadNextPage(final int topId, final String showTime) {
        mDisposables.clear();

        if (mView.isActive()) {
            mView.setLoadingIndicator(true);
        }

        Disposable disposable = loadToplistSongs(topId, showTime, mPage + 1, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Song>>() {
                    @Override
                    public void onNext(List<Song> songs) {
                        mSongs.addAll(songs);

                        if (mView.isActive()) {
                            mView.showNextPageSongs(songs);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadNextPage error :" + t.getMessage());

                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        ++mPage;

                        if (mView.isActive()) {
                            mView.setLoadingIndicator(false);
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

    @Override
    public void playAll() {
        if (mSongs == null || mSongs.isEmpty()) {
            return;
        }

        PlayAllRequest playAllRequest = new PlayAllRequest();
        playAllRequest.songs = mSongs;

        String storeId = mRepository.getStoreId();

        mRepository.playAllSongs(storeId, playAllRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (mView.isActive()) {
                            if (response.resultCode == 0) {
                                mView.showPlaySuccessfulMessage();
                            } else {
                                mView.showPlayFailedMessage();
                            }
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
