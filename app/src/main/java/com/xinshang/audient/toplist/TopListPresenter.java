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
import com.xinshang.audient.model.entities.ApiResponse;
import com.xinshang.audient.model.entities.ToplistDataBean;
import com.xinshang.common.util.LogUtils;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

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
        if (mView.isActive()) {
            mView.setLoadingIndictor(true);
        }

        Disposable disposable = mRepository.getTopList()
                .map(new Function<ApiResponse<List<ToplistDataBean>>, List<ToplistDataBean>>() {
                    @Override
                    public List<ToplistDataBean> apply(ApiResponse<List<ToplistDataBean>> listApiResponse) throws Exception {
                        return listApiResponse.data;
                    }
                })
                .map(new Function<List<ToplistDataBean>, List<ToplistDataBean.TopList>>() {
                    @Override
                    public List<ToplistDataBean.TopList> apply(List<ToplistDataBean> topListResults) throws Exception {
                        List<ToplistDataBean.TopList> topLists = new ArrayList<>();
                        for (ToplistDataBean.TopList topList : topListResults.get(0).topLists) {
                            if (isLegal(topList)) {
                                topLists.add(topList);
                            }
                        }
                        return topLists;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<ToplistDataBean.TopList>>() {
                    @Override
                    public void onNext(List<ToplistDataBean.TopList> topLists) {
                        if (mView.isActive()) {
                            mView.showTopLists(topLists);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (t instanceof ConnectException) {
                            LogUtils.e(TAG, "loadTopList connect error :");
                        }
                        LogUtils.e(TAG, "loadTopList error :" + t.toString());
                        if (mView.isActive()) {
                            mView.setLoadingIndictor(false);
                            mView.showLoadingError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView.isActive()) {
                            mView.setLoadingIndictor(false);
                            mView.showSuccessfulMessage();
                        }
                    }
                });

        mDisposables.add(disposable);
    }

    private static boolean isLegal(ToplistDataBean.TopList topList) {
        String name = topList.title;
        if (name.contains("巅峰榜·歌手") || name.contains("·网络歌曲")
                || name.contains("·MV") || name.contains("·腾讯音乐人原创榜")) {
            return false;
        }
        return true;
    }
}
