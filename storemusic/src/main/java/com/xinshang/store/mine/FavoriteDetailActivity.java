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
package com.xinshang.store.mine;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;
import com.xinshang.store.base.BaseActivity;
import com.xinshang.store.data.entities.Favorite;
import com.xinshang.store.helper.GlideApp;
import com.xinshang.store.utils.Constants;
import com.xinshang.store.utils.LogUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class FavoriteDetailActivity extends BaseActivity {
    private static final String TAG = FavoriteDetailActivity.class.getSimpleName();

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_album)
    ImageView mAlbum;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private Favorite mFavorite;

    @Inject
    FavoriteDetailPresenter mPresenter;

    @OnClick(R.id.fab)
    void onFabClick() {
        mPresenter.addAllToPlaylist(mFavorite.favoritesId);
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
        mFavorite = getIntent().getParcelableExtra(Constants.KEY_FAVORITE);

        mToolbar.setTitle(mFavorite.favoriteName);
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }


        GlideApp.with(this)
                .asBitmap()
                .transition(new BitmapTransitionOptions())
                .placeholder(R.drawable.ic_album)
                .load(mFavorite.coverImageUrl).into(mAlbum);

        mFab.setImageResource(R.drawable.ic_playlist_play);

        FavoriteDetailFragment fragment = (FavoriteDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_main);
        if (fragment == null) {
            fragment = FavoriteDetailFragment.newInstance(mFavorite);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_main, fragment)
                    .commit();
        }

        DaggerFavoriteDetailComponent.builder()
                .audientRepositoryComponent(
                        ((StoreMusicApplication) getApplication()).getRepositoryComponent())
                .favoriteDetailPresenterModule(new FavoriteDetailPresenterModule(fragment))
                .build()
                .inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail_base;
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
