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
package com.xinshang.audient.comment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.audient.helper.GlideApp;
import com.xinshang.audient.model.entities.Audient;
import com.xinshang.common.base.BaseActivity;
import com.xinshang.common.util.ActivityUtils;
import com.xinshang.common.util.Constants;
import com.xinshang.common.util.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class CommentActivity extends BaseActivity {
    private static final String TAG = CommentActivity.class.getSimpleName();

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_album)
    ImageView mAlbum;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @OnClick(R.id.fab)
    void onFabClick() {
        EditCommentDialogFragment.show(getSupportFragmentManager(), mAudient);
    }

    private Audient mAudient;

    @Inject
    CommentPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");
    }

    @Override
    protected void onPermissonGranted() {
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }

        mAudient = getIntent().getParcelableExtra(Constants.KEY_AUDIENT);

        mFab.setImageResource(R.drawable.ic_add_white);

        GlideApp.with(this)
                .load(mAudient)
                .thumbnail(0.1f)
                .placeholder(new ColorDrawable(Color.GRAY))
                .into(mAlbum);

        CommentFragment commentFragment = (CommentFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main);

        if (commentFragment == null) {
            commentFragment = CommentFragment.newInstance(mAudient);

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
