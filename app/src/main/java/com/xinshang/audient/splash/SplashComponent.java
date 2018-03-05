package com.xinshang.audient.splash;

import com.xinshang.audient.model.AudientRepositoryComponent;
import com.xinshang.common.util.ActivityScoped;

import dagger.Component;

/**
 * Created by koma on 3/5/18.
 */
@ActivityScoped
@Component(dependencies = AudientRepositoryComponent.class, modules = SplashPresenterModule.class)
public interface SplashComponent {
    void inject(SplashActivity splashActivity);
}
