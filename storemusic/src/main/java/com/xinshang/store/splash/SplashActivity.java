/*
 * Copyright 2017 Koma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xinshang.store.splash;

import android.os.Bundle;

import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;
import com.xinshang.store.base.BaseActivity;
import com.xinshang.store.utils.ActivityUtils;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {
    @Inject
    SplashPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPermissonGranted() {
        SplashFragment fragment = (SplashFragment) getSupportFragmentManager().findFragmentById(R.id.content_main);
        if (fragment == null) {
            fragment = SplashFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.content_main);
        }
        DaggerSplashComponent.builder()
                .audientRepositoryComponent(
                        ((StoreMusicApplication) getApplication()).getRepositoryComponent())
                .splashPresenterModule(new SplashPresenterModule(fragment))
                .build()
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
