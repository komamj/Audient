package com.xinshang.store.payment;

import com.xinshang.store.data.AudientRepository;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by koma on 3/1/18.
 */

public class PaymentPresenter implements PaymentContract.Presenter {
    private static final String TAG = PaymentPresenter.class.getSimpleName();

    private final PaymentContract.View mView;
    private final AudientRepository mRepository;
    private final CompositeDisposable mDisposables;

    @Inject
    public PaymentPresenter(PaymentContract.View view, AudientRepository repository) {
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

    }
}
