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
package com.koma.audient.search;

import com.koma.audient.model.AudientRepository;
import com.koma.audient.model.entities.MusicFileItem;
import com.koma.common.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SearchPresenter implements SearchContract.Presenter {
    public static final String TAG = SearchPresenter.class.getSimpleName();

    private SearchContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    @Inject
    public SearchPresenter(SearchContract.View view, AudientRepository repository) {
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
    public void loadSearchResults(String keyword) {
        mDisposables.clear();

        if (mView != null) {
            mView.showProgressBar(true);
        }

        mRepository.getSearchReults(keyword, "4", 10, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<MusicFileItem>>() {
                    @Override
                    public void onNext(List<MusicFileItem> musicFileItems) {
                        if (mView != null) {
                            mView.showProgressBar(false);
                            mView.showMusicFileItems(musicFileItems);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                })
                /*.flatMap(new Function<List<MusicFileItem>, Flowable<MusicFileItem>>() {
                    @Override
                    public Flowable<MusicFileItem> apply(final List<MusicFileItem> musicFileItems) throws Exception {
                        return Flowable.create(new FlowableOnSubscribe<MusicFileItem>() {
                            @Override
                            public void subscribe(FlowableEmitter<MusicFileItem> emitter) throws Exception {
                                for (MusicFileItem musicFileItem : musicFileItems) {
                                    LogUtils.i(TAG, "loadSearchResults musicFileItem :" + musicFileItem.musicName);
                                    emitter.onNext(musicFileItem);
                                }
                                emitter.onComplete();
                            }
                        }, BackpressureStrategy.LATEST);
                    }
                }).flatMap(new Function<MusicFileItem, Flowable<Audient>>() {
            @Override
            public Flowable<Audient> apply(final MusicFileItem musicFileItem) throws Exception {
                return mRepository.getAlbum(musicFileItem)
                        .map(new Function<Album, Audient>() {
                            @Override
                            public Audient apply(Album album) throws Exception {
                                Audient audient = new Audient();
                                audient.musicName = musicFileItem.musicName;
                                audient.contentId = musicFileItem.contentId;
                                audient.actorName = musicFileItem.actorName;
                                audient.albumUrl = album.url;
                                return audient;
                            }
                        });
            }
        }).toList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Audient>>() {
                    @Override
                    public void onSuccess(List<Audient> audients) {
                        LogUtils.i(TAG, "loadSearchResults onSuccess");
                        if (mView != null) {
                            mView.showProgressBar(false);
                            mView.showMusicFileItems(audients);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(TAG, "loadSearchResults error :" + e.toString());
                    }
                })*/;
    }
}
