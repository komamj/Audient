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
package com.xinshang.audient.toplist;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.ToplistResult;
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

public class TopListPresenter implements TopListContract.Presenter {
    public static final String TAG = TopListPresenter.class.getSimpleName();

    private TopListContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    @Inject
    public TopListPresenter(TopListContract.View view, AudientRepository repository) {
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

        loadTopList();
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        mDisposables.clear();
    }

    @Override
    public void loadTopList() {
        Disposable disposable = mRepository.getTopList()
                .map(new Function<List<ToplistResult>, List<ToplistResult.TopList>>() {
                    @Override
                    public List<ToplistResult.TopList> apply(List<ToplistResult> topListResults) throws Exception {
                        return topListResults.get(0).topLists;
                    }
                })
                .flatMap(new Function<List<ToplistResult.TopList>, Publisher<ToplistResult.TopList>>() {
                    @Override
                    public Publisher<ToplistResult.TopList> apply(List<ToplistResult.TopList> topLists) throws Exception {
                        return Flowable.fromIterable(topLists);
                    }
                })
                .filter(new Predicate<ToplistResult.TopList>() {
                    @Override
                    public boolean test(ToplistResult.TopList topList) throws Exception {
                        return true;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.e(TAG, "loadTopList error :" + throwable.toString());
                    }
                })
                .subscribe(new Consumer<List<ToplistResult.TopList>>() {
                    @Override
                    public void accept(List<ToplistResult.TopList> topLists) throws Exception {
                        if (mView != null) {
                            mView.showTopLists(topLists);
                        }
                    }
                });

        mDisposables.add(disposable);
    }
}
