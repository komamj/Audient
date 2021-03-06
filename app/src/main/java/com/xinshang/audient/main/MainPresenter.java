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
package com.xinshang.audient.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.ApiResponse;
import com.xinshang.audient.model.entities.Coupon;
import com.xinshang.audient.model.entities.Store;
import com.xinshang.audient.model.entities.User;
import com.xinshang.audient.model.entities.UserResponse;
import com.xinshang.audient.util.ImageUtils;
import com.xinshang.common.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class MainPresenter implements MainContract.Presenter {
    public static final String TAG = MainPresenter.class.getSimpleName();

    private MainContract.View mView;

    private AudientRepository mRepository;

    private CompositeDisposable mDisposables;

    @Inject
    public MainPresenter(MainContract.View view, AudientRepository repository) {
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

        loadUserInfo();

        loadStoreInfo();
    }

    @Override
    public void unSubscribe() {
        LogUtils.i(TAG, "unSubscribe");

        mDisposables.clear();
    }

    @Override
    public void loadMyCoupons() {
        Disposable disposable = mRepository.getMyCoupon("available")
                .map(new Function<ApiResponse<List<Coupon>>, Integer>() {
                    @Override
                    public Integer apply(ApiResponse<List<Coupon>> response) {
                        int count = 0;
                        for (Coupon coupon : response.data) {
                            if (coupon != null && coupon.id != null && !coupon.used) {
                                count++;
                            }
                        }
                        return count;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer count) {
                        if (mView != null && count != 0) {
                            mView.showCoupons(count);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadMyCoupons error :" + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void loadUserInfo() {
        Disposable disposable = mRepository.getUserInfo()
                .map(new Function<UserResponse, User>() {
                    @Override
                    public User apply(UserResponse userResponse) throws Exception {
                        return userResponse.user;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<User>() {
                    @Override
                    public void onNext(User user) {
                        if (mView != null) {
                            mView.showUserInfo(user);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadUserInfo error :" + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void blurBitmap(final Bitmap bitmap, final Context context, final int inSampleSize) {
        Disposable disposable = Flowable.create(new FlowableOnSubscribe<Drawable>() {
            @Override
            public void subscribe(FlowableEmitter<Drawable> emitter) throws Exception {
                Drawable drawable = ImageUtils.createBlurredImageFromBitmap(bitmap, context,
                        inSampleSize);

                emitter.onNext(drawable);
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Drawable>() {
                    @Override
                    public void onNext(Drawable drawable) {
                        if (mView != null) {
                            mView.showBlurBackground(drawable);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "blurBitmap erorr : " + t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void loadStoreInfo() {
        Disposable disposable = mRepository.getStoreInfo(mRepository.getStoreId())
                .map(new Function<ApiResponse<Store>, Store>() {
                    @Override
                    public Store apply(ApiResponse<Store> storeApiResponse) throws Exception {
                        return storeApiResponse.data;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Store>() {
                    @Override
                    public void onNext(Store store) {
                        if (mView != null) {
                            mView.showStoreInfo(store);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtils.e(TAG, "loadStoreInfo error : " + t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mDisposables.add(disposable);
    }

    @Override
    public String getMyShareCode() {
        return mRepository.getShareCode();
    }
}
