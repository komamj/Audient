package com.xinshang.audient.coupon;

import com.xinshang.audient.model.AudientRepository;
import com.xinshang.audient.model.entities.ApiResponse;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by koma on 5/11/18.
 */

public class CouponPresenter implements CouponContract.Presenter {
    private final CouponContract.View mView;

    private final CompositeDisposable mDisposables;

    private final AudientRepository mRepository;

    @Inject
    public CouponPresenter(CouponContract.View view, AudientRepository repository) {
        mView = view;

        mRepository = repository;

        mDisposables = new CompositeDisposable();
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }

    @Override
    public void loadMyCoupons(String shareCode) {
        Disposable disposable = mRepository.shareMyCode(shareCode)
                .map(new Function<ApiResponse, Integer>() {
                    @Override
                    public Integer apply(ApiResponse response) {
                        return response.resultCode;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer resultCode) {
                        if (resultCode == 0) {
                            mView.showSuccessfulMessage();
                        } else {
                            mView.showFailedMessage();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        mDisposables.add(disposable);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        mDisposables.clear();
    }
}
