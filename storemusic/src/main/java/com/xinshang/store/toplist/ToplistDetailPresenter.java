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
import com.xinshang.store.data.entities.TencentMusic;
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

        Disposable disposable = mRepository.getToplistDetail(topId, showTime)
                .map(new Function<ToplistDetailResult, List<TencentMusic>>() {
                    @Override
                    public List<TencentMusic> apply(ToplistDetailResult toplistDetailResult) throws Exception {
                        return toplistDetailResult.dataBean.audients;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<TencentMusic>>() {
                    @Override
                    public void onNext(List<TencentMusic> audients) {
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
}
