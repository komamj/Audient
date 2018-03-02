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
package com.xinshang.store.store;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by koma_20 on 2018/3/1.
 */

public class StoreInfoActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        StoreInfoFragment fragment = (StoreInfoFragment) getSupportFragmentManager().findFragmentById(R.id.content_main);
        if (fragment == null) {
            fragment = StoreInfoFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, fragment)
                    .commit();
        }
        DaggerStoreInfoComponent.builder().audientRepositoryComponent(
                ((StoreMusicApplication) getApplication()).getRepositoryComponent())
                .storeInfoPresenterModule(new StoreInfoPresenterModule(fragment))
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
}
