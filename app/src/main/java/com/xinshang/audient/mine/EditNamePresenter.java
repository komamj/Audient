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
package com.xinshang.audient.mine;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.BaseResponse;
import com.xinshang.audient.model.entities.MessageEvent;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class EditNamePresenter implements EditNameContract.Presenter {
    private static final String TAG = EditNamePresenter.class.getSimpleName();

    private final EditNameContract.View mView;

    private final AudientRepository mRepository;

    private final CompositeDisposable mDisposables;

    @Inject
    public EditNamePresenter(EditNameContract.View view, AudientRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposables = new CompositeDisposable();
    }

    @Inject
    void setUpListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mDisposables.clear();
    }

    @Override
    public void modifyFavoritesName(String favoritesId, String name) {
        mRepository.modifyFavoritesName(favoritesId, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<BaseResponse>() {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.resultCode == 0) {
                            LogUtils.i(TAG, "modifyFavoritesName :" + response.message);

                            EventBus.getDefault().post(new MessageEvent(
                                    Constants.MESSAGE_MY_FAVORITES_CHANGED));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "modifyFavoritesName error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
