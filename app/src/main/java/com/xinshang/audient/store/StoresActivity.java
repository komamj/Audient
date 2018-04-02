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
package com.xinshang.audient.store;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.common.base.BaseActivity;
import com.xinshang.common.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by koma on 4/2/18.
 */

public class StoresActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    StoresPresenter mPresenter;

    @Override
    protected void onPermissonGranted() {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }

        StoresFragment storesFragment = (StoresFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main);

        if (storesFragment == null) {
            storesFragment = StoresFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), storesFragment,
                    R.id.content_main);
        }

        DaggerStoresComponent.builder()
                .audientRepositoryComponent(
                        ((AudientApplication) getApplication()).getRepositoryComponent())
                .storesPresenterModule(new StoresPresenterModule(storesFragment))
                .build()
                .inject(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_base;
    }
}
