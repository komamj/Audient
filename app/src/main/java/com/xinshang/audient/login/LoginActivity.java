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
package com.xinshang.audient.login;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.common.base.BaseActivity;
import com.xinshang.common.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by koma_20 on 2018/3/10.
 */

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    LoginPresenter mPresenter;

    @Override
    protected void onPermissonGranted() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main);
        if (fragment == null) {
            fragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.content_main);
        }

        // inject presenter layer
        DaggerLoginComponent.builder().audientRepositoryComponent(
                ((AudientApplication) getApplication()).getRepositoryComponent())
                .loginPresenterModule(new LoginPresenterModule(fragment))
                .build()
                .inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }
}
