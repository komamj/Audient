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
package com.xinshang.audient.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.audient.coupon.CouponDialogFragment;
import com.xinshang.audient.feedback.FeedbackDialogFragment;
import com.xinshang.audient.help.HelpDialogFragment;
import com.xinshang.audient.helper.GlideApp;
import com.xinshang.audient.mine.MineFragment;
import com.xinshang.audient.model.entities.Store;
import com.xinshang.audient.model.entities.User;
import com.xinshang.audient.playlist.PlaylistFragment;
import com.xinshang.audient.search.SearchActivity;
import com.xinshang.audient.setting.SettingsActivity;
import com.xinshang.audient.store.StoresActivity;
import com.xinshang.audient.toplist.TopListFragment;
import com.xinshang.common.base.BaseActivity;
import com.xinshang.common.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainContract.View,
        NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindArray(R.array.main_page_title)
    String[] mPageTitles;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.tv_title)
    TextView mTitle;

    @OnClick(R.id.tv_title)
    void launchStoresActivity() {
        Intent intent = new Intent(this, StoresActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    TextView mName;
    TextView mShareCode;
    ImageView mUserImage;
    View mBlurImage;

    @Inject
    MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");
    }

    @Override
    protected void onPermissonGranted() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.i(TAG, "onClick");
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        View headerView = mNavigationView.getHeaderView(0);
        mName = headerView.findViewById(R.id.tv_user_name);
        mShareCode = headerView.findViewById(R.id.tv_share_code);
        mUserImage = headerView.findViewById(R.id.iv_user);
        mBlurImage = headerView.findViewById(R.id.header_layout);

        // inject presenter layer
        DaggerMainComponent.builder().audientRepositoryComponent(
                ((AudientApplication) getApplication()).getRepositoryComponent())
                .mainPresenterModule(new MainPresenterModule(this))
                .build()
                .inject(this);

        mShareCode.setText(mPresenter.getMyShareCode());

        mPresenter.loadMyCoupons();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(MineFragment.newInstance());
        fragments.add(PlaylistFragment.newInstance());
        fragments.add(TopListFragment.newInstance());

        AudientAdapter audientAdapter = new AudientAdapter(getSupportFragmentManager(),
                fragments, mPageTitles);
        mViewPager.setAdapter(audientAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);

        setIntent(newIntent);

        LogUtils.i(TAG, "onNewIntent");
    }

    @Override
    public void onResume() {
        super.onResume();

        LogUtils.i(TAG, "onResume");

        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        LogUtils.i(TAG, "onPause");

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_info) {
        } else if (id == R.id.nav_track) {
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (id == R.id.nav_share) {
            String myShareCode = mPresenter.getMyShareCode();
            LogUtils.i(TAG, "myShareCode : " + myShareCode);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "哎哟，不错哦！我用“即乐”成功在店内点歌，你也来试试。https://fir.im/2sq8点击下载，复制我的分享码进入app可领取免费点歌优惠券 " + mShareCode.getText());
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else if (id == R.id.nav_feedback) {
            FeedbackDialogFragment.show(getSupportFragmentManager());
        } else if (id == R.id.nav_coupon) {
            CouponDialogFragment.show(getSupportFragmentManager());
        } else if (id == R.id.nav_help) {
            HelpDialogFragment.show(getSupportFragmentManager());
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {

    }

    @Override
    public void showUserInfo(User user) {
        GlideApp.with(this)
                .asBitmap()
                .placeholder(R.drawable.ic_user)
                .thumbnail(0.1f)
                .circleCrop()
                .load(user.avatar)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final @NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        mUserImage.setImageBitmap(resource);

                        if (mPresenter != null) {
                            mPresenter.blurBitmap(resource, getApplicationContext(), 8);
                        }
                    }
                });

        mName.setText(user.nickName);
    }

    @Override
    public void showBlurBackground(Drawable drawable) {
        if (mBlurImage.getBackground() != null) {
            final TransitionDrawable td =
                    new TransitionDrawable(new Drawable[]{
                            mBlurImage.getBackground(),
                            drawable
                    });
            mBlurImage.setBackground(td);
            td.startTransition(400);

        } else {
            mBlurImage.setBackground(drawable);
        }
    }

    @Override
    public void showStoreInfo(Store store) {
        if (store != null) {
            mTitle.setText(store.name);
        }
    }

    @Override
    public void showCoupons(int count) {
        Snackbar.make(mViewPager,
                "您还有" + count + "张免费点歌券未使用，现在就去点歌吧！",
                Snackbar.LENGTH_INDEFINITE).setAction("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do nothing
            }
        }).show();
    }

    private static class AudientAdapter extends FragmentPagerAdapter {
        private static final int TAB_COUNT = 3;

        private List<Fragment> mFragments;

        private String[] mTitles;

        AudientAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
            super(fm);

            mFragments = fragments;

            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }
}
