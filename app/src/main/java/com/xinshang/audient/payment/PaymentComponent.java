package com.xinshang.audient.payment;

import com.xinshang.audient.model.AudientRepositoryComponent;
import com.xinshang.common.util.FragmentScoped;

import dagger.Component;

/**
 * Created by koma on 3/1/18.
 */
@FragmentScoped
@Component(dependencies = AudientRepositoryComponent.class, modules = PaymentPresenterModule.class)
public interface PaymentComponent {
    void inject(PaymentDialogFragment dialogFragment);
}
