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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xinshang.audient.R;
import com.xinshang.audient.main.MainActivity;
import com.xinshang.audient.model.entities.Store;
import com.xinshang.audient.widget.AudientItemDecoration;
import com.xinshang.common.base.BaseFragment;
import com.xinshang.common.util.LogUtils;

import java.util.List;

import butterknife.BindView;

/**
 * Created by koma on 4/2/18.
 */

public class StoresFragment extends BaseFragment implements StoresContract.View {
    private static final String TAG = StoresFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private StoresAdapter mAdapter;

    private StoresContract.Presenter mPresenter;

    public static StoresFragment newInstance() {
        StoresFragment fragment = new StoresFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.loadStores();
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark,
                R.color.colorPrimary);

        setLoadingIndicator(true);

        mAdapter = new StoresAdapter(mContext);
        mAdapter.setListener(new StoresAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Store store) {
                if (mPresenter != null) {
                    mPresenter.persistenceStore(store);
                }
            }
        });

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mPresenter != null) {
            mPresenter.unSubscribe();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base;
    }

    @Override
    public void setPresenter(StoresContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
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
    public void showEmpty(boolean forceShow) {

    }

    @Override
    public void setLoadingIndicator(boolean isActive) {
        mSwipeRefreshLayout.setRefreshing(isActive);
    }

    @Override
    public void showStores(List<Store> stores) {
        mAdapter.replace(stores);
    }

    @Override
    public void showMainView() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        StoresActivity activity = (StoresActivity) mContext;
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        activity.finish();
    }
}
