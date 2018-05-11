package com.xinshang.audient.coupon;

import com.xinshang.audient.model.AudientRepository;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

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
    public void loadMyCoupons(String freeCode) {

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

    }
}
