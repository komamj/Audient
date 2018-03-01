package com.xinshang.audient.payment;

import dagger.Module;
import dagger.Provides;

/**
 * Created by koma on 3/1/18.
 */

@Module
public class PaymentPresenterModule {
    private final PaymentContract.View mView;

    public PaymentPresenterModule(PaymentContract.View view) {
        this.mView = view;
    }

    @Provides
    PaymentContract.View providePaymentContractView() {
        return this.mView;
    }
}
