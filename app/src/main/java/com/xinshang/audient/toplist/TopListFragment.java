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
package com.xinshang.audient.toplist;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xinshang.audient.AudientApplication;
import com.xinshang.audient.R;
import com.xinshang.audient.main.MainActivity;
import com.xinshang.audient.model.entities.ToplistResult;
import com.xinshang.audient.widget.AudientItemDecoration;
import com.xinshang.common.base.BaseFragment;
import com.xinshang.common.util.LogUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TopListFragment extends BaseFragment implements TopListContract.View {
    private static final String TAG = TopListFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private TopListAdapter mAdapter;

    @Inject
    TopListPresenter mPresenter;

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
                        ((AudientApplication) ((MainActivity) mContext).getApplication()).getRepositoryComponent())
                .topListPresenterModule(new TopListPresenterModule(this))
                .build()
                .inject(this);
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

        if (mPresenter != null) {
            mPresenter.subscribe();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        LogUtils.i(TAG, "onDestroyView");

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
    public void showTopLists(List<ToplistResult.TopList> topLists) {
        mAdapter.replace(topLists);
    }
}
