package com.xinshang.audient.wxapi;

import com.xinshang.audient.model.AudientRepositoryComponent;
import com.xinshang.common.util.ActivityScoped;

import dagger.Component;

/**
 * Created by koma on 3/9/18.
 */
@ActivityScoped
@Component(dependencies = AudientRepositoryComponent.class, modules = WXEntryPresenterModule.class)
public interface WXEntryComponent {
    void inject(WXEntryActivity wxEntryActivity);
}
