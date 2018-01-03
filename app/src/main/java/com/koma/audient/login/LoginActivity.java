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
package com.koma.audient.login;

import com.koma.audient.AudientApplication;
import com.koma.audient.R;
import com.koma.common.base.BaseActivity;
import com.koma.common.util.ActivityUtils;

import javax.inject.Inject;

/**
 * Created by koma on 1/3/18.
 */

public class LoginActivity extends BaseActivity {
    @Inject
    LoginPresenter mPresenter;

    @Override
    protected void onPermissonGranted() {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main);

        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), loginFragment,
                    R.id.content_main);
        }

        DaggerLoginComponent.builder().audientRepositoryComponent(
                ((AudientApplication) getApplication()).getRepositoryComponent())
                .loginPresenterModule(new LoginPresenterModule(loginFragment))
                .build();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }
}
