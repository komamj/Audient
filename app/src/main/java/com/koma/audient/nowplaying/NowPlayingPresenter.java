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
package com.koma.audient.nowplaying;

import com.koma.audient.model.AudientRepository;
import com.koma.audient.model.entities.Audient;
import com.koma.audient.model.entities.Lyric;
import com.koma.audient.model.entities.LyricResult;
import com.koma.audient.model.entities.SongDetailResult;
import com.koma.common.util.LogUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class NowPlayingPresenter implements NowPlayingContract.Presenter {
    public static final String TAG = NowPlayingPresenter.class.getSimpleName();

    private NowPlayingContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    @Inject
    public NowPlayingPresenter(NowPlayingContract.View view, AudientRepository repository) {
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

        loadAudient(mView.getAudientId());

        loadLyric(mView.getAudientId());
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        mDisposables.clear();
    }

    @Override
    public void loadAudient(String id) {
        Disposable disposable = mRepository.getSongDetailResult(id)
                .map(new Function<SongDetailResult, Audient>() {
                    @Override
                    public Audient apply(SongDetailResult songDetailResult) throws Exception {
                        return songDetailResult.audient;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Audient>() {
                    @Override
                    public void onNext(Audient audient) {
                        if (mView.isActive()) {
                            mView.showAudient(audient);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadAudient error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public void loadLyric(String id) {
        Disposable disposable = mRepository.getLyricResult(id)
                .map(new Function<LyricResult, Lyric>() {
                    @Override
                    public Lyric apply(LyricResult lyricResult) throws Exception {
                        return lyricResult.lyric;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Lyric>() {
                    @Override
                    public void onNext(Lyric lyric) {
                        if (mView.isActive()) {
                            mView.showLyric(lyric);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadLyric error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }
}
