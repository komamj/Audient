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
package com.xinshang.store.toplist;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xinshang.store.R;
import com.xinshang.store.StoreMusicApplication;
import com.xinshang.store.base.BaseFragment;
import com.xinshang.store.data.entities.Toplist;
import com.xinshang.store.main.MainActivity;
import com.xinshang.store.utils.LogUtils;
import com.xinshang.store.widget.AudientItemDecoration;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TopListFragment extends BaseFragment implements TopListContract.View {
    private static final String TAG = TopListFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    TopListPresenter mPresenter;

    private boolean mIsPrepared;
    private boolean mIsLoaded;

    private TopListAdapter mAdapter;

    public TopListFragment() {
    }

    public static TopListFragment newInstance() {
        TopListFragment fragment = new TopListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        DaggerTopListComponent.builder()
                .audientRepositoryComponent(
                        ((StoreMusicApplication) ((MainActivity) mContext).getApplication()).getRepositoryComponent())
                .topListPresenterModule(new TopListPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        LogUtils.i(TAG, "setUserVisibleHint isVisibleToUser :" + isVisibleToUser);

        if (isVisibleToUser && mIsPrepared && !mIsLoaded) {
            mSwipeRefreshLayout.setRefreshing(true);

            if (mPresenter != null) {
                mPresenter.subscribe();
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_toplist;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.loadTopList();
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark,
                R.color.colorPrimary);

        mAdapter = new TopListAdapter(mContext);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

        mIsPrepared = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtils.i(TAG, "onDestroy");

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public void setPresenter(TopListContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public void setLoadingIndictor(boolean isActive) {
        mSwipeRefreshLayout.setRefreshing(isActive);
    }

    @Override
    public void showSuccessfulMessage() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.loading_successful_message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showLoadingError() {
        if (getView() == null) {
            return;
        }
        Snackbar.make(getView(), R.string.loading_error_message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void showTopLists(List<Toplist> topLists) {
        mIsLoaded = true;

        mAdapter.replace(topLists);
    }

    @Override
    public void showEmpty(boolean forceShow) {
        LogUtils.i(TAG, "showEmpty forceShow :" + forceShow);
    }
}
