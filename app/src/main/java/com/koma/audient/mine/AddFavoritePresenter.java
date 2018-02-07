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
package com.koma.audient.mine;

import com.koma.audient.model.AudientRepository;
import com.koma.audient.model.entities.BaseResponse;
import com.koma.audient.util.MessageEvent;
import com.koma.common.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class AddFavoritePresenter implements AddFavoriteContract.Presenter {
    public static final String TAG = AddFavoritePresenter.class.getSimpleName();

    private AddFavoriteContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    @Inject
    public AddFavoritePresenter(AddFavoriteContract.View view, AudientRepository repository) {
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
    public void addFavorite(String name) {
        mRepository.addFavorite(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        EventBus.getDefault().post(MessageEvent.MESSAGE_ADD_FAVORITES_COMPLETED);
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "addFavorite error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
