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
package com.xinshang.store.search;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;
import com.xinshang.store.base.BaseActivity;
import com.xinshang.store.data.entities.AlbumResponse;
import com.xinshang.store.helper.GlideApp;
import com.xinshang.store.utils.ActivityUtils;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by koma on 4/13/18.
 */

public class AlbumDetailActivity extends BaseActivity {
    private static final String TAG = AlbumDetailActivity.class.getSimpleName();

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_album)
    ImageView mAlbum;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    @Inject
    AlbumDetailPresenter mPresenter;

    @OnClick(R.id.fab)
    void onFabClick() {
        if (mPresenter != null) {
            mPresenter.playAll();
        }
    }

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

        mFab.setImageResource(R.drawable.ic_playlist_play);

        AlbumResponse.Album album = getIntent().getParcelableExtra(Constants.KEY_ALBUMS);

        GlideApp.with(this)
                .load(album.picUrl)
                .thumbnail(0.1f)
                .placeholder(new ColorDrawable(Color.GRAY))
                .into(mAlbum);

        mCollapsingToolbarLayout.setTitle(album.name);

        AlbumDetailFragment fragment = (AlbumDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main);
        if (fragment == null) {
            fragment = AlbumDetailFragment.newInstance(album);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment,
                    R.id.content_main);
        }

        DaggerAlbumDetailComponent.builder()
                .audientRepositoryComponent(
                        ((StoreMusicApplication) getApplication()).getRepositoryComponent())
                .albumDetailPresenterModule(new AlbumDetailPresenterModule(fragment))
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
