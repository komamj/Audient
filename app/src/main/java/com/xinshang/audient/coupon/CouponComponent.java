package com.xinshang.audient.coupon;

import com.xinshang.audient.model.AudientRepositoryComponent;
import com.xinshang.common.util.FragmentScoped;

import dagger.Component;

/**
 * Created by koma on 5/11/18.
 */
@FragmentScoped
@Component(dependencies = AudientRepositoryComponent.class, modules = CouponPresenterModule.class)
public interface CouponComponent {
    void inject(CouponDialogFragment dialogFragment);
}
