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
package com.xinshang.store.login;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;
import com.xinshang.store.base.BaseActivity;
import com.xinshang.store.data.entities.StoreKeeper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Inject
    LoginPresenter mPresenter;

    @OnClick(R.id.btn_login)
    void loginWeChat() {
        StoreKeeper storeKeeper = new StoreKeeper();
        storeKeeper.nickName = "koma_mj_boss";
        storeKeeper.isDefaultName = false;

        mPresenter.login(storeKeeper);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPermissonGranted() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }

        // inject presenter layer
        DaggerLoginComponent.builder().audientRepositoryComponent(
                ((StoreMusicApplication) getApplication()).getRepositoryComponent())
                .loginPresenterModule(new LoginPresenterModule(this))
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

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {

    }

    @Override
    public void onLoginFinished() {
        this.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
