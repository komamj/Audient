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
package com.koma.audient.toplist;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.koma.audient.R;
import com.koma.audient.base.AudientAdapter;
import com.koma.audient.model.entities.Audient;
import com.koma.audient.widget.AudientItemDecoration;
import com.koma.common.base.BaseFragment;
import com.koma.common.util.Constants;
import com.koma.common.util.LogUtils;

import java.util.List;

import butterknife.BindView;

public class ToplistDetailFragment extends BaseFragment implements ToplistDetailContract.View {
    private static final String TAG = ToplistDetailFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private int mTopId;

    private String mShowTime;

    private AudientAdapter mAdapter;

    private ToplistDetailContract.Presenter mPresenter;

    public ToplistDetailFragment() {
    }

    public static ToplistDetailFragment newInstance(int topId, String showTime) {
        ToplistDetailFragment fragment = new ToplistDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_TOP_ID, topId);
        bundle.putString(Constants.KEY_UPDATE, showTime);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogUtils.i(TAG, "onCreate");

        if (getArguments() != null) {
            mTopId = getArguments().getInt(Constants.KEY_TOP_ID);

            mShowTime = getArguments().getString(Constants.KEY_UPDATE);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LogUtils.i(TAG, "onViewCreated");

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.loadToplistDetail(mTopId, mShowTime);
                }
            }
        });

        mSwipeRefreshLayout.setRefreshing(true);

        mAdapter = new AudientAdapter(mContext);

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new AudientItemDecoration(mContext));
        mRecyclerView.setAdapter(mAdapter);

        if (mPresenter != null) {
            mPresenter.loadToplistDetail(mTopId, mShowTime);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base;
    }

    @Override
    public void setPresenter(ToplistDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @Override
    public int getTopId() {
        return this.mTopId;
    }

    @Override
    public String getShowTime() {
        return this.mShowTime;
    }

    @Override
    public void showToplistDetail(List<Audient> audients) {
        mSwipeRefreshLayout.setRefreshing(false);

        mAdapter.update(audients);
    }
}