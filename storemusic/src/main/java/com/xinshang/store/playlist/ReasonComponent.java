package com.xinshang.store.playlist;

import com.xinshang.store.data.AudientRepositoryComponent;
import com.xinshang.store.utils.FragmentScoped;

import dagger.Component;

@FragmentScoped
@Component(dependencies = AudientRepositoryComponent.class, modules = ReasonPresenterModule.class)
public interface ReasonComponent {
    void inject(ReasonDialogFragment reasonDialogFragment);
}
