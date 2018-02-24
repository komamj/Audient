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
package com.xinshang.audient.search;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.audient.model.entities.SearchResult;
import com.xinshang.common.util.LogUtils;

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
                        String name = audient.mediaName.trim().toUpperCase();
                        String artistName = audient.artistName.trim().toUpperCase();
                        String albumName = audient.albumName.trim().toUpperCase();
                        String title = audient.title.trim().toUpperCase();

                        return !isInvalid(name) && !isInvalid(artistName) && !isInvalid(albumName)
                                && !isInvalid(title);
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

        return keyword.contains("DJ") || keyword.contains("哀") || keyword.contains("葬")
                || keyword.contains("FUNERAL") || keyword.contains("棺") || keyword.contains("蛊")
                || keyword.contains("灵") || keyword.contains("魂") || keyword.contains("SOUL")
                || keyword.contains("诅") || keyword.contains("咒") || keyword.contains("呪")
                || keyword.contains("亡") || keyword.contains("舞曲") || keyword.contains("disco")
                || keyword.contains("迪斯科") || keyword.contains("卡拉") || keyword.contains("OK")
                || keyword.contains("蹦") || keyword.contains("劲爆") || keyword.contains("舞")
                || keyword.contains("DANCE") || keyword.contains("节奏") || keyword.contains("嗨")
                || keyword.contains("HIGH") || keyword.contains("REMIX") || keyword.contains("MIX")
                || keyword.contains("摇") || keyword.contains("DOWNTEMPO") || keyword.contains("滚")
                || keyword.contains("ROCK") || keyword.contains("说唱") || keyword.contains("RAP")
                || keyword.contains("嘻哈") || keyword.contains("动次") || keyword.contains("打次")
                || keyword.contains("打碟") || keyword.contains("电音") || keyword.contains("控");
    }
}
