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
package com.xinshang.audient.playlist;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.NowPlayingResult;
import com.xinshang.common.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class PlaylistPresenter implements PlaylistContract.Presenter {
    public static final String TAG = PlaylistPresenter.class.getSimpleName();

    private PlaylistContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    @Inject
    public PlaylistPresenter(PlaylistContract.View view, AudientRepository repository) {
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

        loadNowPlaying();

        loadAudients();
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        mDisposables.clear();
    }

    @Override
    public void loadNowPlaying() {
        Disposable disposable = mRepository.getNowPlayingResult()
                .map(new Function<NowPlayingResult, Audient>() {
                    @Override
                    public Audient apply(NowPlayingResult nowPlayingResult) throws Exception {
                        return nowPlayingResult.audient;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Audient>() {
                    @Override
                    public void onNext(Audient audient) {
                        if (mView.isActive()) {
                            mView.showNowPlaying(audient);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadNowPlaying error " + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void loadAudients() {
        Disposable disposable = mRepository.getAudientTests()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Audient>>() {
                    @Override
                    public void onNext(List<Audient> audients) {
                        if (mView.isActive()) {
                            mView.showProgressBar(false);

                            mView.showAudients(audients);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadAudients error " + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void thumbUp(Audient audient) {
        LogUtils.i(TAG, "thumbUp");
    }
}
