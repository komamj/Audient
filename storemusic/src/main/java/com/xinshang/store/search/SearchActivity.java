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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xinshang.store.R;
import com.xinshang.store.base.BaseActivity;
import com.xinshang.store.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;

public class SearchActivity extends BaseActivity {
    private static final String TAG = SearchActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindArray(R.array.search_page_title)
    String[] mPageTitles;

    private List<OnSearchListener> mListeners = new ArrayList<>();

    public void addListener(OnSearchListener listener) {
        this.mListeners.add(listener);
    }

    public void removeListener(OnSearchListener listener) {
        this.mListeners.remove(listener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

        mSearchView.onActionViewExpanded();
        mSearchView.setIconifiedByDefault(true);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String keyword) {
                mSearchView.clearFocus();

                if(isValid(keyword)){
                    for (OnSearchListener listener : mListeners) {
                        listener.onSearch(keyword);
                    }
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        List<Fragment> fragments = new ArrayList<>();
        SongsFragment songsFragment = SongsFragment.newInstance();
        fragments.add(songsFragment);
        PlaylistsFragment playlistsFragment = PlaylistsFragment.newInstance();
        fragments.add(playlistsFragment);
        AlbumsFragment albumsFragment = AlbumsFragment.newInstance();
        fragments.add(albumsFragment);

        SearchFragmentPagerAdapter adapter = new SearchFragmentPagerAdapter(getSupportFragmentManager(),
                fragments, mPageTitles);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private boolean isValid(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }
        return true;
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

    private static class SearchFragmentPagerAdapter extends FragmentPagerAdapter {
        private static final int TAB_COUNT = 3;

        private List<Fragment> mFragments;

        private String[] mTitles;

        public SearchFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] titles) {
            super(fm);

            mFragments = fragments;

            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Nullable
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

    public interface OnSearchListener {
        void onSearch(String keyword);
    }
}
