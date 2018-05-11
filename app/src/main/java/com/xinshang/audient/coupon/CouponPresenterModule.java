package com.xinshang.audient.coupon;

import dagger.Module;
import dagger.Provides;

/**
 * Created by koma on 5/11/18.
 */

@Module
public class CouponPresenterModule {
    private final CouponContract.View mView;

    public CouponPresenterModule(CouponContract.View view) {
        mView = view;
    }

    @Provides
    CouponContract.View provideContractView() {
        return this.mView;
    }
}
