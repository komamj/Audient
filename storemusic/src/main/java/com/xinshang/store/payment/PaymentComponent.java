package com.xinshang.store.payment;

import com.xinshang.store.data.AudientRepositoryComponent;
import com.xinshang.store.utils.FragmentScoped;

import dagger.Component;

/**
 * Created by koma on 3/1/18.
 */
@FragmentScoped
@Component(dependencies = AudientRepositoryComponent.class, modules = PaymentPresenterModule.class)
public interface PaymentComponent {
    void inject(PaymentDialogFragment dialogFragment);
}
