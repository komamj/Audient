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
package com.koma.audient.comment;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.koma.audient.AudientApplication;
import com.koma.audient.R;
import com.koma.common.base.BaseActivity;
import com.koma.common.util.ActivityUtils;
import com.koma.common.util.Constants;

import javax.inject.Inject;

import butterknife.BindView;

public class CommentActivity extends BaseActivity {
    private static final String TAG = CommentActivity.class.getSimpleName();

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.iv_album)
    ImageView mAlbum;

    @Inject
    CommentPresenter mPresenter;

    @Override
    protected void onPermissonGranted() {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }

        long id = getIntent().getLongExtra(Constants.ID, -1);

        String name = getIntent().getStringExtra(Constants.NAME);

        mCollapsingToolbarLayout.setTitle(name);

        CommentFragment commentFragment = (CommentFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main);

        if (commentFragment == null) {
            commentFragment = CommentFragment.newInstance(id);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), commentFragment,
                    R.id.content_main);
        }

        DaggerCommentComponent.builder()
                .audientRepositoryComponent(
                        ((AudientApplication) getApplication()).getRepositoryComponent())
                .commentPresenterModule(new CommentPresenterModule(commentFragment))
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
        return R.layout.activity_detail_base;
    }
}
