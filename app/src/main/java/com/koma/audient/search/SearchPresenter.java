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
import com.koma.audient.model.entities.Audient;
import com.koma.audient.model.entities.SearchResult;
import com.koma.common.util.LogUtils;

import org.reactivestreams.Publisher;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

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
        LogUtils.i(TAG, "loadSearchResults :" + keyword);

        mDisposables.clear();

        if (isInvalid(keyword)) {
            if (mView.isActive()) {
                mView.showEmpty(true);
            }

            return;
        }

        if (mView != null) {
            mView.showProgressBar(true);
        }

        Disposable disposable = mRepository.getSearchReult(keyword)
                .flatMap(new Function<SearchResult, Publisher<Audient>>() {
                    @Override
                    public Publisher<Audient> apply(SearchResult searchResult) throws Exception {
                        return Flowable.fromIterable(searchResult.dataBean.audients);
                    }
                })
                .filter(new Predicate<Audient>() {
                    @Override
                    public boolean test(Audient audient) throws Exception {
                        String name = audient.name.trim().toUpperCase();
                        String artistName = audient.artist.name.trim().toUpperCase();
                        String albumName = audient.album.name.trim().toUpperCase();

                        return !name.contains("DJ") && !name.contains("哀乐")
                                && !artistName.contains("DJ") && !artistName.contains("哀乐")
                                && !albumName.contains("DJ") && !albumName.contains("哀乐");
                    }
                }).toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(TAG, "loadSearchResults error :" + throwable.toString());

                        if (mView.isActive()) {
                            mView.showProgressBar(false);

                            mView.showLoadingError();
                        }
                    }
                })
                .subscribe(new Consumer<List<Audient>>() {
                    @Override
                    public void accept(List<Audient> audients) throws Exception {
                        if (mView.isActive()) {
                            mView.showProgressBar(false);
                            mView.showAudients(audients);
                        }
                    }
                });

        mDisposables.add(disposable);
    }

    private boolean isInvalid(String word) {
        String keyword = word.trim().toUpperCase();

        return keyword.contains("DJ") || keyword.contains("哀乐");
    }
}
