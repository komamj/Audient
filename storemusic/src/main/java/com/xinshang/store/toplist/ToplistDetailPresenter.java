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
import com.xinshang.store.data.entities.BaseResponse;
import com.xinshang.store.data.entities.Music;
import com.xinshang.store.data.entities.Song;
import com.xinshang.store.data.entities.ToplistDetailResult;
import com.xinshang.store.utils.LogUtils;

import java.util.List;

import javax.inject.Inject;

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

        loadToplistDetail(mView.getTopId(), mView.getShowTime());
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");
        mDisposables.clear();
    }

    @Override
    public void loadToplistDetail(int topId, String showTime) {
        mDisposables.clear();

        Disposable disposable = mRepository.getToplistDetail(topId, showTime, 0, 40)
                .map(new Function<ToplistDetailResult, List<Song>>() {
                    @Override
                    public List<Song> apply(ToplistDetailResult toplistDetailResult) throws Exception {
                        return toplistDetailResult.dataBean.audients;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Song>>() {
                    @Override
                    public void onNext(List<Song> audients) {
                        if (mView.isActive()) {
                            mView.showToplistDetail(audients);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "getToplistDetail error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

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
