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
package com.koma.audient.search;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.koma.audient.AudientApplication;
import com.koma.audient.R;
import com.koma.common.base.BaseActivity;
import com.koma.common.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;

public class SearchActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_view)
    SearchView mSearchView;

    @Inject
    SearchPresenter mPresenter;

    @Override
    protected void onPermissonGranted() {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main);

        if (searchFragment == null) {
            searchFragment = SearchFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), searchFragment,
                    R.id.content_main);
        }

        DaggerSearchComponent.builder()
                .audientRepositoryComponent(
                        ((AudientApplication) getApplication()).getRepositoryComponent())
                .searchPresenterModule(new SearchPresenterModule(searchFragment))
                .build()
                .inject(this);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                mPresenter.loadSearchResults(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
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
        return R.layout.activity_search;
    }
}